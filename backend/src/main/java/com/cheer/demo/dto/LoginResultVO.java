package com.cheer.demo.dto;

/**
 * 登录/注册成功后的返回结果 VO（对应前端 LoginResult：{ token, user: { id, username, nickname } }）
 * 注意：绝对不把 passwordHash 返回给前端！
 */
public class LoginResultVO {

    private String token;
    private UserBrief user;

    public LoginResultVO() {
    }

    public LoginResultVO(String token, Long id, String username, String nickname) {
        this.token = token;
        this.user = new UserBrief(id, username, nickname);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBrief getUser() {
        return user;
    }

    public void setUser(UserBrief user) {
        this.user = user;
    }

    /** 脱敏后的用户基本信息内部类 */
    public static class UserBrief {
        private Long id;
        private String username;
        private String nickname;

        public UserBrief() {
        }

        public UserBrief(Long id, String username, String nickname) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}

