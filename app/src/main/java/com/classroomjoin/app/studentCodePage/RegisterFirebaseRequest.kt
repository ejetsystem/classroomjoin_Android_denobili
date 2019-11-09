package com.classroomjoin.app.studentCodePage

import com.google.gson.annotations.SerializedName


class RegisterFirebaseRequest(

        @SerializedName("token")
        var token: String,

        @SerializedName("parentUserId")
        var user_id: String,

        @SerializedName("studentId")
        var list: ArrayList<Int>,

        @SerializedName("createDate")
        var createdate: String)