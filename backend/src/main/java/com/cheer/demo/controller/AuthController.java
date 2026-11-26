package com.cheer.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheer.demo.common.Result;
import com.cheer.demo.dto.LoginDTO;
import com.cheer.demo.dto.LoginResultVO;
import com.cheer.demo.dto.RegisterDTO;
import com.cheer.demo.service.AuthService;

import jakarta.validation.Valid;


@RestController 
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResultVO> login(@Valid @RequestBody LoginDTO dto){
        return Result.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public Result<LoginResultVO> register(@Valid @RequestBody RegisterDTO dto){
        return  Result.ok(authService.register(dto));
    }
    
}
