package com.classroomjoin.app.emailSettingPage

import com.google.gson.annotations.SerializedName


class CommunicationModel(
        @SerializedName("id") val id: Int,
        @SerializedName("userName")
        val username: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("senderId")
        val senderid: String,
        @SerializedName("activeFlag")
        val active_flag: Int,
        @SerializedName("vendorId")
        val vendor_id: Int,
        @SerializedName("smsAttendance")
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
        val messageMain: Boolean)