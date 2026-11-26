package com.cheer.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域（CORS）配置类
 *
 * 知识点说明：
 * 虽然我们在开发环境下已经在前端 vite.config.ts 配了 proxy 代理转发，
 * 但如果前端不走代理、直接请求 http://localhost:8080/api/tasks，浏览器就会触发同源策略跨域拦截。
 * 实现 WebMvcConfigurer 接口并重写 addCorsMappings，即可在后端层面允许前端跨域调用。
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 注册跨域规则（@Override 表示重写父接口 WebMvcConfigurer 定义的方法）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 对所有 /api/ 开头的接口生效
                .allowedOriginPatterns("*") // 允许任意前端域名/端口访问
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 允许的 HTTP 请求方法
                .allowedHeaders("*") // 允许前端携带任意请求头（如 Content-Type、Authorization）
                .allowCredentials(true); // 允许携带 Cookie 凭证
    }
}
