package com.denobili.app.teacherMessageSendPage

import com.google.gson.annotations.SerializedName


data class SmsSendModel(

        @SerializedName("accountManagementId")
        var acc_id: String?,

        @SerializedName("orgId")
        var orgid: String?,

        @SerializedName("message")
        val message: String,

        @SerializedName("classId")
        val classId: String?,

        @SerializedName("studentId")
        val studentid: List<Int>,

        @SerializedName("createDate")
        val createDate: String,

        @SerializedName("scheduleTime")
        val scheduleTime: String?


)