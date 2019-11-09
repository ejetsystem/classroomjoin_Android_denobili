package com.classroomjoin.app.readStatus

import com.google.gson.annotations.SerializedName

class ReadStatusModel(

        @SerializedName("eventId")
        var eventId: String? = null,

        @SerializedName("eventTypeId")
        var eventTypeId: String? = null,

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("updateDate")
        var updateDate: String? = null

)