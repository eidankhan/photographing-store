package com.photographing.store.app.dto;

import com.photographing.store.app.modal.User;

// This class is used to transport data from client to server and vice versa
public class FileDTO {
    private Long id;
    private String name;
    private String url;
    private User user;

    public FileDTO(){

    }

    public FileDTO(String name, String url, User user) {
        this.name = name;
        this.url = url;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
