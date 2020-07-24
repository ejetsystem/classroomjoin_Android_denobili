package com.denobili.app.studentDetailPage


import com.google.gson.annotations.SerializedName


class AddStudentModel {

    @SerializedName("firstName")
    var name: String? = null

    @SerializedName("admissionNo")
    var admission_id: String? = null

    @SerializedName("rollNo")
    var roll_no: String? = null

    @SerializedName("classId")
    var class_id: String? = null

    @SerializedName("teacherId")
    var teacher_id: String? = null

    @SerializedName("accountManagementId")
    var accountManagementId: String? = null

    @SerializedName("orgId")
    var org_id: String? = null

    @SerializedName("createDate")
    var createDate: String? = null

    @SerializedName("student_id")
    var student_id: Int? = null

    @SerializedName("parentMobile")
    var parentMobile: String? = null


    constructor(firstName: String, admissionNo: String, rollNo: String, classId: String,
                 createDate11: String, parentMobile11: String) {
        this.name = firstName
        this.admission_id = admissionNo
        this.roll_no = rollNo
        this.class_id = classId
        this.createDate = createDate11
        this.parentMobile = parentMobile11

    }


}
