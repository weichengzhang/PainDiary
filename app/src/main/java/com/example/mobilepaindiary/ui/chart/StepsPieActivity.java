package com.example.mobilepaindiary.ui.chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.databinding.ActivityStepsPieBinding;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class StepsPieActivity extends AppCompatActivity {

    private ActivityStepsPieBinding binding;

    private StepsPieViewModel viewModel;
    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepsPieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(StepsPieViewModel.class);
        viewModel.getTodayPainRecord();
        chart = binding.pcPieChart;
        //显示图标中的百分比
        chart.setUsePercentValues(false);
        //不显示详细信息
        chart.getDescription().setEnabled(false);
        //设置附加到图表的额外偏移（围绕图表视图）
        chart.setExtraOffsets(5, 10, 5, 5);
        //旋转减速摩擦系数
        chart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间的文字大小
        chart.setCenterTextSize(15f);
        //设置中间颜色
        chart.setDrawHoleEnabled(true);
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
        //返回图表的图例对象
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // 设置右上角文字颜色
        chart.setEntryLabelColor(Color.WHITE);
        // 设置右上角文字大小
        chart.setEntryLabelTextSize(15f);
        startObserve();
    }

    private void startObserve() {
        viewModel.todayPainRecord.observe(this, new Observer<PainRecord>() {
            @Override
            public void onChanged(PainRecord painRecord) {
                if (painRecord != null) {
                    int targetSteps = Integer.parseInt(painRecord.targetSteps);
                    int currentSteps = Integer.parseInt(painRecord.currentSteps);
                    //设置中间的文字
                    chart.setCenterText("Target Steps : " + targetSteps);
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    if (targetSteps - currentSteps == 0) {
                        entries.add(new PieEntry(targetSteps, "Current"));
                    } else {
                        entries.add(new PieEntry(targetSteps - currentSteps, "Remaining"));
                        entries.add(new PieEntry(currentSteps, "Current"));
                    }
                    PieDataSet dataSet = new PieDataSet(entries, "Steps");
                    ArrayList<Integer> colors = new ArrayList<>();

//                    for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                        colors.add(c);
//
//                    for (int c : ColorTemplate.JOYFUL_COLORS)
//                        colors.add(c);

                    for (int c : ColorTemplate.COLORFUL_COLORS)
                        colors.add(c);

                    for (int c : ColorTemplate.LIBERTY_COLORS)
                        colors.add(c);

                    for (int c : ColorTemplate.PASTEL_COLORS)
                        colors.add(c);

                    colors.add(ColorTemplate.getHoloBlue());

                    dataSet.setColors(colors);

                    //是否显示图标
                    dataSet.setDrawIcons(false);
                    //设置dp中piechart切片之间的剩余空间。
                    dataSet.setSliceSpace(3f);
                    //设置偏移量
                    dataSet.setIconsOffset(new MPPointF(0, 40));
                    //设置此数据集高亮显示的饼图切片的距离
                    dataSet.setSelectionShift(5f);
                    //设置颜色
                    dataSet.setColors(colors);
                    //设置数据
                    PieData data = new PieData(dataSet);
                    //设置
//                    data.setValueFormatter(new PercentFormatter());
                    //设置字体大小
                    data.setValueTextSize(11f);
                    //设置提提颜色
                    data.setValueTextColor(Color.BLACK);
                    //设置数据
                    chart.setData(data);
                    // 设置默认高亮
                    chart.highlightValues(null);
                    //刷新饼图
                    chart.invalidate();
                }
            }
        });
    }
}