package com.yang.assetmanage.ui.fragment;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.yang.assetmanage.ui.BaseActivity;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.SPUtil;

import java.util.List;

/**
 * Created by yangximin on 2019/5/29.
 */

public class AssetFragment extends BaseFragment {


    RecyclerView mBillRecyclerView, mAssetRcv;

    private RVAdapter<Bill> mBillAdapter;

    private RVAdapter<Asset> mAssetAdapter;

    DbUtils mDbUtils;

    BaseActivity baseActivity;

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                baseActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        initRcyView();
        initRcyViewData(null);
        findViewById(R.id.asset_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.toActivity(AddMainAssetActivity.class);
            }
        });
    }

    private void initRcyViewData(String billId) {
        if (TextUtils.isEmpty(billId)) {
            billId = (String) SPUtil.getData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
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
            }
        });
        mBillAdapter.addFooterView(footerView);
    }

    private void itemAssetConvert(RVAdapter.ViewHolder vH, Asset item, int position) {
        vH.setText(R.id.asset_main_type_tv, item.getMoneyName());
        vH.setText(R.id.asset_main_date_tv, item.getCreteData());
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

    private void itemBillConvert(RVAdapter.ViewHolder vH, Bill bill, int position) {
        vH.setText(R.id.item_bill_name_tv, bill.getBillName());
        TextView tv = vH.getView(R.id.item_bill_name_tv);
        int[] ids = {R.drawable.icon_bank_spdb_bg, R.drawable.icon_bank_hxbk_bg, R.drawable.icon_bank_msbc_bg, R.drawable.icon_bank_pcbc_bg};
        int pos = position % 4;
        tv.setBackgroundResource(ids[pos]);
    }

    @Override
    public void onBaseEvent(Object event) {
        if (event instanceof Bill) {
            initData();
            Bill bill = (Bill) event;
            SPUtil.saveData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, bill.getId());
        } else if (event instanceof String) {
            String ev = (String) event;
            if (TextUtils.equals(Constants.Event.EVENT_ADD_ASSET_SUCCESS, ev)) {
                initRcyViewData(null);
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
}
