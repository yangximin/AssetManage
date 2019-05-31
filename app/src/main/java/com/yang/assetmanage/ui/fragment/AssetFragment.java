package com.yang.assetmanage.ui.fragment;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.RVAdapter;
import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Bill;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.ui.AddBillActivity;
import com.yang.assetmanage.ui.BaseActivity;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.SPUtil;

import java.util.List;

/**
 * Created by yangximin on 2019/5/29.
 */

public class AssetFragment extends BaseFragment {


    RecyclerView mRecyclerView;

    private RVAdapter<Bill> mAdapter;

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
        mRecyclerView = findViewById(R.id.rv_bill);
        baseActivity.setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                baseActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        initRcyView();
    }

    private void initRcyView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        mAdapter = new RVAdapter<Bill>(getActivity(), R.layout.item_bill) {
            @Override
            protected void convert(ViewHolder vH, Bill bill, int position) {
                itemConvert(vH, bill, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        View footerView = LayoutInflater.from(baseActivity).inflate(R.layout.item_bill_footer, mRecyclerView, false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.toActivity(AddBillActivity.class);
            }
        });
        mAdapter.addFooterView(footerView);
    }

    private void itemConvert(RVAdapter.ViewHolder vH, Bill bill, int position) {
        vH.setText(R.id.item_bill_name_tv, bill.getBillName());
    }

    @Override
    public void onBaseEvent(Object event) {

    }
    @Override
    protected void initData() {
        User user = (com.yang.assetmanage.entity.User) SPUtil.getObjData(MyApplication.getInstance(), Constants.Sp.SP_KEY_USER_INFO);
        mDbUtils = DbUtils.getInstance();
        List<Bill> bills = mDbUtils.selectBill(user == null ? "" : user.getId());
        mAdapter.clear();
        mAdapter.addAll(bills);
    }
}
