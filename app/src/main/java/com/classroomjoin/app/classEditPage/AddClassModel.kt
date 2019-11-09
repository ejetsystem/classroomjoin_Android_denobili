package com.classroomjoin.app.classEditPage


import com.google.gson.annotations.SerializedName


class AddClassModel {

    @SerializedName("className")
    var name: String? = null

    @SerializedName("orgId")
    var org_id: String? = null

    @SerializedName("accountManagementId")
    var acc_id: Int? = null

    @SerializedName("classid")
    var class_id: String? = null

    @SerializedName("createDate")
    var createDate: String? = null

    /*constructor(name: String, class_id: String) {
        this.name = name
        this.class_id = class_id
    }*/

    /*constructor(name: String, org_id: String, acc_id: String) {
        this.name = name
        this.org_id = org_id
        this.acc_id = acc_id
    }*/

    constructor(name: String?) {
        this.name = name
    }

    constructor()
}
