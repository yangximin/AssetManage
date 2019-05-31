package com.yang.assetmanage.entity;

import android.text.TextUtils;

import com.yang.assetmanage.db.DbUtils;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class User extends BaseEntity {

    String name;

    String password;

    String idCard;

    String id;

    long registerDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(long registerDate) {
        this.registerDate = registerDate;
    }

    public String getId() {
        if (TextUtils.isEmpty(id)) {
            User user = DbUtils.getInstance().login(name, password);
            return user.getId();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
