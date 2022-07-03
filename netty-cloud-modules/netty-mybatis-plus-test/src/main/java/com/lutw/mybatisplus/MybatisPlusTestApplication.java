package com.lutw.mybatisplus;

import com.lutw.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableCustomSwagger2
/**
 * @SpringCloudApplication相当于以下三个：
 * @SpringBootApplication   //SpringBoot注解
 * @EnableDiscoveryClient   //注册服务中心Eureka注解
 * @EnableCircuitBreaker    //断路器注解
 */
@SpringBootApplication
public class MybatisPlusTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusTestApplication.class,args);
        System.out.println("(♥◠‿◠)ﾉﾞ  MybatisPlusTest模块启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
