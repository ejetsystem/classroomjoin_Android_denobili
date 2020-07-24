package com.denobili.app.readStatus

import com.google.gson.annotations.SerializedName

data class ReadStatusReponser(
        @SerializedName("status") val status: String,
        @SerializedName("data") val data: String,
        @SerializedName("errormessage") val errormessage: String,
        @SerializedName("errorcode") val errorcode: String)



