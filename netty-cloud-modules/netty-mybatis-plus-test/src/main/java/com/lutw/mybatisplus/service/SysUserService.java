package com.lutw.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lutw.mybatisplus.dto.SysUserDTO;
import com.lutw.mybatisplus.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**　
 *   @description : SysUser 服务接口
 *   ---------------------------------
 * 	 @author Administrator
 *   @since 2022-06-27
 */
public interface SysUserService extends IService<SysUser> {

    boolean add(SysUser sysUser);

    void export(SysUserDTO sysUserDTO, HttpServletResponse response);

    Page<SysUser> queryList(SysUserDTO sysUserDTO);
}
