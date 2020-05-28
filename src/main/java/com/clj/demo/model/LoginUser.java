package com.clj.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {
    private String email;
    private String password;
    private Boolean rememberMe;
}
