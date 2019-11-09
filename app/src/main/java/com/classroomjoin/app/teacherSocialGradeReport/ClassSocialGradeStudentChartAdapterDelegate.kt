package com.classroomjoin.app.teacherSocialGradeReport

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import kotlinx.android.synthetic.main.row_social_grade_chart_student.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick
import java.util.*


class ClassSocialGradeStudentChartAdapterDelegate : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {


    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is StudentSummarySocialGrade
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_social_grade_chart_student, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as StudentSummarySocialGrade
        setData(holder.chart, item.positive/(item.positive+Math.abs(-item.negative)), Math.abs(-item.negative)/(item.positive+Math.abs(-item.negative)))

        holder.name.text = item.name

        holder.itemView.onClick {
            if (holder.chart.visibility == View.GONE) holder.chart.visibility = View.VISIBLE
            else holder.chart.visibility = View.GONE
        }


    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val chart: PieChart
        internal val name: TextView

        init {
            chart = itemView.chart1
            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.highlightValues(null)
            name = itemView.summary_name

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
        val dataSet = PieDataSet(entries, "Social Grades")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
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
        mChart.invalidate()
    }


}