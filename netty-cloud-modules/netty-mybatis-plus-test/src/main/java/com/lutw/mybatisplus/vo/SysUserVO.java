package com.lutw.mybatisplus.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 *   @description : SysUser 实体类
 *   ---------------------------------
 * 	 @author Administrator
 *   @since 2022-06-27
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class SysUserVO implements Serializable {

    /**
     * 主键id
     */
	@ApiModelProperty("主键id")
	private Long id;
    /**
     * 用户名
     */
	@ApiModelProperty("用户名")
	private String username;
    /**
     * 密码
     */
	@ApiModelProperty("密码")
	private String password;



}
