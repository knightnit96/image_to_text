package com.example.admin.nav.model;
public class ReceiveImageData{
    private Long id;
    private String name;
    private String body;

    public ReceiveImageData(){
    }

    public ReceiveImageData(Long id, String name, String body) {
        this.id = id;
        this.name = name;
        this.body = body;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}