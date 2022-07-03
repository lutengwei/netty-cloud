package com.lutw.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lutw.common.core.utils.ExcelUtil;
import com.lutw.mybatisplus.dto.SysUserDTO;
import com.lutw.mybatisplus.entity.SysUser;
import com.lutw.mybatisplus.mapper.SysUserMapper;
import com.lutw.mybatisplus.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *   @description : SysUser 服务实现类
 *   ---------------------------------
 * 	 @author Administrator
 *   @since 2022-06-27
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public boolean add(SysUser sysUser) {
        return save(sysUser);
    }

    @Override
    public void export(SysUserDTO sysUserDTO, HttpServletResponse response) {
        List<SysUser> list = list();
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        try {
            util.exportExcel(response, list, "用户信息");
        } catch (IOException e) {
            log.error("用户信息导出失败",e.getMessage());
        }
    }

    @Override
    public Page<SysUser> queryList(SysUserDTO sysUserDTO) {

        int pageNum = sysUserDTO.getPageNum();
        int pageSize = sysUserDTO.getPageSize();

        Page<SysUser> page = new Page<>(pageNum, pageSize);
        List<SysUser> list = list();
        page.setRecords(list);
        return page;
    }
}
