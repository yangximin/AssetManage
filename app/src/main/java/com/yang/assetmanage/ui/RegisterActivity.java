package com.yang.assetmanage.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.OnKeyboardListener;
import com.yang.assetmanage.R;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.EncryptUtil;
import com.yang.assetmanage.utils.Regular;
import com.yang.assetmanage.utils.SPUtil;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class RegisterActivity extends BaseActivity {

    private EditText mUserNameEdt;

    private EditText mPwdEdt;

    private TextView mLoginBtn;

    private TextView mRegisterBtn;

    private TextView mForgetBtn;

    private View mLlLogin;

    private EditText mIdCardEdt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_layout;
    }

    @Override
    protected void initView() {
        mUserNameEdt = findViewById(R.id.userName);
        mPwdEdt = findViewById(R.id.userPassword);
        mLoginBtn = findViewById(R.id.login_button);
        mRegisterBtn = findViewById(R.id.register_button);
        mForgetBtn = findViewById(R.id.login_forget_pwd_tv);
        mLlLogin = findViewById(R.id.ll_login);
        mIdCardEdt = findViewById(R.id.register_id_card);
        initListener();
    }

    private void initListener() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(LoginActivity.class);
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEdt.getText().toString();
                String pwd = mPwdEdt.getText().toString();
                String idCard = mIdCardEdt.getText().toString();
                if (TextUtils.isEmpty(userName) || !userName.matches(Regular.USER_NAME)) {
                    showMessage("用户名为4到16位（字母，数字，下划线，减号）");
                    return;
                }
                if (TextUtils.isEmpty(pwd) || !pwd.matches(Regular.PASSWORD_CHECK)) {
                    showMessage("密码须为6-16位字母、数字或特殊符号两两混合！");
                    return;
                }
                if (TextUtils.isEmpty(idCard) || idCard.length() != 6) {
                    showMessage("请输入正确的身份证后六位");
                    return;
                }
                try {
                    pwd = EncryptUtil.encodeByMd5(pwd);
                    startRegister(userName, pwd, idCard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        mForgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(ForgetPasswordActivity.class);
            }
        });
        mImmersionBar.setOnKeyboardListener(new OnKeyboardListener() {
            @Override
            public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                if (isPopup) {
                    mLlLogin.setVisibility(View.GONE);
                } else {
                    mLlLogin.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void startRegister(String userName, String pwd, String idCard) {
        DbUtils DbUtils = com.yang.assetmanage.db.DbUtils.getInstance();
        if (DbUtils.isExistUser(userName)) {
            showMessage("该用户已注册，请直接登录");
            return;
        }
        User user = new User();
        user.setName(userName);
        user.setPassword(pwd);
        user.setIdCard(idCard);
        user.setRegisterDate(System.currentTimeMillis());
        boolean isSuccess = DbUtils.insertUser(user);
        if (isSuccess) {
            SPUtil.saveObjData(this, Constants.Sp.SP_KEY_USER_INFO,user);
            toActivity(CreateGesturePasswordActivity.class);
        } else {
            showMessage("注册失败，请重新注册");
        }
    }

    @Override
    protected void initData() {

    }
}
