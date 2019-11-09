package com.classroomjoin.app.teacherCommunicationReports

import com.google.gson.annotations.SerializedName


open class ReportDataModel (

    @SerializedName("content")
    var content: List<ReportModel>? = null,

    @SerializedName("last")
    var last: Boolean? = null,

    @SerializedName("totalElements")
    var totalElements: Int? = null


)


