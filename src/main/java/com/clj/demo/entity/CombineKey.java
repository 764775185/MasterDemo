package com.clj.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class CombineKey implements Serializable {
    int id;
    List<String> list;

    public CombineKey(int id, List<String> list) {
        this.id = id;
        this.list = list;
    }

    @Override
    public String toString() {
        return "CombinKey{" +
                "id=" + id +
                ", list=" + list +
                '}';
    }
}
