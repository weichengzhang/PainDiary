package com.example.mobilepaindiary.ui.chart;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.ActivityPainLineBinding;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PainLineActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "PainLineActivity";

    private ActivityPainLineBinding binding;

    private PainLineViewModel viewModel;

    private LineChart chart;

    private DatePickerDialog datePickerDialog;

    private int timeFlag = 0;

    private Date startTime;

    private Date endTime;

    private List<Date> timeList = new ArrayList<>();
    private List<Float> temperatureList = new ArrayList<>();
    private List<Float> humidityList = new ArrayList<>();
    private List<Float> pressureList = new ArrayList<>();

    private String[] weathers;

    /**
     * 0 温度 1 湿度 2 压力
     */
    private int weatherFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weathers = getResources().getStringArray(R.array.weather);
        binding = ActivityPainLineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PainLineViewModel.class);
        chart = binding.cl;
        chart.setLogEnabled(true);
        binding.spWeather.setOnItemSelectedListener(this);
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        binding.tvStartTime.setOnClickListener(v -> {
            if (!datePickerDialog.isShowing()) {
                timeFlag = 1;
                datePickerDialog.show();
            }
        });
        binding.tvEndTime.setOnClickListener(v -> {
            if (!datePickerDialog.isShowing()) {
                timeFlag = 2;
                datePickerDialog.show();
            }
        });
        binding.btnStartQuery.setOnClickListener(v -> {
            if (startTime == null) {
                Toast.makeText(PainLineActivity.this, "Please select start time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endTime == null) {
                Toast.makeText(PainLineActivity.this, "Please select end time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!startTime.before(endTime)) {
                Toast.makeText(PainLineActivity.this, "Time selection error", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.findByTime(startTime, endTime);
            }


        });


        chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY);


        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        startObserve();
    }


    private void startObserve() {
        viewModel.allRecord.observe(this, painRecords -> {
            if (painRecords != null) {
                if (!painRecords.isEmpty()) {
                    runOnUiThread(() -> {
                        setView(painRecords);
                        chart.postInvalidate();
                    });
                } else {
                    Toast.makeText(PainLineActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PainLineActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setView(List<PainRecord> painRecords) {
        timeList.clear();
        temperatureList.clear();
        humidityList.clear();
        pressureList.clear();


        ArrayList<Entry> values1 = new ArrayList<>();
        for (int i = 0; i < painRecords.size(); i++) {
            PainRecord record = painRecords.get(i);
            values1.add(new Entry(i, record.painLevel));
            timeList.add(record.currentTime);
            temperatureList.add(Float.parseFloat(record.currentTemperature));
            humidityList.add(Float.parseFloat(record.currentHumidity));
            pressureList.add(Float.parseFloat(record.currentPressure));
        }
        Collections.sort(timeList);
        Collections.sort(temperatureList);
        Collections.sort(humidityList);
        Collections.sort(pressureList);
        Log.e(TAG, "temperatureList : " + temperatureList.toString());
        Log.e(TAG, "humidityList : " + humidityList.toString());
        Log.e(TAG, "pressureList : " + pressureList.toString());
        for (Date date : timeList) {
            Log.e(TAG, "setView: " + new SimpleDateFormat("yyyy-MM-dd").format(date));
        }


        ArrayList<Entry> values2 = new ArrayList<>();
        YAxis rightAxis = chart.getAxisRight();
        switch (weatherFlag) {
            case 0:
                //温度
                rightAxis.setAxisMaximum(temperatureList.get(temperatureList.size() - 1));
                rightAxis.setAxisMinimum(temperatureList.get(0));
                for (int i = 0; i < timeList.size(); i++) {
                    values2.add(new Entry(i, temperatureList.get(i)));
                }
                break;
            case 2:
                //湿度
                rightAxis.setAxisMaximum(humidityList.get(temperatureList.size() - 1));
                rightAxis.setAxisMinimum(humidityList.get(0));
                for (int i = 0; i < timeList.size(); i++) {
                    values2.add(new Entry(i, humidityList.get(i)));
                }
                break;
            default:
                rightAxis.setAxisMaximum(pressureList.get(temperatureList.size() - 1));
                rightAxis.setAxisMinimum(pressureList.get(0));

                for (int i = 0; i < timeList.size(); i++) {
                    values2.add(new Entry(i, pressureList.get(i)));
                }
                //压力
                break;
        }

        XAxis xAxis = chart.getXAxis();
        Log.e(TAG, "weatherFlag " + weatherFlag);
        Map<Integer, String> xMap = new HashMap<>();
        List<String> collect = timeList.stream().map(date -> new SimpleDateFormat("yyyy-MM-dd").format(date)).collect(Collectors.toList());
        for (int i = 0; i < values1.size(); i++) {
            xMap.put((int) values1.get(i).getX(), collect.get(i));
        }
        //自定义x轴标签数据
        xAxis.setValueFormatter((value, axis) -> xMap.get((int) value));

        LineDataSet set1, set2;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set2.setLabel(weathers[weatherFlag]);
            set1.setEntries(values1);
            set2.setEntries(values2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values1, "AddTask3Fragment level");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);

            // create a dataset and give it a type
            set2 = new LineDataSet(values2, weathers[weatherFlag]);
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));

            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            chart.setData(data);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar instance = Calendar.getInstance();

        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        switch (timeFlag) {
            case 1:
                startTime = instance.getTime();
                binding.tvStartTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
                break;
            case 2:
                endTime = instance.getTime();
                binding.tvEndTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(endTime));
                break;
            default:
                break;
        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //切换选择
        Log.e(TAG, position + "");
        weatherFlag = position;


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}