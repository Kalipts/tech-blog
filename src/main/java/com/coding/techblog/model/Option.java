package com.coding.techblog.model;

import java.io.Serializable;

public class Option implements Serializable {

    private String name;

    private String value;

    private String description;

    public Option(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
