package com.classroomjoin.app.teacherAddClasses


import com.google.gson.annotations.SerializedName
import java.util.*

class ClassListApiResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("error_code")
    var error_code: String? = null
    @SerializedName("error_message")
    var error_message: String = ""
    @SerializedName("data")
    val data: ArrayList<ClassListModel>? = null
}
