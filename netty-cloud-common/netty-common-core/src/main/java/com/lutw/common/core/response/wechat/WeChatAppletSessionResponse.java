package com.lutw.common.core.response.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Description: 获取登录session
 * @Author: ltw
 * @Date: 2022/6/16 12:30
 */
@Data
public class WeChatAppletSessionResponse {

    @JSONField(ordinal = 1, name = "openid")
    private String openId;

    @JSONField(ordinal = 2, name = "session_key")
    private String sessionKey;

    @JSONField(ordinal = 3, name = "unionid")
    private String unionId;

    @JSONField(ordinal = 4, name = "errcode")
    private Integer errCode;

    @JSONField(ordinal = 5, name = "errmsg")
    private String errMsg;
}
