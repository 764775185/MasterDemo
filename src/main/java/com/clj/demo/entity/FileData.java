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
@Table(name = "file_data")
public class FileData implements Serializable {

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
    @Column(name = "keyword_vector")
    private String keywordVector;

    @NotNull
    @Column(name = "timestamp")
    private String timestamp;

    @NotNull
    @Column(name = "authorize_role")
    private String authorizeRole;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    public FileData(@NotNull String title, @NotNull String content, @NotNull String cipher, @NotNull String keywordVector, @NotNull String timestamp, @NotNull String authorizeRole, @NotNull User user) {
        this.title = title;
        this.content = content;
        this.cipher = cipher;
        this.keywordVector = keywordVector;
        this.timestamp = timestamp;
        this.authorizeRole = authorizeRole;
        this.user = user;
    }
}