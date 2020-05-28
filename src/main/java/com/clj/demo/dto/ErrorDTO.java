package com.clj.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
    private Integer code;
    private String message;

    public ErrorDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
