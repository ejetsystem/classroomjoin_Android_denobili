package com.classroomjoin.app.mySocialGradesPage

import com.google.gson.annotations.SerializedName
import java.util.*


class MySocialGradesResponse(@SerializedName("status")
                             val status:String,
                             @SerializedName("data")
                             val data:ArrayList<MySocialGradesModel>,
                             @SerializedName("error_code")
                             val error_code:String,
                             @SerializedName("error_message")
                             val error_response:String)
