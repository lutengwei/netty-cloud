package com.lutw.common.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 根据GPS星期数、周内秒转换GPS格式时间
     *
     * @param week   星期数
     * @param second 周内秒
     * @return
     * @Auther Lutw
     */
    public static String getGPSTime(int week, double second) {
        // GPS规定差多18秒
        double _second = second- 18;
        int _week = 0;
        //判断是否大于一周
        if ((_second > (7*24*3600)) && (_second < 0)){
            return null;
        }

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        // 天：1天=86400000000000L纳秒
        int day = (int)(_second / 86400);//转换天数;
        _second = _second % 86400;//剩余秒数
        //时：1小时=3600000000000L纳秒；
        int hour = (int) (_second / 3600);//转换小时数
        _second = _second % 3600;//剩余秒数
        // 分：1分=60000000000L纳秒；
        int minute = (int) (_second / 60);//转换分钟
        // 秒：1秒=1000000000纳秒
        int s = (int)(_second % 60);//剩余秒数);
        if (hour + 8 > 24) {
            day = day + 1;
            hour = hour + 8 - 24;
        }else {
            hour = hour + 8;
        }
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        String sStr = String.valueOf(s);
        if (String.valueOf(hour).length() == 1) {
            hourStr = "0" + hourStr;
        }
        if (String.valueOf(minute).length() == 1) {
            minuteStr = "0" + minuteStr;
        }
        if (String.valueOf(s).length() == 1) {
            sStr = "0" + sStr;
        }
        String gpsTime = hourStr + ":" + minuteStr + ":" + sStr;
        Date date = null;
        String dateStr = null;
        try {
            date = f.parse("1980-1-6");//起始日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, _week * 7 + day);
            date = calendar.getTime();
            String str = f.format(date);
            dateStr = str + " " + gpsTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 根据gps周内秒和utc转换成当前日期
     *
     * @param week
     * @return
     */
    public static Date weekSecondUTCToDate(int week, double second, String utc) {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        int d = (int) (second / (double) (60 * 60 * 24));//秒转天

        //将hhmmss转换成hh:mm:ss
        String regex = "(.{2})";
        String Str = utc.replaceAll(regex, "$1:");
        String utcStr = Str.substring(0, Str.length() - 1);
        Date date = null;
        Date date1 = null;
        try {
            date1 = f.parse("1980-1-6");//起始日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, week * 7 + d);
            date = calendar.getTime();
            String str = f.format(date);
            String dateStr = str + " " + utcStr;
            //utc转本地时间
            date = utcToLocal(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * utc时间转本地时间
     *
     * @param utcTime
     * @return
     */
    public static Date utcToLocal(String utcTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = sdf.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getDefault());
        Date localDate = null;
        String localTime = sdf.format(utcDate.getTime());
        try {
            localDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localDate;
    }
}
