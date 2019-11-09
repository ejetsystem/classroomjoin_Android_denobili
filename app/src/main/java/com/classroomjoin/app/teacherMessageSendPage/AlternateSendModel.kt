package com.classroomjoin.app.teacherMessageSendPage

import com.google.gson.annotations.SerializedName


data class AlternateSendModel(

        @SerializedName("accountManagementId")
        var acc_id: String?,

        @SerializedName("orgId")
        var orgid: String?,

        @SerializedName("message")
        val message: String,

        @SerializedName("classId")
        val classId: String?,

        @SerializedName("parentId")
        val parentId: List<Int>,

        @SerializedName("createDate")
        val createDate: String,

        @SerializedName("scheduleTime")
        val scheduleTime: String?


)