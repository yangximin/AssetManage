package com.yang.assetmanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.entity.Dicts;
import com.yang.assetmanage.entity.SelectionDate;
import com.yang.assetmanage.utils.GenerateDateUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择适配器
 * Created by YXM
 * on 2018/6/1.
 */

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder> {

    private ListDialogListener mListDialogListener;

    private List mListData, mFirstListData;

    private List mSecondListData;

    private List mThirdListData;
    /**
     * 常规选择
     */
    private final int TYPE_NORMAL = 0;
    /**
     * 地址选择
     */
    private final int TYPE_ADDRESS = 1;
    /**
     * 日期选择
     */
    private final int TYPE_DATE = 2;
    /**
     * 字典选择
     */
    private final int TYPE_DICT = 3;

    private Context mContext;
    /**
     * 当前选择的类型：1省份，2市，3区
     */
    private int mOptions = 1;
    /**
     * 记录索引-省市区中用户选择对应的index
     */
    private int mFirstIndex, mSecondIndex;
    /**
     * 选择的结果集合
     */
    private ArrayList mResultList = new ArrayList<>();


    private GenerateDateUtils mGenerateDateUtils;

    private boolean isNeedDayForMonth;
    private String mAddressType;

    private String mDistType;

    private final long clickInterval = 300;

    private long clickCurrentTime;

    private int clickCount;
    private boolean mNeedMoreYear;

    public SelectionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public SelectionAdapter(Context context, ArrayList listData) {
        this.mListData = listData;
        this.mContext = context;
    }

    public SelectionAdapter(Context context, ArrayList firstListData, ArrayList secondListData, ArrayList thirdListData) {
        this.mListData = firstListData;
        this.mSecondListData = secondListData;
        this.mThirdListData = thirdListData;
        this.mContext = context;
    }

    /**
     * 常规选择
     */
    public void setListData(List<Dicts> dicts) {
        this.mListData = dicts;
        notifyDataSetChanged();
    }

    /**
     * 常规选择
     */
//    public void setDictChoseId(String type, String choseId) {
//        mDistType = type;
//        this.mListData = mFirstListData = getDictList(choseId);
//        notifyDataSetChanged();
//    }


    /**
     * 日期选择
     */
    public void setListData(GenerateDateUtils generateDateUtils, boolean isNeedDayForMonth, boolean needMoreYear) {

        if (needMoreYear) {
            this.mListData = this.mFirstListData = generateDateUtils.getMoreYearList();
        } else {
            this.mListData = this.mFirstListData = generateDateUtils.getYearList();
        }

        mNeedMoreYear = needMoreYear;
        this.isNeedDayForMonth = isNeedDayForMonth;
        mGenerateDateUtils = generateDateUtils;
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.zac_loan_apply_selection_item_layout, parent, false);
        return new SelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectionViewHolder holder, int position) {
        holder.bindView(mListData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }


    public void setListDialogListener(ListDialogListener mListDialogListener) {
        this.mListDialogListener = mListDialogListener;
    }


    class SelectionViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;
        private Object mObject;

        SelectionViewHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.item_selection_tv);
        }

        void bindView(Object object, int position) {
            mObject = object;
            int type = getObjType(object);
            if (type == TYPE_DATE) {
                SelectionDate selectionDate = (SelectionDate) object;
                mNameTv.setText(selectionDate.getName());
            } else {
                Dicts dicts = (Dicts) object;
                mNameTv.setText(dicts.getName());
            }
            setListener(position);
        }

        private void setListener(final int pos) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - clickCurrentTime > clickInterval) {
                        itemClick(v, pos);
                        clickCurrentTime = System.currentTimeMillis();
                    }
                }
            });
        }

        private void itemClick(View v, int pos) {
            int type = getObjType(mObject);
            //日期选择
            if (type == TYPE_DATE) {
                chooseDate(v, pos);
            } else if (type == TYPE_DICT) {
                chooseDict(v, pos);
            }
        }

        private int getObjType(Object obj) {
            if (obj instanceof SelectionDate) {
                return TYPE_DATE;
            } else if (obj instanceof Dicts) {
                return TYPE_DICT;
            } else {
                return TYPE_NORMAL;
            }
        }

        private void chooseDict(View v, int pos) {
            Dicts dicts = (Dicts) mObject;
//            if (mOptions == 1) {                //单项选择
//                if (TextUtils.equals(mDistType, DEFINE_COMPONENT.COMPONENT_TYPE.SELECT) ||
//                        TextUtils.equals(mDistType, DEFINE_COMPONENT.COMPONENT_TYPE.SELECT_L1)) {
//                    mListData = null;
//                } else {
//                    mListData = mSecondListData = DictsDaoUtil.getDictsList(dicts.getCode());
//                }
//            } else if (mOptions == 2) {//双项选择
//                if (TextUtils.equals(mDistType, DEFINE_COMPONENT.COMPONENT_TYPE.SELECT_L2)) {
//                    mListData = null;
//                } else {
//                    mListData = mThirdListData = DictsDaoUtil.getDictsList(dicts.getCode());
//                }
//            } else if (mOptions == 3) {
//                mListData = null;
//            }
            mListData = null;
            addResultList(dicts);
            if (mListData == null) {
                mListDialogListener.onItemClick(v, mResultList);
            } else {
                notifyDataSetChanged();
                mListDialogListener.onSelectItem(mOptions, dicts.getName() + "");
                mOptions++;
            }
        }

        /**
         * 选择日期
         */
        private void chooseDate(View v, int pos) {
            SelectionDate address = (SelectionDate) mObject;
            //年
            if (mOptions == 1) {

                if (mNeedMoreYear) {
                    mListData = mSecondListData = mGenerateDateUtils.getMoreMonthList();
                } else {
                    mListData = mSecondListData = mGenerateDateUtils.getMonthList(address.getDate());
                }

            }
            //月
            else if (mOptions == 2) {
                //不需要天数
                if (!isNeedDayForMonth) {
                    mListData = null;
                } else {
                    int year = ((SelectionDate) mResultList.get(0)).getDate();
                    int month = address.getDate();

                    if (mNeedMoreYear) {
                        mListData = mThirdListData = mGenerateDateUtils.getDayForMothList(year, month);
                    } else {
                        mListData = mThirdListData = mGenerateDateUtils.getDayForMoth(year, month);
                    }

                }
            }
            //日
            else if (mOptions == 3) {
                mListData = null;
            }
            addResultList(address);
            if (mListData == null) {
                mListDialogListener.onItemClick(v, mResultList);
            } else {
                notifyDataSetChanged();
                mListDialogListener.onSelectItem(mOptions, address.getName() + "");
                mOptions++;
            }
        }
    }

    private void addResultList(Object item) {
        mResultList.add(item);
    }


    /**
     * 点击Tab切换数据
     */
    public void onTabSelect(int position) {
        if (mFirstListData == null || mSecondListData == null) {
            return;
        }
        //第一项
        if (position == 0) {
            mOptions = 1;
            mListData = mFirstListData;
            mResultList.clear();
        }
        //第二项
        else if (position == 1) {
            mOptions = 2;
            mListData = mSecondListData;
            mResultList.remove(mResultList.size() - 1);
        }
        mListDialogListener.onSelectItem(position, null);
        notifyDataSetChanged();
    }

    /**
     * Item点击回调
     */
    public interface ListDialogListener {

        void onItemClick(View view, ArrayList addresses);

        void onSelectItem(int pos, String text);
    }


}
