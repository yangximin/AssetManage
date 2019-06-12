package com.yang.assetmanage.ui;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.yang.assetmanage.R;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.ui.fragment.AddExpendRecordFragment;
import com.yang.assetmanage.ui.fragment.AddIncomeRecordFragment;
import com.yang.assetmanage.utils.Constants;

/**
 * Created by YXM
 * on 2019/6/12.
 */

public class AssetDetailActivity extends BaseActivity {
    /**
     * Fragment适配器
     */

    private AddExpendRecordFragment mAddExpendRecordFragment;

    LinearLayout mLinearLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_detail_layout;
    }

    @Override
    protected void initView() {
        mLinearLayout = findViewById(R.id.rootFrameLayout);

    }

    @Override
    protected void initData() {
        Asset asset = (Asset) getIntent().getSerializableExtra(Constants.Intent.INTENT_KEY_OBJ);
        String type = getIntent().getStringExtra(Constants.Intent.INTENT_KEY_STR);
        Fragment fragment;
        if (TextUtils.equals(Constants.Normal.TYPE_EXPEND, type)) {
            fragment = AddExpendRecordFragment.getInstance(asset);
        } else {
            fragment = AddIncomeRecordFragment.getInstance(asset);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.rootFrameLayout, fragment).commitAllowingStateLoss();
    }


}
