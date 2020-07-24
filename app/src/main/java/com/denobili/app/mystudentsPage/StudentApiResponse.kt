package com.denobili.app.mystudentsPage


import com.google.gson.annotations.SerializedName
import java.util.*

class StudentApiResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("e_cod")
    var error_code: String? = null
    @SerializedName("e_msg")
    var error_message: String = ""
    @SerializedName("data")
    var data: ArrayList<MyStudentModel>? = null
}
