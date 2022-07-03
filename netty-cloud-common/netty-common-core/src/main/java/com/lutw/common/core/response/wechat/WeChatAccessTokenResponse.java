package com.lutw.common.core.response.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Description WeChatAccessTokenResponse   微信返回token信息
 * @Author Lutw
 * @Date 2021/9/26 9:01
 * @Version 1.0
 */

@Data
public class WeChatAccessTokenResponse {

    @JSONField(ordinal = 1, name = "access_token")
    private String accessToken;


    @JSONField(ordinal = 2, name = "expires_in")
    private Integer expiresIn;


}
