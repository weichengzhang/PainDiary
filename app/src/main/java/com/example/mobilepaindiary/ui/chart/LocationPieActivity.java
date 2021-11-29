package com.example.mobilepaindiary.ui.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.ActivityLocationPieBinding;
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
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class LocationPieActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final String TAG = "LocationPieActivity";

    private ActivityLocationPieBinding binding;

    private LocationPieViewModel viewModel;

    private PieChart chart;

    private String[] locations;

    protected Typeface tfRegular;
    protected Typeface tfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        binding = ActivityLocationPieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locations = getResources().getStringArray(R.array.location);
        viewModel = new ViewModelProvider(this).get(LocationPieViewModel.class);
        viewModel.getLocationCount();
        chart = binding.pcPieChart;

        //显示图标中的百分比
        chart.setUsePercentValues(true);
        //不显示详细信息
        chart.getDescription().setEnabled(false);
        //设置附加到图表的额外偏移（围绕图表视图）
        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        //旋转减速摩擦系数
        chart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间的文字
//        chart.setCenterText(generateCenterSpannableText());
        //设置中间的文字大小
        chart.setCenterTextSize(15f);
        //设置中间颜色
        chart.setDrawHoleEnabled(false);
        //设置中间颜色
        chart.setHoleColor(Color.WHITE);
        //Sets the color the transparent-circle should have.
        chart.setTransparentCircleColor(Color.WHITE);
        //设置透明度
        chart.setTransparentCircleAlpha(110);
        //设置中间元的半径
        chart.setHoleRadius(58f);
        //设置中间圆孔周围的透明度
        chart.setTransparentCircleRadius(61f);
        //是否显示中间圆孔中的文字
        chart.setDrawCenterText(true);
        //是否可以旋转
        chart.setRotationEnabled(true);
        //是否点击高亮
        chart.setHighlightPerTapEnabled(true);
        //Y轴动画
        chart.animateY(1400, Easing.EaseInOutQuad);

        chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        chart.setRotationAngle(0);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        // 设置右上角文字颜色
        chart.setEntryLabelColor(Color.WHITE);
        // 设置右上角文字大小
        chart.setEntryLabelTextSize(15f);
        startObserve();
    }
    private void startObserve() {
        viewModel.locationCounts.observe(this, counts -> {
            Log.e(TAG, counts.toString());
            float totalCount = 0f;
            for (Integer count : counts) {
                totalCount += count;
            }
            Log.e(TAG, "totalCount is : " + totalCount);
            ArrayList<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < counts.size(); i++) {
                entries.add(new PieEntry((float) counts.get(i) / totalCount * 100, locations[i]));
            }

            PieDataSet dataSet = new PieDataSet(entries, "AddTask3Fragment Location");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
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


            dataSet.setValueLinePart1OffsetPercentage(80.f);
            dataSet.setValueLinePart1Length(0.2f);
            dataSet.setValueLinePart2Length(0.4f);
            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.BLACK);
            data.setValueTypeface(tfLight);
            chart.setData(data);

            // undo all highlights
            chart.highlightValues(null);

            chart.invalidate();
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}