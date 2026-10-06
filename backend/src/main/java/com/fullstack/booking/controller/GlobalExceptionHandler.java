package com.fullstack.booking.controller;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 统一把控制器抛出的异常转成 4xx JSON 响应，避免非法入参变成 500。
 * 响应体统一为 {"message": "..."}。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 参数缺失、格式错误、状态流转非法等。 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(message(e));
    }

    /** 请求体不是合法 JSON，或字段类型无法转换。 */
    @ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Map<String, String>> handleUnreadableRequest(Exception e) {
        return ResponseEntity.badRequest().body(message("请求参数格式不正确"));
    }

    /** 引用的资源不存在。 */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message(e));
    }

    /** 悲观锁等待超时等并发冲突。 */
    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<Map<String, String>> handleConflict(ConcurrencyFailureException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message("该资源正在被其他请求占用，请稍后重试"));
    }

    /** 违反数据库约束，例如同一服务的同一开始时间已存在时间段。 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message("请求与已有数据冲突，请检查后重试"));
    }

    private static Map<String, String> message(Exception e) {
        return message(e.getMessage());
    }

    private static Map<String, String> message(String text) {
        if (text == null || text.trim().isEmpty()) {
            text = "请求处理失败";
        }
        return Collections.singletonMap("message", text);
    }
}
