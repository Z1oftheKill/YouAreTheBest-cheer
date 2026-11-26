package com.cheer.demo.common;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常统一拦截处理器（对应 Express/Koa 的全局错误处理中间件，或前端的集中错误捕获）
 *
 * @RestControllerAdvice 注解的作用：
 *                       自动包裹全项目所有的 @RestController 接口。只要任何 Controller 或 Service
 *                       抛出了异常，
 *                       都会自动跳进这个类里寻找匹配的 @ExceptionHandler 方法，并把返回值转成 JSON
 *                       响应给前端。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 专门捕获我们在 Service 层主动抛出的 BusinessException（业务异常）
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException ex) {
        // 取出异常里的错误码和提示语，包装成 Result 对象返回给前端
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 2. 专门捕获 Controller 入参 @Valid 校验失败抛出的 MethodArgumentNotValidException
     * （例如前端传了空标题，触发了 DTO 上的 @NotBlank 校验）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException ex) {
        // 使用 Java 8 Stream 流（等价于 JS 数组的 .map().join("；")）把所有字段的校验失败原因拼接成一句话
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        // 返回 400 状态码及拼接好的中文校验提示
        return Result.fail(400, msg);
    }

    /**
     * 3. 兜底捕获所有未预料到的其他系统异常（Exception 是所有异常的父类）
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnknown(Exception ex) {
        // 在后端控制台打印完整报错堆栈，方便开发者排查 Bug
        ex.printStackTrace();
        // 向前端返回友好的 500 错误 JSON，防止直接暴露生硬的 Tomcat 默认报错页
        return Result.fail(500, "服务端处理异常：" + ex.getMessage());
    }
}
