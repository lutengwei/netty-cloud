package com.lutw.mybatisplus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lutw.common.core.constants.ServiceNameConstants;
import com.lutw.common.core.response.resp.ResponseResult;
import com.lutw.mybatisplus.dto.SysUserDTO;
import com.lutw.mybatisplus.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: 系统用户远程接口
 * @Author: ltw
 * @Date: 2022/6/27 18:26
 */
@FeignClient(contextId = "remoteSysUserService", value = ServiceNameConstants.NETTY_MYBATIS_PLUS_TEST)
public interface RemoteSysUserService {

    @PostMapping("/sysUser/queryList")
    ResponseResult<Page<SysUser>> queryList(@RequestBody SysUserDTO sysUserDTO);
}
