package com.lutw.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @SpringCloudApplication相当于以下三个：
 * @SpringBootApplication   //SpringBoot注解
 * @EnableDiscoveryClient   //注册服务中心Eureka注解
 * @EnableCircuitBreaker    //断路器注解
 */
@SpringBootApplication
public class TCPServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TCPServerApplication.class,args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Netty服务端模块启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
