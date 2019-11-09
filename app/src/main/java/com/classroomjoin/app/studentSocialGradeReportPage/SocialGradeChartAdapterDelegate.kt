package com.classroomjoin.app.studentSocialGradeReportPage

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.DisplayItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_social_grade_chart.view.*
import org.jetbrains.anko.AnkoLogger
import java.util.*


class SocialGradeChartAdapterDelegate : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {


    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is SocialGradeChartModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_social_grade_chart, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as SocialGradeChartModel
        setData(holder.chart, item.positive/(item.positive+Math.abs(-item.negative)), Math.abs(-item.negative)/(item.positive+Math.abs(-item.negative)))
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val chart: PieChart

        init {
            chart = itemView.chart1
            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.highlightValues(null)

            val l = chart.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.setOrientation(Legend.LegendOrientation.VERTICAL)
            l.setDrawInside(false)
            l.setXEntrySpace(7f)
            l.setYEntrySpace(0f)
            l.setYOffset(0f)


        }


    }

    private fun setData(mChart: PieChart, positive: Float, negative: Float) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(PieEntry(positive, "Positive"))
        entries.add(PieEntry(negative, "Negative"))
        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.valueTextSize = 20f
        // add a lot of colors
        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#55A300"))
        colors.add(Color.parseColor("#9B1300"))
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTextColor(Color.WHITE)
        mChart.data = data
        // undo all highlights
        mChart.highlightValues(null)

        /* val description = Description()
         description.textColor = ColorTemplate.rgb("#000000")
         description.text = "positive percentage negative percentage "
         description.textSize = 12f
         description.textAlign=Paint.Align.RIGHT
         mChart.setDescription(description)*/

        mChart.invalidate()
    }


}