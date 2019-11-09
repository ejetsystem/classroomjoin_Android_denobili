package com.classroomjoin.app.studentDiaryPage

import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.OfflineAttachmentModel
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


open class StudentDiaryEventModel() : DisplayItem, RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("eventTypeId")
    var eventTypeId: String? = ""

    @SerializedName("senderName", alternate = arrayOf("teacherName"))
    var teacher_name: String? = null

    @SerializedName(value = "diaryText", alternate = arrayOf("textAll"))
    var message: String? = ""

    @SerializedName("diarySubject", alternate = arrayOf("subject"))
    var subject: String? = ""

    @SerializedName("createDate")
    var date: String? = ""

    var month_date: String? = null

    @SerializedName("statusRead")
    var statusRead: Int = 0

    var type: String? = null

    var student_id: String? = ""

    @SerializedName("attachmentResponseBean")
    var attachments: RealmList<OfflineAttachmentModel>? = null
}


internal class SortbyDateDiary : Comparator<StudentDiaryEventModel> {

    override fun compare(a: StudentDiaryEventModel, b: StudentDiaryEventModel): Int = when {
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