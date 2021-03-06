package com.yang.assetmanage.ui.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.entity.Dicts;
import com.yang.assetmanage.entity.SelectionDate;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.ui.BaseActivity;
import com.yang.assetmanage.ui.SelectionDialogActivity;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.GenerateDateUtils;
import com.yang.assetmanage.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by YXM
 * on 2019/5/30.
 */

public class AddExpendRecordFragment extends BaseFragment {

    private EditText mMoneyEdt;

    private TextView mTypeBtn;

    private TextView mBillBtn;

    private TextView mDateBtn;

    private TextView mMemberBtn;

    private EditText mRemarkEdt;

    private TextView mConfirmBtn;

    private int mClickType;

    Dicts mTypeDict, mMemberDict, mBillDict;

    /**
     * 支出类型
     */
    private final static int TYPE_CLASSIFY = 1;

    /**
     * 账单类型
     */
    private final static int TYPE_BILL = 2;

    /**
     * 日期类型
     */
    private final static int TYPE_DATE = 3;

    /**
     * 成员类型
     */
    private final static int TYPE_MEMBER = 4;
    private User mUser;

    private boolean isAlter;

    private Asset mAsset;

    private View mDelBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_expend_record_layout;
    }

    @Override
    protected void initView() {
        mMoneyEdt = findViewById(R.id.expend_money_edt);
        mTypeBtn = findViewById(R.id.expend_type_edt);
        mBillBtn = findViewById(R.id.expend_bill_edt);
        mDateBtn = findViewById(R.id.expend_date_edt);
        mMemberBtn = findViewById(R.id.expend_member_edt);
        mRemarkEdt = findViewById(R.id.expend_remark_edt);
        mConfirmBtn = findViewById(R.id.login_button);
        mDelBtn = findViewById(R.id.del_button);
        initListener();
    }

    private void initListener() {
        mTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickType = TYPE_CLASSIFY;
                toSelectActivity(GenerateDateUtils.getInstance().getExpendType());
            }
        });
        mBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickType = TYPE_BILL;
                toSelectActivity(GenerateDateUtils.getInstance().getBillList());
            }
        });
        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickType = TYPE_DATE;
                toSelectActivity(null);
            }
        });
        mMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickType = TYPE_MEMBER;
                toSelectActivity(GenerateDateUtils.getInstance().getMemberDicts());
            }
        });
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAlter) {
                    alterAsset();
                } else {
                    confirmCommit();
                }
            }
        });
        mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAsset();
            }
        });


    }

    private void deleteAsset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("是否确认删除这条财务记录？")
                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DbUtils.getInstance().delete("Asset", "_ID=?", new String[]{mAsset.getId()});
                            showMessage("删除成功");
                            SPUtil.saveData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
                            getActivity().finish();
                            EventBus.getDefault().post(Constants.Event.EVENT_ADD_ASSET_SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage("删除失败");
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


    /**
     * 新增记录
     */
    private void confirmCommit() {
        String money = mMoneyEdt.getText().toString();
        if (TextUtils.isEmpty(money)) {
            showMessage("支出金额不能为空");
            return;
        }
        /**
         *                 "   BILL_ID       INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
         "   USER_ID       INTEGER      NOT NULL,\n" +
         "   MONEY         TEXT     NOT NULL,\n" +
         "   MONEY_TYPE    INT      NOT NULL,\n" +
         "   CRETE_DATA    TEXT     NOT NULL,\n" +
         "   MEMBER        TEXT     NOT NULL,\n" +
         "   REMARK        TEXT     NOT NULL\n" +
         ");");
         */
        ContentValues cv = new ContentValues();
        cv.put("BILL_ID", mBillDict.getId());
        cv.put("USER_ID", mUser.getId());
        cv.put("MONEY", money);
        cv.put("MONEY_TYPE_ID", mTypeDict.getId());
        cv.put("MONEY_TYPE", mTypeDict.getType());
        cv.put("CRETE_DATA", mDateBtn.getText().toString());
        cv.put("MEMBER", mMemberDict.getId());
        cv.put("REMARK", mRemarkEdt.getText() == null ? "" : mRemarkEdt.getText().toString());
        try {
            DbUtils.getInstance().insert("ASSET", cv);
            showMessage("新增记录成功");
            EventBus.getDefault().post(Constants.Event.EVENT_ADD_ASSET_SUCCESS);
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("数据异常，请重试");
        }
    }

    /**
     * 修改记录
     */
    private void alterAsset() {
        String money = mMoneyEdt.getText().toString();
        if (TextUtils.isEmpty(money)) {
            showMessage("支出金额不能为空");
            return;
        }
        /**
         *                 "   BILL_ID       INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
         "   USER_ID       INTEGER      NOT NULL,\n" +
         "   MONEY         TEXT     NOT NULL,\n" +
         "   MONEY_TYPE    INT      NOT NULL,\n" +
         "   CRETE_DATA    TEXT     NOT NULL,\n" +
         "   MEMBER        TEXT     NOT NULL,\n" +
         "   REMARK        TEXT     NOT NULL\n" +
         ");");
         */
        ContentValues cv = new ContentValues();
        cv.put("BILL_ID", mBillDict.getId());
        cv.put("USER_ID", mUser.getId());
        cv.put("MONEY", money);
        cv.put("MONEY_TYPE_ID", mTypeDict.getId());
        cv.put("MONEY_TYPE", mTypeDict.getType());
        cv.put("CRETE_DATA", mDateBtn.getText().toString());
        cv.put("MEMBER", mMemberDict.getId());
        cv.put("REMARK", mRemarkEdt.getText() == null ? "" : mRemarkEdt.getText().toString());
        try {
            DbUtils.getInstance().updata("ASSET", cv, new String[]{mAsset.getId()});
            showMessage("修改记录成功");
            EventBus.getDefault().post(Constants.Event.EVENT_ADD_ASSET_SUCCESS);
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("数据异常，请重试");
        }

    }

    private void toSelectActivity(List<Dicts> dicts) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SelectionDialogActivity.class);
        if (dicts != null) {
            intent.putExtra(SelectionDialogActivity.TYPE_INTENT_SELECTION, (Serializable) dicts);
        } else {
            intent.putExtra(SelectionDialogActivity.TYPE_INTENT_TYPE, SelectionDialogActivity.DATE);
        }
        startActivityForResult(intent, 1);
        ((BaseActivity) getActivity()).overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out);
    }

    @Override
    protected void initData() {
        String createTime;
        Bundle bundle = getArguments();
        ((TextView) findViewById(R.id.expend_money_tv)).setText("支出金额");
        ((TextView) findViewById(R.id.expend_type_tv)).setText("支出类型");
        isAlter = bundle != null && bundle.getBoolean(Constants.Intent.INTENT_KEY_BOOLEAN);
        if (isAlter) {
            mAsset = (Asset) bundle.getSerializable(Constants.Intent.INTENT_KEY_OBJ);
            mConfirmBtn.setText("修改");
            mTypeDict = new Dicts(mAsset.getMoneyTypeId(), mAsset.getMoneyName(), mAsset.getMoneyType());
            mBillDict = new Dicts(mAsset.getBillId(), mAsset.getBillName());
            mMemberDict = new Dicts(mAsset.getMember(), mAsset.getMemberName());
            createTime = mAsset.getCreteData();
            mMoneyEdt.setText(mAsset.getMoney());
            mRemarkEdt.setText(mAsset.getRemark());
            mDelBtn.setVisibility(View.VISIBLE);
        } else {
            mTypeDict = GenerateDateUtils.getInstance().getExpendType().get(0);
            String billId = (String) SPUtil.getData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
            if (TextUtils.isEmpty(billId)) {
                mBillDict = GenerateDateUtils.getInstance().getBillList().get(0);
            } else {
                mBillDict = new Dicts(billId, DbUtils.getInstance().getBillName(billId));
            }
            mMemberDict = GenerateDateUtils.getInstance().getMemberDicts().get(0);
            createTime = GenerateDateUtils.getInstance().getCurrentTime();
        }
        mDateBtn.setText(createTime);
        mTypeBtn.setText(mTypeDict.getName());
        mBillBtn.setText(mBillDict.getName());
        mMemberBtn.setText(mMemberDict.getName());
        mUser = (com.yang.assetmanage.entity.User) SPUtil.getObjData(MyApplication.getInstance(), Constants.Sp.SP_KEY_USER_INFO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList obj = (ArrayList) data.getSerializableExtra(SelectionDialogActivity.TYPE_INTENT_SELECTION);
            switch (mClickType) {
                case TYPE_CLASSIFY:
                    mTypeDict = (Dicts) obj.get(0);
                    mTypeBtn.setText(mTypeDict.getName());
                    break;
                case TYPE_BILL:
                    mBillDict = (Dicts) obj.get(0);
                    mBillBtn.setText(mBillDict.getName());
                    break;
                case TYPE_DATE:
                    handleDate(obj);
                    break;
                case TYPE_MEMBER:
                    mMemberDict = (Dicts) obj.get(0);
                    mMemberBtn.setText(mMemberDict.getName());
                    break;
            }

        }
    }

    public static AddExpendRecordFragment getInstance(Asset asset) {
        AddExpendRecordFragment fragment = new AddExpendRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.Intent.INTENT_KEY_OBJ, asset);
        bundle.putSerializable(Constants.Intent.INTENT_KEY_BOOLEAN, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void handleDate(ArrayList list) {
        StringBuilder date = new StringBuilder();
        for (Object obj : list) {
            SelectionDate selectionDate = (SelectionDate) obj;
            date.append("-").append(selectionDate.getDate() < 10 ? "0" + selectionDate.getDate() : selectionDate.getDate());
        }
        mDateBtn.setText(date.toString().replaceFirst("-", ""));
    }
}
