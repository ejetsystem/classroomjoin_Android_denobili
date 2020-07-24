package com.denobili.app.teacherMessageSendPage

import com.google.gson.annotations.SerializedName


data class DiarySendModel(
        @SerializedName("diarySubject")
        val subject: String,
        @SerializedName("diaryText")
        val message: String,
        @SerializedName("orgId")
        var orgid: String?,
        @SerializedName("accountManagementId")
        var acc_id: String?,
        @SerializedName("idEventType")
        val eventType: Int,
        @SerializedName("studentIdList")
        val studentid: List<Int>,
        @SerializedName("attachmentId")
        val attachment_id: Int? = null,
        @SerializedName("classId")
        var classId: String?,
        @SerializedName("createDate")
        var createDate: String?)
