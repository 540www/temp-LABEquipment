package com.example.equipment.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDATION_ERROR");
        StringBuilder msg = new StringBuilder("验证失败: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            msg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        });
        body.put("message", msg.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({BindException.class, IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "BAD_REQUEST");
        String msg = ex.getMessage();
        if (ex.getCause() != null) {
            msg += " (" + ex.getCause().getMessage() + ")";
        }
        body.put("message", msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "DATA_INTEGRITY_ERROR");
        String msg = ex.getMessage();
        if (msg.contains("foreign key")) {
            msg = "外键约束失败：请确保类别ID和实验室ID存在";
        } else if (msg.contains("Duplicate entry")) {
            msg = "数据重复：该记录已存在";
        }
        body.put("message", msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOther(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "INTERNAL_ERROR");
        body.put("message", ex.getMessage());
        if (ex.getCause() != null) {
            body.put("cause", ex.getCause().getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}


