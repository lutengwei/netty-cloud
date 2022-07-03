package com.lutw.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 测试阿里哨兵
 * @Author: ltw
 * @Date: 2022/7/1 12:00
 */
@Api(tags="sentinel 控制器")
@RestController
@RequestMapping("sentinel")
public class TestSentinelController {

    @GetMapping(value = "/hello")
    @SentinelResource("hello")
    public String hello() {
        return "Hello Sentinel";
    }
}
