package com.example.springboot.domain;

import javax.persistence.*;

@Entity
public class Crawler {

    @Id
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "img", nullable = true)
    private String img;

    @Column(name = "title", nullable = true)
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
