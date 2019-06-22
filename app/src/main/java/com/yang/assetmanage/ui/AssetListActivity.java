package com.yang.assetmanage.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.RVAdapter;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.utils.Constants;

import java.util.List;

/**
 * Created by YXM
 * on 2019/6/11.
 */

public class AssetListActivity extends BaseActivity implements RVAdapter.OnItemClickListener {
    RecyclerView mRecyclerView;

    RVAdapter<Asset> mAssetAdapter;

    private String mTypeId, mBillId, mYear;

    private int mDate;

    private String mType;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.asset_list_rv);
        initRcyView();
    }

    @Override
    protected void initData() {
        setTitle("财务分类列表");
        mTypeId = getIntent().getStringExtra(Constants.Intent.INTENT_KEY_TYPE_ID);
        mType = getIntent().getStringExtra(Constants.Intent.INTENT_KEY_TYPE);
        mBillId = getIntent().getStringExtra(Constants.Intent.INTENT_KEY_BILL_ID);
        mYear = getIntent().getStringExtra(Constants.Intent.INTENT_KEY_YEAR);
        mDate = getIntent().getIntExtra(Constants.Intent.INTENT_KEY_DAY, 1);
        selectAsset();
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
        mRecyclerView.setAdapter(mAssetAdapter);

    }


    private void selectAsset() {
        List<Asset> assets = DbUtils.getInstance().getAssetTypeList(mTypeId, mBillId, mYear, mDate);
        mAssetAdapter.clear();
        mAssetAdapter.addAll(assets);
    }


    private void itemAssetConvert(RVAdapter.ViewHolder vH, Asset item, int position) {
        vH.setText(R.id.asset_main_type_tv, item.getMoneyName());
        vH.setText(R.id.asset_main_date_tv, item.getCreteData());
        vH.setText(R.id.asset_main_member_edt,item.getRemark());
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
        Asset asset = mAssetAdapter.getData(position);
        Intent intent = new Intent(this, AssetDetailActivity.class);
        intent.putExtra(Constants.Intent.INTENT_KEY_OBJ, asset);
        intent.putExtra(Constants.Intent.INTENT_KEY_STR, mType);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectAsset();
        }
    }
}
