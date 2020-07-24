package com.denobili.app.teacherMessageSendPage

import com.google.gson.annotations.SerializedName


data class EmailSendModel(

        @SerializedName("subject")
        val subject: String,

        @SerializedName("message")
        val message: String,

        @SerializedName("orgId")
        var orgid: String?,

        @SerializedName("accountManagementId")
        var acc_id: String?,

        @SerializedName("studentId")
        val studentid: List<Int>,

        @SerializedName("attachmentId")
        val attachment_id: Int? = null,

        @SerializedName("classId")
        var classId: String?,

        @SerializedName("createDate")
        var createDate: String?,

        @SerializedName("scheduleTime")
        val scheduleTime: String?)