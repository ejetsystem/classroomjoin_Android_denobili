package com.denobili.app.mystudentsPage

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyStudentModel() : DisplayItem, RealmObject() {
    var classid: String = ""

    @SerializedName("studentId")
    @PrimaryKey
    var id: Int = 0

    @SerializedName("firstName")
    var fname: String = ""

    @SerializedName("middleName")
    var mname: String = ""

    @SerializedName("lastName")
    var lname: String = ""

    @SerializedName("admissionNo")
    var admission_id: String = ""

    @SerializedName("rollNo")
    var roll_no: Int = 0

    @SerializedName("parentId")
    var parentId: Int = 0

    @SerializedName("parentMobile")
    var parentMobile: String = ""

    var isSelected: Boolean? = false

    var isListView: Boolean? = true


    constructor(classid: String,
                id: Int,
                first_name: String, middle_name: String, last_name: String,
                admission_id: String,
                roll_no: Int, listview: Boolean?) : this() {
        this.classid = classid
        this.id = id
        this.fname = first_name
        this.mname = middle_name
        this.lname = last_name
        this.admission_id = admission_id
        this.roll_no = roll_no
        this.isSelected = isSelected
        this.isListView = listview

    }
}

internal class Sortbyroll : Comparator<MyStudentModel> {
    // Used for sorting in ascending order of
    // roll number
    override fun compare(a: MyStudentModel, b: MyStudentModel): Int = a.roll_no - b.roll_no
}

internal class Sortbyname : Comparator<MyStudentModel> {
    // Used for sorting in ascending order of
    // roll name
    override fun compare(a: MyStudentModel, b: MyStudentModel): Int = a.fname.compareTo(b.fname)
}