package com.yang.assetmanage.ui;

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
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Bill;
import com.yang.assetmanage.entity.User;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.SPUtil;

import java.util.List;

public class MainActivity extends BaseActivity {

    RecyclerView mRecyclerView;

    private RVAdapter<Bill> mAdapter;

    DbUtils mDbUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_asset_layout;
    }

    @Override
    protected boolean isNeedTitle() {
        return false;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.rv_bill);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        initRcyView();
    }

    protected void initData() {
        User user = (com.yang.assetmanage.entity.User) SPUtil.getObjData(this, Constants.Sp.SP_KEY_USER_INFO);
        mDbUtils = DbUtils.getInstance();
        List<Bill> bills = mDbUtils.selectBill(user == null ? "" : user.getId());
        mAdapter.clear();
        mAdapter.addAll(bills);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void initRcyView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVAdapter<Bill>(this, R.layout.item_bill) {
            @Override
            protected void convert(ViewHolder vH, Bill bill, int position) {
                itemConvert(vH, bill, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        View footerView = LayoutInflater.from(this).inflate(R.layout.item_bill_footer, null);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(AddBillActivity.class);
            }
        });
        mAdapter.addFooterView(footerView);
    }

    private void itemConvert(RVAdapter.ViewHolder vH, Bill bill, int position) {
        vH.setText(R.id.item_bill_name_tv, bill.getBillName());
    }


}
