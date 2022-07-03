package com.lutw.common.core.response.wechat;

import lombok.Data;

/**
 * @Description:
 * @Author: ltw
 * @Date: 2022/6/16 14:24
 */
@Data
public class AppletTemplateData {
    private String value;

    public AppletTemplateData(String value) {
        this.value = value;
    }
}
