package com.lutw.feign;

import com.lutw.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableCustomSwagger2
/**
 * @SpringCloudApplication相当于以下三个：
 * @SpringBootApplication   //SpringBoot注解
 * @EnableDiscoveryClient   //注册服务中心Eureka注解
 * @EnableCircuitBreaker    //断路器注解
 */
@EnableFeignClients("com.lutw.mybatisplus")
@SpringBootApplication
public class RemoteFeignTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteFeignTestApplication.class,args);
        System.out.println("(♥◠‿◠)ﾉﾞ  RemoteFeignTest模块启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
