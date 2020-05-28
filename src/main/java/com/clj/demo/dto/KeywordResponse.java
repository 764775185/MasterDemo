package com.clj.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordResponse<T> {
    private int code;
    private String message;
    private T data;

    public KeywordResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
