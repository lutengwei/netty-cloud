package com.lutw.feign.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lutw.common.core.response.resp.ResponseMsg;
import com.lutw.common.core.response.resp.ResponseResult;
import com.lutw.mybatisplus.dto.SysUserDTO;
import com.lutw.mybatisplus.entity.SysUser;
import com.lutw.mybatisplus.service.RemoteSysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *   @description : SysUser 控制器
 *   ---------------------------------
 * 	 @author Administrator
 *   @since 2022-06-27
 */
@RestController
@Api(tags="remoteSysUser 控制器")
@RequestMapping("/remoteSysUser")
public class SysUserController {

    private final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private RemoteSysUserService remoteSysUserService;

    @ApiOperation("查询列表")
    @PostMapping("/queryList")
    public ResponseResult<Page<SysUser>> queryList(@RequestBody SysUserDTO sysUserDTO) {
        ResponseResult<Page<SysUser>> result = remoteSysUserService.queryList(sysUserDTO);
        Page<SysUser> data = result.getData();
        return ResponseMsg.success(data);
    }

}
