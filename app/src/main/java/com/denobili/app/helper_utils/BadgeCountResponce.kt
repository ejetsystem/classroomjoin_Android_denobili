package com.denobili.app.helper_utils

import com.denobili.app.profilePage.ProfileData
import com.google.gson.annotations.SerializedName

class BadgeCountResponce (
        @SerializedName("status") val status: String,
        @SerializedName("data") val data: BadgeCountModel)
class BadgeCountModel(
        @SerializedName("smsUnread") val smsUnread: String,
        @SerializedName("messageUnread") val messageUnread: String,
        @SerializedName("emailUnread") val emailUnread: String,
        @SerializedName("socialgradeUnread") val socialgradeUnread: String,
        @SerializedName("diaryNotificationUnread") val diaryNotificationUnread: String,
        @SerializedName("diaryEventUnread") val diaryEventUnread: String,
        @SerializedName("diaryAssignmentUnread") val diaryAssignmentUnread: String,
        @SerializedName("diaryUnread") val diaryUnread: String,
        @SerializedName("allUnreadNotification") val allUnreadNotification: String)


