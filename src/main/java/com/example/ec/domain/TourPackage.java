package com.example.ec.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TourPackage {
    @Id
    private String code;
    private String name;

    protected TourPackage() {
    }

    public TourPackage(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
