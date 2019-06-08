package com.yang.assetmanage.entity;

/**
 * Created by yangximin on 2019/5/31.
 */

public class Asset {
    /**
     *  _ID       INTEGER      PRIMARY KEY AUTOINCREMENT,
     " +
     "   BILL_ID       INTEGER       NOT NULL,\n" +
     "   USER_ID       INTEGER      NOT NULL,\n" +
     "   MONEY         TEXT     NOT NULL,\n" +
     "   MONEY_TYPE    INT      NOT NULL,\n" +
     "   CRETE_DATA    TEXT     NOT NULL,\n" +
     "   MEMBER        TEXT     NOT NULL,\n" +
     "   REMARK        TEXT     NOT NULL\n" +
     */
    private String id;
    private String billId;
    private String userId;
    private String money;
    private String moneyType;
    private String moneyName;
    private String creteData;
    private String member;
    private String memberName;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getCreteData() {
        return creteData;
    }

    public void setCreteData(String creteData) {
        this.creteData = creteData;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
