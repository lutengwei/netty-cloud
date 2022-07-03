package com.lutw.common.redis.service;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author ruoyi
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisService {
    @Autowired
    public RedisTemplate redisTemplate;

    public static String LOCK = "lock";

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 解锁的lua脚本
     */
    public static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 查看hash中，指定的键是否存在。
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return
     */
    public Boolean hasCacheMapKey(final String key, final String hKey) {
        return redisTemplate.opsForHash().hasKey(key, hKey);
    }

    /**
     * 删除hash中，指定的键
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return
     */
    public Long delCacheMap(final String key, final String... hKey) {
        return redisTemplate.opsForHash().delete(key, hKey);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * redisTemplate 尝试加锁
     */
    public boolean tryLock(String key, String value, long expireSeconds) {
        final String lockKey = new StringBuilder(LOCK).append(key).toString();
        Boolean setBool = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> stringRedisSerializer =
                        (RedisSerializer<String>) redisTemplate.getKeySerializer();
                byte[] keyByte = stringRedisSerializer.serialize(lockKey);
                //springboot 2.0以上的spring-data-redis 包默认使用 lettuce连接包
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
                byte[] valueBytes = valueSerializer.serialize(value);
                Boolean setBool = connection.set(keyByte, valueBytes, Expiration.seconds(expireSeconds),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
                return setBool;
            }
        });
        if (setBool == null) {
            return false;
        }
        return setBool;
    }

    /**
     * 解锁
     */
    public Boolean unlock(String lockKey, String lockValue) {
        lockKey = new StringBuilder(LOCK).append(lockKey).toString();
        //注意是Long类型,而不是Integer
        RedisScript redisScript = RedisScript.of(UNLOCK_LUA, Long.class);
        Object exeResult = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockValue);
        if (exeResult == null) {
            return false;
        }
        return NumberUtils.LONG_ONE.equals(exeResult);
    }

    /**
     * 自增
     */
    public long incr(String key, long delta) {
//        if (delta < 0) {
//            throw new RuntimeException("递增因子必须大于0");
//        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 判断key是否存在
     *
     * @param keyStr
     * @return
     */
    public boolean hasKey(Object keyStr) {
        return redisTemplate.hasKey(keyStr);
    }

    public Boolean updateAlarmStatus(String alarmStatuKey, int stat) {
        String luaStr = new StringBuffer()
                .append("local alarmStatStr = string.gsub(redis.call('get',KEYS[1]),'\"','',2) ")
                .append("local alarmStat = tonumber(alarmStatStr) ")
                .append("if alarmStat <= 0 ")
                .append("then ")
                .append("    if tonumber(ARGV[1]) == 1")
                .append("    then ")
                .append("        redis.call('set',KEYS[1],'\"' .. 1 .. '\"') ")
                .append("        return true ")
                .append("    elseif tonumber(ARGV[1]) == -1 ")
                .append("    then ")
                .append("        redis.call('set',KEYS[1],'\"' .. 0 .. '\"') ")
                .append("        return true ")
                .append("    else ")
                .append("        return false")
                .append("    end ")
                .append("elseif alarmStat > 0 ")
                .append("then ")
                .append("    if tonumber(ARGV[1]) == 1 ")
                .append("    then ")
                .append("        redis.call('set',KEYS[1],'\"' .. (alarmStat + 1) .. '\"') ")
                .append("        return true ")
                .append("    elseif tonumber(ARGV[1]) == -1 ")
                .append("    then ")
                .append("        redis.call('set',KEYS[1],'\"' .. (alarmStat - 1) .. '\"') ")
                .append("        return true ")
                .append("    else ")
                .append("        return false ")
                .append("    end ")
                .append("else ")
                .append("    return false ")
                .append("end ")
                .toString();
        Object execute = redisTemplate.execute(new DefaultRedisScript(luaStr, Boolean.class),
                Collections.singletonList(alarmStatuKey),
                stat);
        if (null != execute) {
            return (Boolean) execute;
        } else {
            return false;
        }
    }
}
