package com.lutw.common.core.response.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName ResponseResult
 * @Description  返回数据格式
 * @Author ltw
 * @Date 2021-01-15 10:30
 * @Version V1.0
 */
@Accessors(chain = true)
@Data
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 3728877563912075885L;

    /**
     * http状态码
     */
    private Long code;
    /**
     * 状态码
     */
    private Boolean success;
    /**
     * 提示内容
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;
}
