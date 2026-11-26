package com.cheer.demo.service;

import com.cheer.demo.dto.LoginDTO;
import com.cheer.demo.dto.LoginResultVO;
import com.cheer.demo.dto.RegisterDTO;

public interface AuthService {

    LoginResultVO login(LoginDTO dto);

    LoginResultVO register(RegisterDTO dto);
    
}