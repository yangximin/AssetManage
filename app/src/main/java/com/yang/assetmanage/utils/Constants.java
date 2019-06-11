package com.yang.assetmanage.utils;

/**
 * Created by YXM
 * on 2019/5/29.
 */

public class Constants {

    public interface Normal{
        /**
         * 支出类型
         */
        String ASSET_TYPE_EXPEND = "2";

        String TYPE_SELECT_YEAR_MONTH = "type_select_year_month";
        /**
         * 支出
         */
        String TYPE_EXPEND = "2";
        /**
         * 收入
         */
        String TYPE_INCOME = "3";
    }

    public interface Sp {
        String SP_KEY_USER_INFO = "sp_key_user_info";
        String SP_KEY_BILL_ID = "sp_key_bill_id";
    }

    public interface Intent{
        String INTENT_KEY_TYPE = "intent_key_type";
        String INTENT_KEY_BILL_ID = "intent_key_bill_id";
        String INTENT_KEY_YEAR = "intent_key_year";
        String INTENT_KEY_DAY = "intent_key_day";
    }
    public interface  Event{
        String EVENT_ADD_ASSET_SUCCESS = "event_add_asset_success";
    }
}
