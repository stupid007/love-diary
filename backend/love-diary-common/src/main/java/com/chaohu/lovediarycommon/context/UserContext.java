package com.chaohu.lovediarycommon.context;
// 当前登录用户上下文，单次请求使用ThreadLocal传递身份
public class UserContext {
    // 用户id
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    // 用户名字
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    public static void set(Long userId,String userName) {
        USER_ID.set(userId);
        USERNAME.set(userName);
    }
    // 从当前线程获取用户id
    public static Long getUserId() {
        return USER_ID.get();
    }
    // 从当前线程获取用户名
    public static String getUserName() {
        return USERNAME.get();
    }
    // 清理当前线程内容，避免内存泄露
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
    }

}
