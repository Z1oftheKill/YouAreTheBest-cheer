package com.cheer.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheer.demo.common.BusinessException;
import com.cheer.demo.common.JwtUtils;
import com.cheer.demo.dto.LoginDTO;
import com.cheer.demo.dto.LoginResultVO;
import com.cheer.demo.dto.RegisterDTO;
import com.cheer.demo.entity.SysUser;
import com.cheer.demo.mapper.SysUserMapper;
import com.cheer.demo.service.AuthService;

@Service
@SuppressWarnings("null")
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;

    public AuthServiceImpl(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Override 
    public LoginResultVO login(LoginDTO dto ){

       SysUser user = userMapper.selectOne(
        new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername().trim())
        );
       
       if(user == null){
        throw new BusinessException("账号或者密码错误");
       }

       String inputHash = JwtUtils.hashPassword(dto.getPassword());
       
       if(!inputHash.equals(user.getPasswordHash())){
        throw new BusinessException("密码错误");
       }

       String token = JwtUtils.generateToken(user.getId(), user.getUsername());

       return new LoginResultVO(token, user.getId(), user.getUsername(), user.getNickname());
    }

    @Override 
    public LoginResultVO register(RegisterDTO dto){
        String username = dto.getUsername().trim();

        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUsername, username)
        );

        if(count > 0){
            throw new BusinessException("该账号已注册!");
        }

        SysUser user = new SysUser();

        user.setUsername(username);

        user.setPasswordHash(
            JwtUtils.hashPassword(dto.getPassword())
        );

        user.setNickname(dto.getNickname());

        userMapper.insert(user);

        // 签发token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername());

        return new LoginResultVO(
            token,
            user.getId(),
            user.getUsername(),
            user.getNickname()
        );
    }
}
