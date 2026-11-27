package com.cheer.demo.common;

public class UserContext {

    private static final ThreadLocal<Long> USER_HOLDER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_HOLDER.get();
    }

    // 面试必考点：请求结束必须 remove，防止 Tomcat 线程池复用线程时串号或内存泄漏！
    public static void clear() {
        USER_HOLDER.remove();
    }
}