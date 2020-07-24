package com.denobili.app.templatePage

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


//Email 1
//SMS 2
// Message 3

open class Template() : DisplayItem, RealmObject() {


    @SerializedName("templateId")
    @PrimaryKey
    var id: Int = 0

    @SerializedName("templateTypeId")
    var type_id: Int = 0

    @SerializedName("accountManagementId")
    var account_id: Int = 0

    @SerializedName("orgId")
    var orgid: Int? = null

    @SerializedName("teachersAdminId")
    var admin_id: Int? = null

    @SerializedName("templateText")
    var message: String = ""

    @SerializedName("templateTextHeading")
    var subject: String = ""

    @SerializedName("createdate")
    var createdate: String = ""

   /* constructor(message: String, subject: String, id: Int) : this() {
        this.message = message
        this.subject = subject
        this.id = id
    }*/

    constructor(id: Int,
                type_id: Int,
                account_id: Int,
                admin_id: Int,
                org_id: Int,
                message: String,
                subject: String) : this() {
        this.id = id
        this.type_id = type_id
        this.account_id = account_id
        this.admin_id = admin_id
        this.orgid = org_id
        this.message = message
        this.subject = subject
    }


}