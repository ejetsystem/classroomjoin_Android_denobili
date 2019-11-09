package com.classroomjoin.app.teacherCommunicationReports

import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.text.SimpleDateFormat
import java.util.*


open class ReportModel() : DisplayItem, RealmObject() {

    @SerializedName("studentName")
    var student_name: String? = null

    @SerializedName("className")
    var className: String? = null

    @SerializedName("createDate")
    var date: String? = null

    @SerializedName(value = "textall", alternate = arrayOf("message"))
    var message: String? = null

    @SerializedName("communicationText")
    var message1: String? = null

    @SerializedName("statusRead")
    var statusRead: Int? = 0

    @SerializedName("status")
    var status: String? = null

    var type_id: String? = null
    var class_id: String? = null

    constructor(student_name: String, date: String, message: String, type_id: String, class_id: String) : this() {
        this.student_name = student_name
        this.date = date
        this.message = message
        this.type_id = type_id
        this.class_id = class_id
    }

    constructor(student_name: String, date: String, message: String) : this() {
        this.student_name = student_name
        this.date = date
        this.message = message
        this.type_id = type_id
        this.class_id = class_id
    }


}

internal class SortbyDate : Comparator<ReportModel> {

    override fun compare(a: ReportModel, b: ReportModel): Int = when {
        getDateDetails(a.date, 1) != getDateDetails(b.date, 1) -> getDateDetails(b.date, 1) - getDateDetails(a.date, 1)
        getDateDetails(a.date, 2) != getDateDetails(b.date, 2) -> getDateDetails(b.date, 2) - getDateDetails(a.date, 2)
        getDateDetails(a.date, 3) != getDateDetails(b.date, 3) -> getDateDetails(b.date, 3) - getDateDetails(a.date, 3)
        getDateDetails(a.date, 4) != getDateDetails(b.date, 4) -> getDateDetails(b.date, 4) - getDateDetails(a.date, 4)
        else -> getDateDetails(b.date, 5) - getDateDetails(a.date, 5)
    }
}

fun getDateDetails(input_date: String?, type: Int?): Int {

    // val input_date = "Mar 10, 2016 6:30:00 PM"
    val format1 = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault())
    System.out.println("Data--->" + "getDateDetails-->" + input_date)

    val dt1 = format1.parse(input_date)
    if (type == 1) {
        val format2 = SimpleDateFormat("yyyy", Locale.getDefault())
        //System.out.println("Data--->" + "onNext yyyy-->" + format2.format(dt1).toInt())
        return format2.format(dt1).toInt()
    } else if (type == 2) {
        val format2 = SimpleDateFormat("MM", Locale.getDefault())
        // System.out.println("Data--->" + "onNext MM-->" + format2.format(dt1).toInt())
        return format2.format(dt1).toInt()
    } else if (type == 3) {
        val format2 = SimpleDateFormat("dd", Locale.getDefault())                //System.out.println("Data--->"+"onNext list size-->"+lists.size)
        //System.out.println("Data--->" + "onNext dd-->" + format2.format(dt1).toInt())
        return format2.format(dt1).toInt()
    } else if (type == 4) {
        val format2 = SimpleDateFormat("HH", Locale.getDefault())
        //System.out.println("Data--->" + "onNext hh-->" + format2.format(dt1).toInt())
        return format2.format(dt1).toInt()
    } else if (type == 5) {
        val format2 = SimpleDateFormat("mm", Locale.getDefault())
        //System.out.println("Data--->" + "onNext mm-->" + format2.format(dt1).toInt())
        return format2.format(dt1).toInt()
    }
    return -1

}
