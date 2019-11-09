package com.classroomjoin.app.loginPage

import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject


open class TeacherLoginBean : RealmObject(), DisplayItem {

    var userId: Int? = null

    @SerializedName("teacherId")
    var teacherId: Int? = null

    @SerializedName("teachersAdminId")
    var teachersAdminId: Int? = null

}