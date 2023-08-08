package com.example.springboot.domain;

import javax.persistence.*;

@Entity
public class KeyWords {

    @Id
    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "count")
    private int count;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}