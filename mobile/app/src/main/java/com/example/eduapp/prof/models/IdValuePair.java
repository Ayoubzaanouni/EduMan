package com.example.eduapp.prof.models;

public class IdValuePair {
    private int id;
    private String value;

    public IdValuePair(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
