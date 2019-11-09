package com.classroomjoin.app.myConnectedStudents


import com.google.gson.annotations.SerializedName
import java.util.*

class ConnectedStudentApiResponse {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("error_code")
    var error_code: String? = null

    @SerializedName("message")
    var error_message: String = ""

    @SerializedName("data")
    var data: ArrayList<MyConnectedStudentModel>? = null

}
