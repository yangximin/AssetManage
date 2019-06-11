package com.yang.assetmanage.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.RVAdapter;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.utils.Constants;

/**
 * Created by YXM
 * on 2019/6/11.
 */

public class AssetListActivity extends BaseActivity implements RVAdapter.OnItemClickListener {
    RecyclerView mRecyclerView;

    RVAdapter<Asset> mAssetAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.asset_list_rv);
    }

    @Override
    protected void initData() {

    }

    private void initRcyView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAssetAdapter = new RVAdapter<Asset>(this, R.layout.item_asset_layout) {
            @Override
            protected void convert(ViewHolder vH, Asset item, int position) {
                itemAssetConvert(vH, item, position);
            }
        };
        mAssetAdapter.setOnItemClickListener(this);
    }

    private void itemAssetConvert(RVAdapter.ViewHolder vH, Asset item, int position) {
        vH.setText(R.id.asset_main_type_tv, item.getMoneyName());
        vH.setText(R.id.asset_main_date_tv, item.getCreteData());
        TextView money = vH.getView(R.id.asset_main_money_edt);
        // 支出
        if (TextUtils.equals(Constants.Normal.ASSET_TYPE_EXPEND, item.getMoneyType())) {
            money.setTextColor(this.getResources().getColor(R.color.global_red_button_color));
            vH.setText(R.id.asset_main_money_edt, "-" + item.getMoney() + "元");
        } else {
            money.setTextColor(this.getResources().getColor(R.color.global_green_color));
            vH.setText(R.id.asset_main_money_edt, "+" + item.getMoney() + "元");
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
