
package com.yang.assetmanage.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.RVAdapter;
import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.entity.SelectionDate;
import com.yang.assetmanage.ui.AssetListActivity;
import com.yang.assetmanage.ui.SelectionDialogActivity;
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.DensityUtil;
import com.yang.assetmanage.utils.SPUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PiePolylineChartFragment extends BaseFragment implements OnChartValueSelectedListener, RVAdapter.OnItemClickListener {

    private PieChart chart;

//    private Typeface tf;

    private LinearLayout mDataLl;

    private RecyclerView mRecyclerView;

    private RVAdapter<Asset> mAssetAdapter;

    RadioGroup mSelectorGroup;

    RadioGroup mTypeSelectorGroup;

    private TextView mYearSelector;

    private int mClickType;
    private RadioButton mExpendRb, mIncomeRb;
    /**
     * 日期类型
     */
    private final static int TYPE_DATE = 3;

    private String mSelectType;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_report_layout;
    }

    @Override
    protected void initView() {
        chart = findViewById(R.id.chart1);
        mDataLl = findViewById(R.id.report_ll);
        mRecyclerView = findViewById(R.id.asset_rv);
        mSelectorGroup = findViewById(R.id.report_rg);
        mYearSelector = findViewById(R.id.report_year_tv);
        mTypeSelectorGroup = findViewById(R.id.report_type_rg);
        mExpendRb = findViewById(R.id.report_expend_rb);
        mIncomeRb = findViewById(R.id.report_income_rb);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.setOnChartValueSelectedListener(this);
        chart.animateY(1400, Easing.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
        initRcy();
        initSelector();
        mDataLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initSelector() {
        mTypeSelectorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.report_expend_rb:
                        mSelectType = Constants.Normal.TYPE_EXPEND;
                        break;
                    case R.id.report_income_rb:
                        mSelectType = Constants.Normal.TYPE_INCOME;
                        break;
                }
                initData();
            }
        });
        mExpendRb.setChecked(true);
        for (int i = 1; i <= 12; i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(mContext, 50), ViewGroup.LayoutParams.MATCH_PARENT));
            radioButton.setBackgroundResource(R.drawable.report_selector_bg);
            radioButton.setButtonDrawable(null);
            radioButton.setGravity(Gravity.CENTER);
//            radioButton.setTextColor(mContext.getResources().getColor(R.color.color_rb_selector));
            radioButton.setText(i + "月");
            radioButton.setId(i);
            mSelectorGroup.addView(radioButton);
        }
        mYearSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickType = TYPE_DATE;
                Intent intent = new Intent();
                intent.setClass(getActivity(), SelectionDialogActivity.class);
                intent.putExtra(SelectionDialogActivity.TYPE_INTENT_TYPE, SelectionDialogActivity.DATE);
                intent.putExtra(Constants.Normal.TYPE_SELECT_YEAR_MONTH, false);
                startActivityForResult(intent, 1);
                (getActivity()).overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out);

            }
        });
        mSelectorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initData();
            }
        });
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        mSelectorGroup.check(month);
    }


    private void initRcy() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAssetAdapter = new RVAdapter<Asset>(getActivity(), R.layout.item_asset_layout) {
            @Override
            protected void convert(ViewHolder vH, Asset item, int position) {
                itemAssetConvert(vH, item, position);
            }
        };
        mRecyclerView.setAdapter(mAssetAdapter);
        mAssetAdapter.setOnItemClickListener(this);
    }

    private void itemAssetConvert(RVAdapter.ViewHolder vH, Asset item, int position) {
        View view = vH.getView(R.id.asset_main_date_rl);
        vH.setVisibility(R.id.asset_main_member_edt, View.GONE);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 40)));
        vH.setText(R.id.asset_main_type_tv, item.getMoneyName());
//        vH.setText(R.id.asset_main_date_tv, item.getCreteData());
        vH.setVisibility(R.id.asset_main_date_tv, View.GONE);
        vH.setText(R.id.asset_main_money_edt, "共" + item.getMoney() + "元");
    }

    @Override
    protected void initData() {
        setData();
    }

    private void setData() {
        int checkedId = mSelectorGroup.getCheckedRadioButtonId();
        String year = mYearSelector.getText().toString().replace("年", "");
        ArrayList<PieEntry> entries = new ArrayList<>();

        String billId = (String) SPUtil.getData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
        List<Asset> assets = DbUtils.getInstance().getAssetReportList(mSelectType, billId, year, checkedId);
        for (int i = 0; i < assets.size(); i++) {
            Asset asset = assets.get(i);
            entries.add(new PieEntry(Float.parseFloat(asset.getMoney()), asset.getMoneyName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(tf);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
        mAssetAdapter.clear();
        mAssetAdapter.addAll(assets);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onBaseEvent(Object event) {
        if (event instanceof String) {
            String ev = (String) event;
            if (TextUtils.equals(Constants.Event.EVENT_ADD_ASSET_SUCCESS, ev)) {
                initData();
            }
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList obj = (ArrayList) data.getSerializableExtra(SelectionDialogActivity.TYPE_INTENT_SELECTION);
            switch (mClickType) {
                case TYPE_DATE:
                    handleDate(obj);
                    break;

            }

        }
    }

    private void handleDate(ArrayList list) {
        SelectionDate selectionDate = (SelectionDate) list.get(0);
        mYearSelector.setText(selectionDate.getName());
        initData();
    }

    @Override
    public void onItemClick(View view, int position) {
        Asset asset = mAssetAdapter.getData(position);
        int date = mSelectorGroup.getCheckedRadioButtonId();
        String year = mYearSelector.getText().toString().replace("年", "");
        String billId = (String) SPUtil.getData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
        Intent intent = new Intent(mContext, AssetListActivity.class);
        intent.putExtra(Constants.Intent.INTENT_KEY_YEAR, year);
        intent.putExtra(Constants.Intent.INTENT_KEY_BILL_ID, billId);
        intent.putExtra(Constants.Intent.INTENT_KEY_TYPE_ID, asset.getMoneyTypeId());
        intent.putExtra(Constants.Intent.INTENT_KEY_TYPE, mSelectType);
        intent.putExtra(Constants.Intent.INTENT_KEY_DAY, date);
        mContext.startActivity(intent);
    }
}
