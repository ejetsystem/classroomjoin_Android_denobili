package com.classroomjoin.app.emailSettingPage

import com.google.gson.annotations.SerializedName


class CommunicationSettingModel(
        @SerializedName("userName")
        val username: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("senderId")
        val senderid: String,
        @SerializedName("orgId")
        var orgid: String? = null,
        @SerializedName("communicationid")
        var comm_id: Int? = null,
        @SerializedName("accountManagementId")
        var acc_id: String? = null,
        @SerializedName("communicationType")
        val comm_type: String? = null,
        @SerializedName("venderId")
        val vendorid: Int? = null,
        @SerializedName("createDate")
        val createDate: String? = null,
        @SerializedName("attendanceSms")
        val attendanceSms: Boolean,
        @SerializedName("smsMain")
        val smsMain: Boolean,
        @SerializedName("emailAttendance")
        val emailAttendance: Boolean,
        @SerializedName("emailMain")
        val emailMain: Boolean,
        @SerializedName("messageAttendancePresent")
        val messageAttendancePresent: Boolean,
        @SerializedName("messageAttendanceAbsent")
        val messageAttendanceAbsent: Boolean,
        @SerializedName("messageMain")
        val messageMain: Boolean

)