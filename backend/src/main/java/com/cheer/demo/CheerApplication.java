// package 声明当前类所在的目录包路径（对应文件路径 com/cheer/demo）
package com.cheer.demo;

// import 引入外部依赖包中的类（完全对应前端 ES6 的 import ... from '...'）
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后端服务总启动类（对应前端的 src/main.ts）
 *
 * 1. @SpringBootApplication 是核心组合注解，干了三件事：
 *    - 声明这是一个配置类
 *    - 开启自动装配（根据 pom.xml 引入的依赖，自动启动内置 Tomcat 和连接 MySQL）
 *    - 自动扫描当前包（com.cheer.demo）及其子包下所有带 @RestController/@Service/@Configuration 的类
 *
 * 2. @MapperScan("com.cheer.demo.mapper")：
 *    告诉 MyBatis-Plus 去 mapper 文件夹下扫描所有的数据库接口，自动为它们生成实现类对象
 */
@SpringBootApplication
@MapperScan("com.cheer.demo.mapper")
public class CheerApplication {

    /**
     * Java 程序的标准入口方法（固定写法 public static void main）
     * @param args 命令行启动时传入的参数数组
     */
    public static void main(String[] args) {
        // 启动 Spring 容器与内置 Tomcat 服务器（监听 application.yml 中配置的 8080 端口）
        SpringApplication.run(CheerApplication.class, args);
    }
}
