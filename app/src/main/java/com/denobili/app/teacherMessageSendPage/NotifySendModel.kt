package com.denobili.app.teacherMessageSendPage

import com.google.gson.annotations.SerializedName


data class NotifySendModel(

        @SerializedName("communicationSubject")
        val subject: String,

        @SerializedName("communicationText")
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
        val classId: String?,

        @SerializedName("createDate")
        val createDate: String,

        @SerializedName("scheduleTime")
        val scheduleTime: String?

)