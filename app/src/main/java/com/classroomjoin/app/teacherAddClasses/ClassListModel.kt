package com.classroomjoin.app.teacherAddClasses

import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ClassListModel() : DisplayItem, RealmObject() {

    companion object ClassmodelFiels {
        val UseridKey: String = "account_id"
    }

    @SerializedName("className")
    var name: String = ""

    @SerializedName("classId")
    @PrimaryKey
    var id: Int = 0

    var account_id: String = ""

    @SerializedName("noOfStudents")
    var no_of_students: Int = 0

    @SerializedName("activeFlag")
    var active_flag = 1

    @SerializedName("classCode")
    var classCode: String = ""

    constructor(name: String, id: Int, userid: String, no_of_students: Int) : this() {
        this.name = name
        this.id = id
        this.account_id = userid
        this.no_of_students = no_of_students
    }

    fun getNoofStudents(): Int {
        /* val lists = MyStudentModel().query { realmQuery: RealmQuery<MyStudentModel> -> realmQuery.equalTo("classid", id.toString()) }*/
        return no_of_students

    }
}