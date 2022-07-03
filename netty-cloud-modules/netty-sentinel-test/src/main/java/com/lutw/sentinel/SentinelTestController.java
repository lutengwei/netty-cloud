package com.lutw.sentinel;

import com.lutw.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 哨兵
 * @Author: ltw
 * @Date: 2022/7/1 11:52
 */
@EnableCustomSwagger2
@SpringBootApplication
public class SentinelTestController {

    public static void main(String[] args) {
        SpringApplication.run(SentinelTestController.class,args);
        System.out.println("(♥◠‿◠)ﾉﾞ  RemoteFeignTest模块启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
