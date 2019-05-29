package com.yang.assetmanage.entity;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class ForgetPwd extends BaseEntity{

    String name;

    String idCard;

    String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
