package com.clj.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cpabe_data")
public class CpabeData implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "cipher")
    private String cipher;

    @NotNull
    @Column(name = "timestamp")
    private String timestamp;

    @NotNull
    @Column(name = "policy")
    private String plicy;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    public CpabeData(@NotNull String title, @NotNull String content, @NotNull String cipher, @NotNull String timestamp, @NotNull String policy, @NotNull User user) {
        this.title = title;
        this.content = content;
        this.cipher = cipher;
        this.timestamp = timestamp;
        this.plicy = policy;
        this.user = user;
    }
}