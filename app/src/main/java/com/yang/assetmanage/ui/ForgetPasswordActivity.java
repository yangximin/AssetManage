package com.yang.assetmanage.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.db.DBUtils;
import com.yang.assetmanage.entity.ForgetPwd;
import com.yang.assetmanage.utils.EncryptUtil;
import com.yang.assetmanage.utils.Regular;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class ForgetPasswordActivity extends BaseActivity {


    private EditText mUserNameEdt;

    private EditText mPwdEdt;

    private EditText mConfirmPwdEdt;

    private TextView mConfirmBtn;

    private EditText mIdCardEdt;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd_layout;
    }

    @Override
    protected void initView() {
        mUserNameEdt = findViewById(R.id.forget_pwd_user_name_edt);
        mIdCardEdt = findViewById(R.id.forget_pwd_user_id_card_edt);
        mPwdEdt = findViewById(R.id.forget_pwd_new_pwd_edt);
        mConfirmPwdEdt = findViewById(R.id.forget_pwd_confirm_pwd_edt);
        mConfirmBtn = findViewById(R.id.confirm_btn);
    }

    @Override
    protected void initData() {
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCommit();
            }
        });
    }

    public boolean isEmpty(String text, String msg) {
        if (TextUtils.isEmpty(text)) {
            showMessage(msg);
            return true;
        }
        return false;
    }

    private void confirmCommit() {
        String userName;
        String idCard;
        String newPassword;
        String confirmPassword;
        idCard = mIdCardEdt.getText().toString();
        userName = mUserNameEdt.getText().toString();
        if (isEmpty(userName, getString(R.string.login_input_name))) {
            return;
        }
        if (isEmpty(idCard, getString(R.string.login_id_card_last_number_hint))) {
            return;
        }
        newPassword = mPwdEdt.getText().toString();
        confirmPassword = mConfirmPwdEdt.getText().toString();

        if (isEmpty(newPassword, getString(R.string.login_new_pwd_hint))) {
            return;
        }
        if (!newPassword.matches(Regular.PASSWORD_CHECK)) {
            showMessage(getString(R.string.login_register_password_error));
            return;
        }
        if (!TextUtils.equals(newPassword, confirmPassword)) {
            showMessage("确认密码不一致");
            return;
        }
        //忘记密码or修改密码
        try {
            mConfirmBtn.setFocusable(true);
            mConfirmBtn.setFocusableInTouchMode(true);
            mConfirmBtn.requestFocus();
            newPassword = EncryptUtil.encodeByMd5(newPassword);
            ForgetPwd forgetPwd = new ForgetPwd();
            forgetPwd.setName(userName);
            forgetPwd.setIdCard(idCard);
            forgetPwd.setPwd(newPassword);
            startUpDataPassword(forgetPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startUpDataPassword(ForgetPwd forgetPwd) {
        DBUtils DBUtils = DBUtils.getInstance();
        boolean isSuccess = DBUtils.upSetPassword(forgetPwd);
        if (isSuccess){
            showMessage("修改密码成功");
            finish();
        }else {
            showMessage("修改密码失败，请确认信息是否正确");
        }
//        DBUtils

    }
}
