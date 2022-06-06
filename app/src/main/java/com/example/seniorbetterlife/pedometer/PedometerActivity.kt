package com.example.seniorbetterlife.pedometer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.databinding.ActivityPedometerBinding
import com.example.seniorbetterlife.util.DateFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.math.roundToInt

class PedometerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedometerBinding
    val viewModel: PedometerViewModel by viewModels()

    private lateinit var currentDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedometerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentDate = DateFormatter.getDate()

        viewModel.listOfDailySteps.observe(this) { listOfDailySteps ->
            listOfDailySteps.let {
                listOfDailySteps.filter { it.day == DateFormatter.getDate() }[0].steps.toString()

                if (listOfDailySteps.isNotEmpty()) {
                    val dailySteps = listOfDailySteps.filter { it.day == currentDate }
                    if (dailySteps.isNotEmpty()) {
                        setupDoughnutChart(dailySteps[0])
                        setUpInfo(dailySteps[0])
                    }
                }
                setupColumnChart(listOfDailySteps.sortedBy { it.day })
            }
        }


    }

    private fun setUpInfo(dailySteps: DailySteps) {
        binding.tvCountOfSteps.text = dailySteps.steps.toString()

        val oneStepKilometers = 0.00064f
        val oneStepCalories = 0.04f

        val kilometers: Float = dailySteps.steps * oneStepKilometers * 100
        val roundedKilometer = kilometers.roundToInt()
        val kilometerDouble = roundedKilometer / 100.0
        val calories: Float = dailySteps.steps * oneStepCalories
        val roundedCalories = calories.roundToInt()

        binding.tvAchievedDistance.text = "$kilometerDouble km"
        binding.tvBurnedCalories.text = "$roundedCalories [kcal]"
    }

    private fun setupDoughnutChart(dailySteps: DailySteps) {

        //initialize reference
        val pieChart = binding.pieChart

        val currentSteps = dailySteps.steps.toFloat()

        //add data
        val dataEntries = mutableListOf<PieEntry>()
        dataEntries.add(PieEntry(currentSteps, "Zrobione"))
        dataEntries.add(PieEntry((6000f - currentSteps), "Do zrobienia"))

        //set colors
        val colors = arrayListOf(Color.parseColor("#3FD727"), Color.parseColor("#E52020"))

        val set = PieDataSet(dataEntries, "")
        val data = PieData(set)
        val percentageProgress = (currentSteps / 6000f * 100.0)
        set.valueTextColor = Color.BLACK
        set.colors = colors
        pieChart.data = data
        pieChart.data.setValueTextColor(Color.BLACK)
        pieChart.description.text = ""
        pieChart.centerText = "${percentageProgress.roundToInt()}%"
        pieChart.invalidate() // refresh

    }

    private fun setupColumnChart(listOfDailySteps: List<DailySteps>) {
        //initialize reference
        val barChart = binding.barChart

        val entries = arrayListOf<BarEntry>()
        val labels = arrayListOf<String>()
        var initializingPosition = 0f

        for(dailySteps in listOfDailySteps){
            entries.add(BarEntry(initializingPosition,dailySteps.steps.toFloat()))
            labels.add(dailySteps.day)
            initializingPosition++
        }

        val barDataSet = BarDataSet(entries,"Dzienny wykres kroków")
        barDataSet.setColors(Color.GREEN)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f

        val barData = BarData(barDataSet)

        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = ""
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.animateY(1000)

    }
}