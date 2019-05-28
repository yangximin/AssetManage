package com.yang.assetmanage.utils;

/**
 * 规则类-正则
 * Created by YXM
 * on 2018/5/24.
 */

public class Regular {
    /**
     * 验证手机号码格式的正则表达式 （可以验证以 13、15、18、14、17 开头的手机号格式）
     */
    public final static String MOBILE = "^1[3456789]\\d{9}$";

    /**
     * 密码规则-必须包含字母、数字、特殊字符其中两种
     */
    public final static String PASSWORD_CHECK = "^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\\\\(\\\\)])+$)([^(0-9a-zA-Z)]|[\\\\(\\\\)]|[a-zA-Z]|[0-9]){6,16}$";


    public final static String USER_NAME = "^[a-zA-Z0-9_-]{4,16}$";

}
