package com.lutw.common.core.response.wechat;

import lombok.Data;

import java.util.Map;

/**
 * @Description TemplateMessageVo
 * @Author Lutw
 * @Date 2021/10/8 9:43
 * @Version 1.0
 */

@Data
public class WeChatTemplateMessage {

    /**
     * 接收者openid
     */
    private String touser;
    /**
     * 模板id
     */
    private String template_id;
    /**
     * 点击模板卡片后的跳转页面
     */
    private String page;
    /**
     * 跳转小程序类型
     */
    private String miniprogram_state;
    /**
     * 语言
     */
    private String lang = "zh_CN";
    /**
     * 模板数据
     */
    private Map<String, AppletTemplateData> data;
}
