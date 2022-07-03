package com.lutw.mybatisplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lutw.common.core.response.resp.ResponseMsg;
import com.lutw.common.core.response.resp.ResponseResult;
import com.lutw.mybatisplus.dto.SysUserDTO;
import com.lutw.mybatisplus.entity.SysUser;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.lutw.mybatisplus.service.SysUserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 *   @description : SysUser 控制器
 *   ---------------------------------
 * 	 @author Administrator
 *   @since 2022-06-27
 */
@RestController
@Api(tags="SysUser 控制器")
@RequestMapping("/sysUser")
public class SysUserController {

    private final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    public SysUserService sysUserService;

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    public ResponseResult<String> add(@RequestBody SysUser sysUser){
        return ResponseMsg.success(sysUserService.add(sysUser));
    }

    @ApiOperation(value = "导出")
    @PostMapping(value = "/export")
    public void export(SysUserDTO sysUserDTO, HttpServletResponse response){
        sysUserService.export(sysUserDTO,response);
    }


    @ApiOperation("查询列表")
    @PostMapping("/queryList")
    public ResponseResult<Page<SysUser>> queryList(@RequestBody SysUserDTO sysUserDTO) {
        Page<SysUser> visitorRecordVOIPage = sysUserService.queryList(sysUserDTO);
        return ResponseMsg.success(visitorRecordVOIPage);
    }

}
