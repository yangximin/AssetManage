package com.yang.assetmanage.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.SPUtil;

/**
 * Created by yangximin on 2019/5/29.
 */

public class AddBillActivity extends BaseActivity {

    private EditText mBillEdt;

    private TextView mConfirmBtn;

    private User mUser;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_bill_layout;
    }

    @Override
    protected void initView() {
        mBillEdt = findViewById(R.id.add_bill_name_tv);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBill();
            }
        });
    }

    private void addBill() {
        mUser = (User) SPUtil.getObjData(this, Constants.Sp.SP_KEY_USER_INFO);
        if(mUser==null){
            return;
        }
        String billName = mBillEdt.getText().toString();
        if(TextUtils.isEmpty(billName)){
            showMessage("请输入账本名字");
            return;
        }
        DbUtils dbUtils =  DbUtils.getInstance();
        boolean isSuccess = dbUtils.insertBill(mUser.getId(),billName);
        if(isSuccess){
            finish();
        }else {
            showMessage("账本名字重复,请重新输入");
        }
    }

    @Override
    protected void initData() {

    }
}
