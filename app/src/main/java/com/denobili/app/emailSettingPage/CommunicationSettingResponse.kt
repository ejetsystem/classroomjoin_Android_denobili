package com.denobili.app.emailSettingPage

import android.support.annotation.Nullable
import com.google.gson.annotations.SerializedName


class CommunicationSettingResponse(
        @SerializedName("data")
        val data: List<CommunicationModel>,
        @SerializedName("status")
        @Nullable
        val status: String,
        @SerializedName("error_code")
        @Nullable
        val error_code: String,
        @SerializedName("error_message")
        @Nullable
        val error_message: String)