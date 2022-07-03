package com.lutw.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import com.lutw.common.core.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;


/**
 *   @description : SysUser 实体类
 *   ---------------------------------
 * 	 @author Administrator
 *   @since 2022-06-27
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser  implements Serializable {

    /**
     * 主键id
     */
	@Excel(name = "Id")
	@ApiModelProperty("主键id")
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 用户名
     */
	@Excel(name = "用户名")
	@ApiModelProperty("用户名")
	private String username;
    /**
     * 密码
     */
	@Excel(name = "密码")
	@ApiModelProperty("密码")
	private String password;



}
