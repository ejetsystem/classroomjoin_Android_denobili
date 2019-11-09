package com.classroomjoin.app.emailSettingPage

import android.support.annotation.Nullable
import com.google.gson.annotations.SerializedName

/**
 * Created by  on 28/2/18.
 */
class CommunicationSettingAddResponse(

        @SerializedName("data")
        val data: String,
        @SerializedName("status")
        @Nullable
        val status: String,
        @SerializedName("error_code")
        @Nullable
        val error_code: String,
        @SerializedName("error_message")
        @Nullable
        val error_message: String)