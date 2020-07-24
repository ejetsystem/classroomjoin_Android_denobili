package com.denobili.app.postAttendancePage


import com.google.gson.annotations.SerializedName

class PostAttendence {

    @SerializedName("attandanceDate")
    var date: String? = null

    @SerializedName("classId")
    var class_id: String? = null

    @SerializedName("accountManagementId")
    var accountmanagementid: String? = null

    @SerializedName("presentStudentId")
    var roll_no_list: List<String>? = null

    @SerializedName("absentStudentId")
    var roll_no_list_absent: List<String>? = null

    @SerializedName("createDate")
    private var createDate: String? = null

    @SerializedName("templateDataAbsentSubject")
    var templateDataAbsentSubject: String? = null

    @SerializedName("templateDateAbsentText")
    var templateDateAbsentText: String? = null

    @SerializedName("templateDataPresentSubject")
    var templateDataPresentSubject: String? = null

    @SerializedName("templateDatePresentText")
    var templateDatePresentText: String? = null

    @SerializedName("firstNameSender")
    var firstNameSender: String? = null

    @SerializedName("firstNameTeacher")
    var firstNameTeacher: String? = null

    @SerializedName("teacherId")
    var teacherId: String? = null



    constructor(date: String, roll_no_list: List<String>, roll_no_list_absent: List<String>) {
        this.date = date
        this.roll_no_list = roll_no_list
        this.roll_no_list_absent = roll_no_list_absent

    }

    constructor(date: String?, roll_no_list: List<String>, class_id: String?,
                roll_no_list_absent: List<String>,create_date: String?,templateDataAbsentSubject: String?,
                templateDateAbsentText: String?,templateDataPresentSubject: String?,
                templateDatePresentText: String?,firstNameSender: String?,
                firstNameTeacher: String?) {
        this.date = date
        this.roll_no_list = roll_no_list
        this.roll_no_list_absent = roll_no_list_absent
        this.class_id = class_id
        this.createDate = create_date

        this.templateDataAbsentSubject = templateDataAbsentSubject
        this.templateDateAbsentText = templateDateAbsentText
        this.templateDataPresentSubject = templateDataPresentSubject
        this.templateDatePresentText = templateDatePresentText
        this.firstNameSender = firstNameSender
        this.firstNameTeacher = firstNameTeacher

    }

    constructor(date: String?, roll_no_list: List<String>, class_id: String?, roll_no_list_absent: List<String>) {
        this.date = date
        this.roll_no_list = roll_no_list
        this.roll_no_list_absent = roll_no_list_absent
        this.class_id = class_id
    }

    constructor(roll_no_list: List<String>) {
        this.roll_no_list = roll_no_list
    }
}
