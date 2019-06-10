package com.yang.assetmanage.utils;

import android.text.TextUtils;

import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Bill;
import com.yang.assetmanage.entity.Dicts;
import com.yang.assetmanage.entity.SelectionDate;
import com.yang.assetmanage.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期选择
 * Created by YXM
 * on 2018/6/7.
 */

public class GenerateDateUtils {

    private static GenerateDateUtils sInstance;

    public static GenerateDateUtils getInstance() {
        if (sInstance == null) {
            synchronized (GenerateDateUtils.class) {
                if (sInstance == null) {
                    sInstance = new GenerateDateUtils();
                }
            }
        }
        return sInstance;
    }

    public List<SelectionDate> getYearList() {
        List<SelectionDate> selectionDates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int endYear = c.get(Calendar.YEAR);
        int startYear = 1950;
        for (int i = endYear; i >= startYear; i--) {
            selectionDates.add(createDict(i, i + "年"));
        }
        return selectionDates;
    }


    public List<SelectionDate> getMoreYearList() {
        List<SelectionDate> selectionDates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 1);
        int endYear = c.get(Calendar.YEAR);
        int startYear = 1950;
        for (int i = endYear; i >= startYear; i--) {
            selectionDates.add(createDict(i, i + "年"));
        }
        return selectionDates;
    }


    public List<SelectionDate> getMonthList(int year) {
        int month = 12;
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        if (currentYear == year) {
            month = c.get(Calendar.MONTH) + 1;
        }
        List<SelectionDate> selectionDates = new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            selectionDates.add(createDict(i, i + "月"));
        }
        return selectionDates;
    }


    public List<SelectionDate> getMoreMonthList() {
        int month = 12;
        List<SelectionDate> selectionDates = new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            selectionDates.add(createDict(i, i + "月"));
        }
        return selectionDates;
    }


    public List<SelectionDate> getDayForMothList(int year, int month) {
        List<SelectionDate> selectionDates = new ArrayList<>();
        int lastDay;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);

        //获取一个月中最好一天
        for (int i = 1; i <= lastDay; i++) {
            selectionDates.add(createDict(i, i + "日"));
        }
        return selectionDates;
    }

    public List<SelectionDate> getDayForMoth(int year, int month) {
        List<SelectionDate> selectionDates = new ArrayList<>();
        int lastDay;
        int currentMonth;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        if (currentYear == year && currentMonth == month) {
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        }
        //获取一个月中最好一天
        for (int i = 1; i <= lastDay; i++) {
            selectionDates.add(createDict(i, i + "日"));
        }
        return selectionDates;
    }

    public String getCurrentTime() {
        long millis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(millis));
    }

    public List<Dicts> getMemberDicts() {
        return DbUtils.getInstance().getDictList("1");
    }

    /**
     * 食品酒水：早午晚餐、食材、水果、零食、烟酒茶饮
     * 衣服饰品：衣服裤子、鞋帽包包、化妆饰品
     * 行车交通：公共交通、打车出租、私家车费用、加油、修车、汽车保险、高速路费、违章罚款、停车费
     * 居家物业：日常用品、房租、水电煤气、物业管理、维修保养
     * 交流通讯：座机费、手机费、上网费、邮寄费
     * 休闲娱乐：运动健身、腐败聚会、休闲玩乐、旅游度假、宠物宝贝
     * 学习进修：书报杂志、培训进修、数码设备
     * 人情往来：送礼请客、孝敬家长、还人钱物、慈善捐助、红白喜事
     * 医疗保险：药品费、保健费、美容费、治疗费
     * 金融保险：银行手续、投资亏损、按揭还款、消费税收、利息支出、赔偿罚款、基金申购
     * 其他杂项：其他支出、意外丢失、烂账损失
     */
    public List<Dicts> getExpendType() {
        return DbUtils.getInstance().getDictList("2");
    }
    public List<Dicts> getIncomeType() {
        return DbUtils.getInstance().getDictList("3");
    }

    public List<Dicts> getBillList() {
        List<Dicts> dicts = new ArrayList<>();
        Object obj = SPUtil.getObjData(MyApplication.getInstance(), Constants.Sp.SP_KEY_USER_INFO);
        List<Bill> bills = DbUtils.getInstance().selectBill(obj == null ? "" : ((User) obj).getId());
        for (Bill bill : bills) {
            dicts.add(new Dicts(bill.getId(), bill.getBillName()));
        }
        return dicts;
    }


    private static SelectionDate createDict(int date, String name) {
        SelectionDate selectionDate = new SelectionDate();
        selectionDate.setDate(date);
        selectionDate.setName(name);
        return selectionDate;
    }

    public static String formatData(String data) {
        if (TextUtils.isEmpty(data)) {
            return data;
        }
        if (data.length() == 1) {
            return "0" + data;
        }
        return data;
    }
}
