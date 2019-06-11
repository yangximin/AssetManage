package com.yang.assetmanage.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.entity.Dicts;
import com.yang.assetmanage.entity.TabEntity;
import com.yang.assetmanage.ui.fragment.SelectionFragment;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.FragmentTableBar;
import com.yang.assetmanage.utils.GenerateDateUtils;
import com.yang.assetmanage.view.CommonTabLayout;
import com.yang.assetmanage.view.CustomTabEntity;
import com.yang.assetmanage.view.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 下拉弹框界面
 * Created by YXM
 * on 2018/6/1.
 */

public class SelectionDialogActivity extends BaseActivity {

    private static final String FRAGMENT_SELECTION_TAG = SelectionFragment.class.getName();
    /*选择类型*/
    public static final String TYPE_INTENT_TYPE = "type_intent_type";
    /*单项选择的数据源*/
    public static final String TYPE_INTENT_SELECTION = "type_intent_selection";
    /*fieldKey用户返回时查找控件*/
    public static final String TYPE_INTENT_FIELD_KEY = "type_intent_field_key";
    /*page field用户动态验证*/
    public static final String TYPE_INTENT_PAGE_KEY = "type_intent_page_key";
    /*地址选择控件*/
    public static final String SELECTION_TYPE_ADDRESS = "selection_type_address";
    /*常规类答案id*/
    public static final String SELECTION_CHOICE_ID = "SELECTION_CHOICE_ID";
    /* 已选择的答案 */
    public static final String SELECTION_ANSWER_KEY = "selection_answer_key";
    public static String DATE = "DATE";

    public static final String TYPE_NAME_TITLE = "type_name_title";

    private FragmentTableBar mFragmentTableBar;

    private List<Dicts> mDicts;

    SelectionFragment mSingleSelectionFragment;

    private boolean isFirstLoad = true;

    private View mTranslucentView;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout mTabLayout;

    private ImageView mCloseIv;
    /*选择类型名字*/
    private TextView mTypeNameTv;


    private String mType;


    private TabEntity mFirstTab, mSecondTab, mThirdTab;

    private final String PLEASE_SELECT = "请选择";
    private String mAddressType;
    private boolean mIsNeedDayForMonth;

    private boolean mNeedMoreYear;

    /**
     * 元素名称
     */
    private String elementName;
    /**
     * 元素内容
     */
    private String elementContent;
    /**
     * 输入类型
     */
    private String inputType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selection_layout;
    }

    @Override
    public boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    protected void initView() {
        initSystemBar();
        String typeName = getIntent().getStringExtra(TYPE_NAME_TITLE);
        mIsNeedDayForMonth = true;
        mAddressType = getIntent().getStringExtra(SELECTION_TYPE_ADDRESS);
        mNeedMoreYear = getIntent().getBooleanExtra(Constants.Normal.TYPE_SELECT_YEAR_MONTH, true);
        mTranslucentView = findViewById(R.id.selection_translucent_view);
        mCloseIv = findViewById(R.id.selection_close_iv);
        mTypeNameTv = findViewById(R.id.selection_type_name_tv);
        mFragmentTableBar = new FragmentTableBar(this, R.id.selection_content_fl);
        mSingleSelectionFragment = (SelectionFragment) mFragmentTableBar.switchFragment(FRAGMENT_SELECTION_TAG);
//        if (TextUtils.isEmpty(typeName) && mPageField != null) {
//            mTypeNameTv.setText(mPageField.getName());
//        } else {
//            mTypeNameTv.setText(typeName);
//        }
        setInitListener();

    }

    /**
     * 初始化状态栏-需要透明状态栏时手动调用
     */
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra(TYPE_INTENT_TYPE);
        mDicts = (List<Dicts>) getIntent().getSerializableExtra(TYPE_INTENT_SELECTION);
        initTabLayout();
    }

    private void initTabLayout() {
        mTabLayout = findViewById(R.id.selection_indicator_tl);
        mFirstTab = new TabEntity(PLEASE_SELECT, false);
        mSecondTab = new TabEntity(PLEASE_SELECT, false);
        mThirdTab = new TabEntity(PLEASE_SELECT, false);
        if (mDicts != null) {
            mSecondTab.setVisible(true);
        } else {
            mFirstTab.setVisible(true);
        }
        mTabEntities.add(mFirstTab);
        mTabEntities.add(mSecondTab);
        mTabEntities.add(mThirdTab);
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mSingleSelectionFragment.onTabSelect(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    private void setInitListener() {
        mTranslucentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isFirstLoad) {
            isFirstLoad = false;
            if (TextUtils.equals(mType, DATE)) {
                mSingleSelectionFragment.setListData(GenerateDateUtils.getInstance(), mIsNeedDayForMonth, mNeedMoreYear);
            } else {
                mSingleSelectionFragment.setListData(mDicts);
                mTabLayout.setCurrentTab(1);
            }
        }
    }


    @Override
    protected boolean isNeedTitle() {
        return false;
    }


    public void confirmSelection(ArrayList resultList) {

        Intent intent = new Intent();
        intent.putExtra(TYPE_INTENT_SELECTION, resultList);
        setResult(RESULT_OK, intent);
        finish();
    }


    public void onSelectItem(int pos, String text) {
//        UserActionRecord.elementOperEnd(getTextViewContent(mTypeNameTv), text, "");

        if (pos == 0) {
            mFirstTab.setTitle(PLEASE_SELECT);
        } else if (pos == 1) {
            if (!TextUtils.isEmpty(text)) {
                mFirstTab.setTitle(text);
            }
            mSecondTab.setTitle(PLEASE_SELECT);
        } else if (pos == 2) {
            if (!TextUtils.isEmpty(text)) {
                mSecondTab.setTitle(text);
            }
            mThirdTab.setTitle(PLEASE_SELECT);
        }
        if (pos >= 3) {
            return;
        }
        mTabLayout.setCurrentTab(pos, false);

    }

}
