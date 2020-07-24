package com.denobili.app.invoice

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class InputInvoiceResponse (
        @SerializedName("status")
        val status:String,
        @SerializedName("data")
        var data: ArrayList<ConnectedListModel>? = null)