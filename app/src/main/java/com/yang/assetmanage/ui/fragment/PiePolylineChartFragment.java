
package com.yang.assetmanage.ui.fragment;

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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.yang.assetmanage.utils.Constants;
import com.yang.assetmanage.utils.DensityUtil;
import com.yang.assetmanage.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;


public class PiePolylineChartFragment extends BaseFragment implements OnChartValueSelectedListener {

    private PieChart chart;

//    private Typeface tf;

    private LinearLayout mDataLl;

    private RecyclerView mRecyclerView;

    private RVAdapter<Asset> mAssetAdapter;

    RadioGroup mRadioGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_report_layout;
    }

    @Override
    protected void initView() {
        chart = findViewById(R.id.chart1);
        mDataLl = findViewById(R.id.report_ll);
        mRecyclerView = findViewById(R.id.asset_rv);
        mRadioGroup = findViewById(R.id.report_rg);
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
        for (int i = 0; i <= 12; i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(i+"月");
            mRadioGroup.addView(radioButton);
        }
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
    }

    private void itemAssetConvert(RVAdapter.ViewHolder vH, Asset item, int position) {
        View view = vH.getView(R.id.asset_main_date_rl);
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

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void setData() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        String billId = (String) SPUtil.getData(MyApplication.getInstance(), Constants.Sp.SP_KEY_BILL_ID, "");
        List<Asset> assets = DbUtils.getInstance().getAssetReportList(billId, null);
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

}
