一、介绍
    1、Netty-Cloud 是一款基于代码生成器的低代码平台！后端框架使用SpringBoot2.6x，SpringCloud2022.1，Netty，Mybatis-plus，支持微服务。
    强大的代码生成器通过配置数据库(目前只支持MYSQL)，让后端代码一键生成，实现低代码开发! Netty-Cloud 引领新的低代码开发模式
    帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省研发成本，同时又不失灵活性！

    2、Netty-Cloud 提供了一系列案例模块，服务包含：公共模块、Feign接口、TCP服务端/客户端、服务熔断/降级/限流等！

    3、Netty-Cloud宗旨是: 快速搭建微服务开发；实现低配置搭建框架，既保证了智能又兼顾灵活；解决了普遍不灵活的弊端！
二、技术架构
   基础环境：
        1、语言：Java 8+ (小于17)
        2、IDE(JAVA)： IDEA (必须安装lombok插件 )
        3、依赖管理：Maven
        4、缓存：Redis
        5、数据库：MySQL5.7
   技术栈：
        1、基础框架：Spring Boot 2.6.6
        2、微服务框架： Spring Cloud Alibaba 2021.1
        3、持久层框架：MybatisPlus 3.5.1
        4、微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel
        5、数据库连接池：阿里巴巴Druid 1.1.22
        6、日志打印：logback
        7、其他：Netty、 fastjson、Swagger-ui、lombok（简化代码）等。

三、微服务解决方案
    1、服务注册和发现 Nacos √
    2、统一配置中心 Nacos √
    3、路由网关 gateway(三种加载方式)
    4、分布式 http feign √
    5、熔断降级限流 Sentinel √
    6、分布式文件 Minio、阿里OSS
    7、统一权限控制 JWT + Shiro
    8、服务监控 SpringBootAdmin
    9、链路跟踪 Skywalking 参考文档
    10、消息中间件 RabbitMQ
    11、分布式事务 Seata
    12、CAS 单点登录
    13、路由限流

Spring Cloud Alibaba 查看版本选择地址：https://github.com/alibaba/spring-cloud-alibaba/wiki