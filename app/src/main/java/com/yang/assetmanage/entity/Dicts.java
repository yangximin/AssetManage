package com.yang.assetmanage.entity;

/**
 * Created by zhangc on 2018/5/31.
 * 字典表
 */

public class Dicts extends BaseEntity {

    private String id;

    private String name;

    private String type;
    public Dicts() {
    }

    public Dicts(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dicts(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
