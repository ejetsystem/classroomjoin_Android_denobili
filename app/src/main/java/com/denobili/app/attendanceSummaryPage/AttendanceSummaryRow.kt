package com.denobili.app.attendanceSummaryPage

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import org.jetbrains.anko.AnkoLogger


open class AttendanceSummaryRow : DisplayItem, RealmObject(), AnkoLogger {
    var roll_no: String? = null
    @SerializedName("studentName")
    var name: String? = null
    @SerializedName("studentId")
    var id: Int? = null
    @SerializedName("totalDays")
    var no_days: Int? = null
    @SerializedName("presentDays")
    var pre_days: Int? = null
    var class_id: Int? = null
    var account_id: Int? = null
    var month: String? = null
    var year: String? = null


    fun setQueryParams(classid: Int, accountid: Int, month: String, year: String) {
        this.class_id = classid
        this.account_id = accountid
        this.month = month
        this.year = year
    }

    fun getPercentage(): String =
            ((pre_days!!.toFloat() / (no_days!!.toFloat()) * 100.0)).toInt().toString() + "%"

}