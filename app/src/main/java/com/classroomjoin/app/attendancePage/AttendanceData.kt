package com.classroomjoin.app.attendancePage

import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName

class AttendanceData : DisplayItem {
    @SerializedName("attandanceDate")
    var date: String? = null
    @SerializedName("presentAbsentFlag")
    var attendance: Int = 0
}
