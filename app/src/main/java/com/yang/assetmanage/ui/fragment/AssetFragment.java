package com.yang.assetmanage.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.RVAdapter;
import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.entity.Bill;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.ui.AddBillActivity;
import com.yang.assetmanage.ui.AddMainAssetActivity;
import com.yang.assetmanage.ui.AssetDetailActivity;
import com.yang.assetmanage.ui.BaseActivity;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by yangximin on 2019/5/29.
 */

public class AssetFragment extends BaseFragment implements RVAdapter.OnItemClickListener {


    RecyclerView mBillRecyclerView, mAssetRcv;

    private RVAdapter<Bill> mBillAdapter;

    private RVAdapter<Asset> mAssetAdapter;

    DbUtils mDbUtils;

    BaseActivity baseActivity;

    DrawerLayout mDrawerLayout;

    TextView mBillNameTv;

    ImageView mBillIv;

    TextView mBillManageBtn;
    boolean isBillEdit = false;
    int[] mDrawableIds = {R.drawable.icon_bank_spdb_bg, R.drawable.icon_bank_hxbk_bg, R.drawable.icon_bank_msbc_bg, R.drawable.icon_bank_pcbc_bg};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        baseActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAssetRcv = findViewById(R.id.asset_rv);
        mBillRecyclerView = findViewById(R.id.rv_bill);
        baseActivity.setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mBillNameTv = findViewById(R.id.asset_add_name_tv);
        mBillIv = findViewById(R.id.asset_add_bg_iv);
        mBillManageBtn = findViewById(R.id.bill_edt_btn);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                baseActivity, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        initRcyView();
        initRcyViewData(null);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.asset_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.toActivity(AddMainAssetActivity.class);
            }
        });
        mBillManageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBillEdit) {
                    //非编辑状态
                    mBillManageBtn.setText("账本编辑");
                } else {
                    //编辑状态
                    mBillManageBtn.setText("取消编辑");
                }
                isBillEdit = !isBillEdit;
                initData();
            }
        });
    }

    private void initRcyViewData(String billId) {
        if (TextUtils.isEmpty(billId)) {
            billId = (String) SPUtil.getData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
        }
        if (TextUtils.isEmpty(billId)) {
            mBillNameTv.setText("日常账本");
            mBillIv.setBackgroundResource(mDrawableIds[0]);
        }
        try {
            int pos = (Integer.parseInt(billId) - 1) % 4;
            mBillIv.setBackgroundResource(mDrawableIds[pos]);
            mBillNameTv.setText(DbUtils.getInstance().getBillName(billId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Asset> assets = DbUtils.getInstance().getAssetList(billId);
        mAssetAdapter.clear();
        mAssetAdapter.addAll(assets);
    }

    private void initRcyView() {
        mAssetRcv.setLayoutManager(new LinearLayoutManager(baseActivity));
        mBillRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        mAssetAdapter = new RVAdapter<Asset>(getActivity(), R.layout.item_asset_layout) {
            @Override
            protected void convert(ViewHolder vH, Asset item, int position) {
                itemAssetConvert(vH, item, position);
            }
        };
        mBillAdapter = new RVAdapter<Bill>(getActivity(), R.layout.item_bill) {
            @Override
            protected void convert(ViewHolder vH, Bill bill, int position) {
                itemBillConvert(vH, bill, position);
            }
        };
        mAssetRcv.setAdapter(mAssetAdapter);
        mBillRecyclerView.setAdapter(mBillAdapter);
        View footerView = LayoutInflater.from(baseActivity).inflate(R.layout.item_bill_footer, mBillRecyclerView, false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.toActivity(AddBillActivity.class);
            }
        });
        mBillAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bill bill = mBillAdapter.getData(position);
                initRcyViewData(bill.getId());
                SPUtil.saveData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, bill.getId());
                mDrawerLayout.closeDrawers();
                mBillNameTv.setText(bill.getBillName());
                int pos = position % 4;
                mBillIv.setBackgroundResource(mDrawableIds[pos]);
                EventBus.getDefault().post(Constants.Event.EVENT_ADD_ASSET_SUCCESS);
            }
        });
        mBillAdapter.addFooterView(footerView);
        mAssetAdapter.setOnItemClickListener(this);
    }

    private void itemAssetConvert(RVAdapter.ViewHolder vH, Asset item, int position) {
        vH.setText(R.id.asset_main_type_tv, item.getMoneyName());
        vH.setText(R.id.asset_main_date_tv, item.getCreteData());
        vH.setText(R.id.asset_main_member_edt, item.getRemark());
        TextView money = vH.getView(R.id.asset_main_money_edt);
        // 支出
        if (TextUtils.equals(Constants.Normal.ASSET_TYPE_EXPEND, item.getMoneyType())) {
            money.setTextColor(mContext.getResources().getColor(R.color.global_red_button_color));
            vH.setText(R.id.asset_main_money_edt, "-" + item.getMoney() + "元");
        } else {
            money.setTextColor(mContext.getResources().getColor(R.color.global_green_color));
            vH.setText(R.id.asset_main_money_edt, "+" + item.getMoney() + "元");
        }
    }

    private void itemBillConvert(RVAdapter.ViewHolder vH, final Bill bill, int position) {
        vH.setText(R.id.item_bill_name_tv, bill.getBillName());
        TextView tv = vH.getView(R.id.item_bill_name_tv);
        View delView = vH.getView(R.id.bill_del_tv);
        if (position != 0 && isBillEdit) {
            delView.setVisibility(View.VISIBLE);
        } else {
            delView.setVisibility(View.GONE);
        }
        delView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(bill);
            }
        });
        int pos = position % 4;
        tv.setBackgroundResource(mDrawableIds[pos]);
    }

    private void showDialog(final Bill bill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("删除\" " + bill.getBillName() + "\" 的同时会删掉该账单下的财务记录，是否删除?")
                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DbUtils.getInstance().delete("BILL", "_ID=?", new String[]{bill.getId()});
                            showMessage("删除成功");
                            SPUtil.saveData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
                            mDrawerLayout.closeDrawers();
                            mBillManageBtn.performClick();
                            initRcyViewData(null);
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

    @Override
    public void onBaseEvent(Object event) {
        if (event instanceof String) {
            String ev = (String) event;
            if (TextUtils.equals(Constants.Event.EVENT_ADD_ASSET_SUCCESS, ev)) {
                initRcyViewData(null);
            } else if (TextUtils.equals(Constants.Event.EVENT_ADD_BILL_SUCCESS, ev)) {
                initData();
            }
        }
    }

    @Override
    protected void initData() {
        User user = (com.yang.assetmanage.entity.User) SPUtil.getObjData(MyApplication.getInstance(), Constants.Sp.SP_KEY_USER_INFO);
        mDbUtils = DbUtils.getInstance();
        List<Bill> bills = mDbUtils.selectBill(user == null ? "" : user.getId());
        mBillAdapter.clear();
        mBillAdapter.addAll(bills);
    }

    @Override
    public void onItemClick(View view, int position) {
        Asset asset = mAssetAdapter.getData(position);
        Intent intent = new Intent(getActivity(), AssetDetailActivity.class);
        intent.putExtra(Constants.Intent.INTENT_KEY_OBJ, asset);
        intent.putExtra(Constants.Intent.INTENT_KEY_STR, asset.getMoneyType());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            initRcyViewData(null);
        }
    }
}
