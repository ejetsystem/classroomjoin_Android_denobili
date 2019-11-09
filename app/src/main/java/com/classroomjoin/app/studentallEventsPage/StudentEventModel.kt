package com.classroomjoin.app.studentallEventsPage

import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.OfflineAttachmentModel
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

open class StudentEventModel() : DisplayItem, RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("teacherName")
    var teacher_name: String? = ""

    @SerializedName(value = "textAll", alternate = arrayOf("communicationText"))
    var message: String? = ""

    @SerializedName(value = "subject", alternate = arrayOf("communicationSubject"))
    var subject: String? = ""

    @SerializedName(value = "createDate", alternate = arrayOf("sendingDate"))
    var date: String? = ""

    @SerializedName("updateDate")
    var date_alt: String? = ""

    @SerializedName("statusRead")
    var statusRead: Int?= 0

    @SerializedName("eventTypeId")
    var event_type: Int = 0

    @SerializedName("statusFlag")
    var status_flag: Int = 2

    @SerializedName("studentGrade")
    var grade: String? = null

    var student_id: String? = ""

    @SerializedName("attachmentResponseBean")
    var attachments: RealmList<OfflineAttachmentModel>? = null

    var month_date: String? = null

    var type: String? = null


    constructor(id: Int,
                teacher_name: String,
                message: String,
                subject: String,
                date: String,
                event_type: Int,
                status_flag: Int,
                grade: String,
                student_id: String,
                attachments: RealmList<OfflineAttachmentModel>) : this() {
        this.id = id
        this.teacher_name = teacher_name
        this.message = message
        this.subject = subject
        this.date = date
        this.event_type = event_type
        this.status_flag = status_flag
        this.grade = grade
        this.student_id = student_id
        this.attachments = attachments
    }


}

internal class SortbyDateStudent : Comparator<StudentEventModel> {

    override fun compare(a: StudentEventModel, b: StudentEventModel): Int = when {
        getDateDetails(a.date, 1) != getDateDetails(b.date, 1) -> getDateDetails(b.date, 1) - getDateDetails(a.date, 1)
        getDateDetails(a.date, 2) != getDateDetails(b.date, 2) -> getDateDetails(b.date, 2) - getDateDetails(a.date, 2)
        getDateDetails(a.date, 3) != getDateDetails(b.date, 3) -> getDateDetails(b.date, 3) - getDateDetails(a.date, 3)
        getDateDetails(a.date, 4) != getDateDetails(b.date, 4) -> getDateDetails(b.date, 4) - getDateDetails(a.date, 4)
        getDateDetails(a.date, 5) != getDateDetails(b.date, 5) -> getDateDetails(b.date, 5) - getDateDetails(a.date, 5)
        else -> getDateDetails(b.date, 6) - getDateDetails(a.date, 6)
    }
}

fun getDateDetails(input_date: String?, type: Int?): Int {

    // val input_date = "Mar 10, 2016 6:30:00 PM"
    val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
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
    } else if (type == 6) {
        val format2 = SimpleDateFormat("ss", Locale.getDefault())
        //System.out.println("Data--->" + "onNext mm-->" + format2.format(dt1).toInt())
        return format2.format(dt1).toInt()
    }
    return -1

}