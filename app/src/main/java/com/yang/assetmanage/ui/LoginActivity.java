package com.yang.assetmanage.ui;

import android.content.Intent;
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

import static com.yang.assetmanage.ui.CreateGesturePasswordActivity.KEY_IS_CREATE;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class LoginActivity extends BaseActivity {

    private EditText mUserNameEdt;

    private EditText mPwdEdt;

    private TextView mLoginBtn;

    private TextView mRegisterBtn;

    private TextView mForgetBtn;

    private View mLlRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mUserNameEdt = findViewById(R.id.userName);
        mPwdEdt = findViewById(R.id.userPassword);
        mLoginBtn = findViewById(R.id.login_button);
        mRegisterBtn = findViewById(R.id.register_button);
        mForgetBtn = findViewById(R.id.login_forget_pwd_tv);
        mLlRegister = findViewById(R.id.ll_register);
        initListener();
    }

    private void initListener() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEdt.getText().toString();
                String pwd = mPwdEdt.getText().toString();
                if (TextUtils.isEmpty(userName) || !userName.matches(Regular.USER_NAME)) {
                    showMessage("用户名为4到16位（字母，数字，下划线，减号）");
                    return;
                }
                if (TextUtils.isEmpty(pwd) || !pwd.matches(Regular.PASSWORD_CHECK)) {
                    showMessage("密码须为6-16位字母、数字或特殊符号两两混合！");
                    return;
                }
                try {
                    pwd = EncryptUtil.encodeByMd5(pwd);
                    startLogin(userName, pwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(RegisterActivity.class);
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
                    mLlRegister.setVisibility(View.GONE);
                } else {
                    mLlRegister.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void startLogin(String userName, String pwd) {
        DbUtils DbUtils = com.yang.assetmanage.db.DbUtils.getInstance();
        User user = DbUtils.login(userName, pwd);
        if (user != null) {
            toActivity(CreateGesturePasswordActivity.class);
            SPUtil.saveObjData(this, Constants.Sp.SP_KEY_USER_INFO, user);
        } else {
            showMessage("用户名密码错误，请重新登录");
        }
    }

    @Override
    protected void initData() {
        User user = (User) SPUtil.getObjData(this, Constants.Sp.SP_KEY_USER_INFO);
        if (user != null) {
            Intent intent = new Intent(this,CreateGesturePasswordActivity.class);
            intent.putExtra(KEY_IS_CREATE,false);
//            startActivity(intent);
//            toActivity(CreateGesturePasswordActivity.class);
        }
    }
}
