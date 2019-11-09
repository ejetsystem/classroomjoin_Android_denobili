package com.classroomjoin.app.postAttendancePage.checkAttendance

import com.google.gson.annotations.SerializedName

class AttendanceMicroModel {

    @SerializedName("attandanceDate")
    var attandanceDate: String? = null

    @SerializedName("classId")
    var classId: String? = null


    constructor(attandanceDate: String?, classId: String?) {
        this.attandanceDate = attandanceDate
        this.classId = classId
    }


}