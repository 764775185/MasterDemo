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
@Table(name = "keyword")
public class Keyword implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "word")
    private String word;

    @NotNull
    @Column(name = "sum")
    private Integer sum;

    public Keyword(@NotNull String word,  @NotNull Integer sum) {
        this.word = word;
        this.sum = sum;
    }
}