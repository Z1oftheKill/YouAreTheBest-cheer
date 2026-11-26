package com.cheer.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求入参 DTO（对应前端 LoginPayload）
 */
public class LoginDTO {

    @NotBlank(message = "登录账号不能为空")
    private String username;

    @NotBlank(message = "登录密码不能为空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

