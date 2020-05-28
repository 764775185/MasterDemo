package com.clj.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number")
    @NotNull
    private String number;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "role")
    @NotNull
    private String role;

    @Column(name = "pkey")
    @NotNull
    private String pkey;

    @Column(name = "attr")
    @NotNull
    private String attr;

    public User(@NotNull String number, @NotNull String username, @NotNull @Email String email, @NotNull String password, @NotNull String role, @NotNull String pkey) {
        this.number = number;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.pkey = pkey;
        this.attr = "role:"+role;
    }

    @Override
    public String toString() {
        return "User{" +
                "number='" + number + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", pkey='" + pkey + '\'' +
                '}';
    }
}
