package com.yang.assetmanage.entity;

/**
 * Created by yangximin on 2019/5/29.
 */

public class Bill extends BaseEntity{
    private String id;

    private String billName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }
}
