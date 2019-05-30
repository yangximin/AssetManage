package com.yang.assetmanage.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.SelectionAdapter;
import com.yang.assetmanage.entity.Dicts;
import com.yang.assetmanage.ui.SelectionDialogActivity;
import com.yang.assetmanage.utils.GenerateDateUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YXM
 * on 2018/6/1.
 */

public class SelectionFragment extends Fragment implements SelectionAdapter.ListDialogListener {

    private View mRootView;

    private FragmentActivity mActivity;

    private RecyclerView mRecyclerView;


    SelectionAdapter mSelectionAdapter;
    private String mAddressType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mActivity = getActivity();
            mRootView = inflater.inflate(R.layout.zac_loan_apply_selection_recycler_view_layout, container, false);
            initView();
            initListener();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }


    private void initView() {
        mSelectionAdapter = new SelectionAdapter(mActivity);
        mRecyclerView = mRootView.findViewById(R.id.zac_loan_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSelectionAdapter.setListDialogListener(this);
        mRecyclerView.setAdapter(mSelectionAdapter);
    }

    /**
     * 常规选择
     */
    public void setListData(List<Dicts> dicts) {
        mSelectionAdapter.setListData(dicts);
    }

//    public void setDictChoseId(String type, String choseId){
//        mSelectionAdapter.setDictChoseId(type,choseId);
//    }
    /**
     * 地址选择
     */
//    public void setListData(AddressDaoUtil addressDaoUtil,String addressType) {
//        mSelectionAdapter.setListData(addressDaoUtil,addressType);
//    }

    /**
     * 日期选择
     */
    public void setListData(GenerateDateUtils generateDateUtils, boolean isNeedDayForMonth, boolean needMoreYear){
        mSelectionAdapter.setListData(generateDateUtils,isNeedDayForMonth,needMoreYear);
    }
    private void initListener() {

    }




    @Override
    public void onItemClick(View view, ArrayList mAddresses) {
        if (mActivity instanceof SelectionDialogActivity) {
            mSelectionAdapter.notifyDataSetChanged();
            SelectionDialogActivity selectionDialogActivity = (SelectionDialogActivity) mActivity;
            selectionDialogActivity.confirmSelection(mAddresses);
        }
    }

    @Override
    public void onSelectItem(int pos, String text) {
        if (mActivity instanceof SelectionDialogActivity) {
            SelectionDialogActivity selectionDialogActivity = (SelectionDialogActivity) mActivity;
            selectionDialogActivity.onSelectItem(pos, text);
            mRecyclerView.scrollToPosition(0);
        }
    }

    public void onTabSelect(int position) {
        mSelectionAdapter.onTabSelect(position);
        mRecyclerView.scrollToPosition(0);
    }

}
