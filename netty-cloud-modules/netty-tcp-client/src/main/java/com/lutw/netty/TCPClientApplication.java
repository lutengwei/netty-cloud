package com.lutw.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.lutw")
public class TCPClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TCPClientApplication.class,args);
    }

}
