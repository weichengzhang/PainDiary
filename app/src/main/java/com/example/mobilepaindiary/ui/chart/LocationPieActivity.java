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

        //???????????????????????????
        chart.setUsePercentValues(true);
        //?????????????????????
        chart.getDescription().setEnabled(false);
        //????????????????????????????????????????????????????????????
        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        //????????????????????????
        chart.setDragDecelerationFrictionCoef(0.95f);
        //?????????????????????
//        chart.setCenterText(generateCenterSpannableText());
        //???????????????????????????
        chart.setCenterTextSize(15f);
        //??????????????????
        chart.setDrawHoleEnabled(false);
        //??????????????????
        chart.setHoleColor(Color.WHITE);
        //Sets the color the transparent-circle should have.
        chart.setTransparentCircleColor(Color.WHITE);
        //???????????????
        chart.setTransparentCircleAlpha(110);
        //????????????????????????
        chart.setHoleRadius(58f);
        //????????????????????????????????????
        chart.setTransparentCircleRadius(61f);
        //????????????????????????????????????
        chart.setDrawCenterText(true);
        //??????????????????
        chart.setRotationEnabled(true);
        //??????????????????
        chart.setHighlightPerTapEnabled(true);
        //Y?????????
        chart.animateY(1400, Easing.EaseInOutQuad);

        chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        chart.setRotationAngle(0);

        // chart.setUnit(" ???");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        // ???????????????????????????
        chart.setEntryLabelColor(Color.WHITE);
        // ???????????????????????????
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