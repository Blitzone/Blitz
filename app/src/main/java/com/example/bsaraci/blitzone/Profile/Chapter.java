package com.example.bsaraci.blitzone.Profile;

public class Chapter  {
    private Integer id;
    private String name;

    public Chapter(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {this.id = id;}

    public void setName(String name) {
        this.name = name;
    }
}
