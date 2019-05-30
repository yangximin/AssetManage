package com.yang.assetmanage.entity;

import java.io.Serializable;

/**
 * Created by YXM
 * on 2018/6/7.
 */

public class SelectionDate extends BaseEntity implements Serializable {

    private String name;

    private int date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
