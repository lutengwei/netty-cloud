package com.lutw.mybatisplus.factory;

//import feign.hystrix.FallbackFactory;


/**
 * 用户服务降级处理
 *
 * @author ruoyi
 */
/*public class RemoteSysUserFallbackFactory implements FallbackFactory<RemoteSysUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSysUserFallbackFactory.class);

    @Override
    public RemoteSysUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteSysUserService() {

            @Override
            public ResponseResult<IPage<SysUser>> queryList(SysUserDTO sysUserDTO) {
                return ResponseMsg.error(null, "系统调用异常");
            }
        };
    }
}*/
