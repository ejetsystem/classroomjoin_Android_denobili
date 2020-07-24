package com.denobili.app.invoice

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName

class InvoiceModel (
        @SerializedName("studentId")
        val id: Int?,

        @SerializedName("firstName")
        val firstName: String?,

        @SerializedName("middleName")
        val middleName: String?,

        @SerializedName("lastName")
        val lastName: String?,

        @SerializedName("admissionNo")
        val admission_id: String,

        @SerializedName("className")
        val classname: String?,

        @SerializedName("activeFlag")
        var active_flag: Int ?) : DisplayItem