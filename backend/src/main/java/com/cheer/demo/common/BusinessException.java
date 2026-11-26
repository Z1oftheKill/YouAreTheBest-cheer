package com.cheer.demo.common;

/**
 * 自定义业务异常类（继承自 Java 标准运行时异常 RuntimeException）
 *
 * 作用：
 * 当 Service 层遇到业务不合法（比如查不到指定 ID 的任务、未勾选要删除的数据）时，
 * 直接写 throw new BusinessException("提示文案") 即可中断流程，
 * 并由 GlobalExceptionHandler 自动捕获后包装成标准 JSON 返回给前端。
 */
public class BusinessException extends RuntimeException {

    /** 自定义业务错误码（使用 final 修饰表示创建后不可修改） */
    private final Integer code;

    /**
     * 单参数构造方法：默认使用 400 作为业务错误码
     * 
     * @param message 错误原因提示
     */
    public BusinessException(String message) {
        // 调用父类 RuntimeException 的构造方法保存 message
        super(message);
        this.code = 400;
    }

    /**
     * 双参数构造方法：支持指定特殊错误码（例如 404 未找到、401 未登录）
     * 
     * @param code    错误状态码
     * @param message 错误原因提示
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /** 获取业务错误码（供 GlobalExceptionHandler 调用） */
    public Integer getCode() {
        return code;
    }

}
