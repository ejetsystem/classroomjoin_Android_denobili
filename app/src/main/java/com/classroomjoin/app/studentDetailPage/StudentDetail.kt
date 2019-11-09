package com.classroomjoin.app.studentDetailPage

import com.google.gson.annotations.SerializedName


class StudentDetail(
        @SerializedName("firstName")
        val name: String,
        @SerializedName("rollNo")
        val rollno: Int,
        @SerializedName("admissionNo")
        val admission: String,
        @SerializedName("parentFirstName")
        val parentname: String,
        @SerializedName("parentLastName")
        val parentLastName: String,
        @SerializedName("parentMiddleName")
        val parentMiddleName: String,
        @SerializedName("parentEmail")
        val parent_email: String,
        @SerializedName("parentMobile")
        val mobile: String,
        @SerializedName("studentCode")
        val studentCode: String? = "Empty"
)