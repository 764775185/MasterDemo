package com.clj.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public UserResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
