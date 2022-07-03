package com.lutw.netty.controller;

import com.alibaba.fastjson.JSONObject;
import com.lutw.common.core.bean.TargetDeviceProtocol;
import com.lutw.common.core.constants.WeChatConstants;
import com.lutw.common.core.http.OkHttpClientUtil;
import com.lutw.common.core.response.resp.ResponseMsg;
import com.lutw.common.core.response.resp.ResponseResult;
import com.lutw.common.core.response.wechat.AppletTemplateData;
import com.lutw.common.core.response.wechat.WeChatAccessTokenResponse;
import com.lutw.common.core.response.wechat.WeChatAppletSessionResponse;
import com.lutw.common.core.response.wechat.WeChatTemplateMessage;
import com.lutw.common.core.utils.FormatTransfer;
import com.lutw.common.redis.service.RedisService;
import com.lutw.netty.client.SocketClient;
import com.lutw.netty.session.ClientSession;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 小程序
 * @Author: ltw
 * @Date: 2022/6/16 10:31
 */
@Slf4j
@RestController
@RequestMapping("/weChatApplet")
public class WeChatAppletController {

    private static final String AppID = "";
    private static final String AppSecret = "";
    private static final String WE_APPLET_ACCESS_TOKEN = "we_applet_access_token";

    @Autowired
    private RedisService redisService;

    @Autowired
    private OkHttpClientUtil clientUtil;

    /**
     * @description: 获取用户 Session
     * @param code : 换取code
     * @return: void
     * @author: ltw
     * @date: 2022/6/16 12:33
     */
    @GetMapping("/getAppletSession")
    public ResponseResult<WeChatAppletSessionResponse> getAppletSession(String code){
        return ResponseMsg.success(getAppletOpenId(code));
    }

    @GetMapping("/sendSubscribeMessage")
    public ResponseResult<Boolean> sendSubscribeMessage(){
        Map<String, AppletTemplateData> map = new HashMap<>();
        map.put("character_string14", new AppletTemplateData("10002"));
        map.put("thing1", new AppletTemplateData("业务交流"));
        map.put("time2", new AppletTemplateData("2019年10月1日 15:01"));
        map.put("phone_number12", new AppletTemplateData("15226060000"));
        map.put("thing3", new AppletTemplateData("戴口罩"));

        WeChatTemplateMessage message = new WeChatTemplateMessage();
        //WeChatAppletSessionResponse result = getAppletOpenId(code);
        message.setTouser("o7dq45ZTcdr1eSHZcaa4w1TMn8m4");
        message.setTemplate_id("105AsQH_ZjXHnF2md5wLlVwiGERsVvRk5b_CAnGlEhU");
        message.setMiniprogram_state("developer");
        message.setData(map);
        sendPublicTempMessage(message);
        return ResponseMsg.success();
    }

    private WeChatAppletSessionResponse getAppletOpenId(String code){
        String url = WeChatConstants.API_URL + WeChatConstants.WE_APPLETS_SESSION;
        Map<String, String> map = new HashMap<>();
        map.put("appid", AppID);
        map.put("secret", AppSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String resp = clientUtil.doGet(url, map);
        System.out.println(resp);
        return JSONObject.parseObject(resp, WeChatAppletSessionResponse.class);
    }

    private boolean sendPublicTempMessage(WeChatTemplateMessage weChatTemplateMessage) {
        try {
            log.info("发送的模板消息：" + weChatTemplateMessage);
            String url = WeChatConstants.API_URL + WeChatConstants.WE_APPLET_SEND + "?access_token=" + getAccessToken();;
            String s = clientUtil.doPostJson(url, JSONObject.toJSONString(weChatTemplateMessage));
            System.out.println("发送的模板消息:" +s );
            return true;
        } catch (Exception e) {
            log.error("发送模板消息失败！" + weChatTemplateMessage + "--错误信息：--" + e.getMessage());
            return false;
        }
    }

    /**
     * @description: 获取 微信小程序 AccessToken
     * @return: java.lang.String
     * @author: ltw
     * @date: 2022/6/16 12:24
     */
    private String getAccessToken(){
        //获取微信AccessToken地址
        /*String accessToken = redisService.getCacheObject(WE_APPLET_ACCESS_TOKEN);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }*/
        String url = WeChatConstants.API_URL + WeChatConstants.WE_APPLET_ACCESS_TOKEN;
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credential");
        map.put("appid", AppID);
        map.put("secret", AppSecret);
        String doGet = clientUtil.doGet(url, map);
        WeChatAccessTokenResponse response = JSONObject.parseObject(doGet, WeChatAccessTokenResponse.class);
        /*accessToken = response.getAccessToken();
        if (StringUtils.isEmpty(response.getAccessToken())) {
            log.error("获取token响应信息失败:{}", JSONObject.toJSONString(response));
            return response.getAccessToken();
        }

        long expires_in = response.getExpiresIn();
        log.info("微信token:" + accessToken);
        // 将获取的token保存在redis中
        redisService.setCacheObject(WE_APPLET_ACCESS_TOKEN, accessToken, expires_in - 900, TimeUnit.SECONDS);*/
        return response.getAccessToken();
    }

    @GetMapping("/createSession")
    public String createSession(){
        SocketClient socketClient = new SocketClient();
        socketClient.startConnect();
        socketClient.waitCommandThread();
        ClientSession session = socketClient.getSession();
        Channel channel = session.getChannel();

        byte pr = 2;
        short le = 6;
        char sn = (char)0;
        short id = 1;
        byte ch = 1;
        byte[] deviceId = FormatTransfer.short2ByteNew(id);
        TargetDeviceProtocol protocol = new TargetDeviceProtocol(pr,le,sn,deviceId);
        protocol.setCheckNum(ch);
        //ctx.channel().attr(AttributeKey.newInstance());
        channel.writeAndFlush(protocol);
        System.out.println("1111111111111");

        return "1111";

    }
}
