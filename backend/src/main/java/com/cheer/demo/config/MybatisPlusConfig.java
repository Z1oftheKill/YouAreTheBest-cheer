package com.cheer.demo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 框架配置类（对应前端在 main.ts 中 app.use(...) 注册插件）
 *
 * @Configuration 告诉 Spring：这是一个配置类，启动时请执行里面的 @Bean 方法
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @Bean 注解的作用：
     * 把这个方法 return 出来的 MybatisPlusInterceptor 对象交给 Spring 容器全局托管。
     *
     * 为什么要配这个拦截器？
     * 因为如果不配它，调用 taskMapper.selectPage(...) 时只会查全表（内存分页）；
     * 配上 PaginationInnerInterceptor(DbType.MYSQL) 后，它会自动拦截分页查询：
     * 1. 先自动发一条 SELECT COUNT(*) 查总条数 total
     * 2. 再自动在原 SQL 末尾拼接上 MySQL 的 LIMIT offset, size 实现真正的物理分页！
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建 MyBatis-Plus 主插件容器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 往容器中添加针对 MySQL 数据库语法的分页内部拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
