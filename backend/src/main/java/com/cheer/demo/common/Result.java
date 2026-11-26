package com.cheer.demo.common;

/**
 * 全局统一响应包装类（对应前端 types/task.ts 中的 ApiResult<T>）
 *
 * 为什么要用泛型 <T>？
 * 因为所有接口外层结构都是 { code, message, data }，唯一不同的是 data 的具体类型 T：
 * - 查询统计时，T 是 DashboardStatsVO
 * - 查询单条任务时，T 是 CheerTask
 * - 删除任务无需返回数据时，T 是 Void
 */
public class Result<T> {

    /** 业务状态码：200 表示成功，400 表示参数或业务错误，404 表示未找到，500 表示服务器异常 */
    private Integer code;

    /** 提示信息：成功时为 "success"，失败时为具体的中文错误原因 */
    private String message;

    /** 实际返回给前端的核心业务载荷数据（泛型 T） */
    private T data;

    /**
     * 静态工厂方法：构造【带数据】的成功响应
     * static 表示无需 new Result() 即可直接通过 Result.ok(data) 调用
     */
    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    /**
     * 静态工厂方法：构造【无数据】的成功响应（常用于删除接口）
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 静态工厂方法：构造失败响应（常用于 GlobalExceptionHandler 异常拦截器）
     * @param code    错误码（如 400、404、500）
     * @param message 具体错误说明（前端 Axios 拦截器会读取该字段弹出 ElMessage.error）
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    // ==================== 标准 Getter / Setter 方法 ====================
    // （Jackson 转 JSON 以及业务代码读写私有属性时均通过这些方法）

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
