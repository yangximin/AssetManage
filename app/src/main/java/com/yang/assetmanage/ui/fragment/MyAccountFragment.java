package com.yang.assetmanage.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.ui.BaseActivity;
import com.yang.assetmanage.ui.ForgetPasswordActivity;
import com.yang.assetmanage.ui.GesturePasswordActivity;
import com.yang.assetmanage.ui.LoginActivity;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.SPUtil;

/**
 * Created by YXM
 * on 2019/6/22.
 */

public class MyAccountFragment extends BaseFragment {

    private TextView userName;

    private View mLoginPwd, mGesture, mAbout, loginOut;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_my_account_layout;
    }

    @Override
    protected void initView() {
        userName = findViewById(R.id.my_user_name);
        mLoginPwd = findViewById(R.id.my_login_pwd);
        mGesture = findViewById(R.id.my_gesture_pwd);
        mAbout = findViewById(R.id.my_about);
        loginOut = findViewById(R.id.my_login_out);
        mLoginPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).toActivity(ForgetPasswordActivity.class);
            }
        });
        mGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GesturePasswordActivity.class);
                startActivity(intent);
            }
        });
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("关于系统")
                        .setMessage("基于Android个人财务管理系统-杨习敏作业")
                        .setPositiveButton("原来没页面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("温馨提示")
                        .setMessage("是否要退出登录")
                        .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtil.saveObjData(getContext(), Constants.Sp.SP_KEY_USER_INFO, null);
                                ((BaseActivity) getActivity()).toActivity(LoginActivity.class);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    protected void initData() {
        User user = (User) SPUtil.getObjData(getContext(), Constants.Sp.SP_KEY_USER_INFO);
        userName.setText(user.getName());
    }
}
