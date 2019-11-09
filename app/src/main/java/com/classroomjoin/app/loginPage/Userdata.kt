package com.classroomjoin.app.loginPage

import android.support.annotation.Nullable
import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey


open class Userdata : RealmObject(), DisplayItem {

    @SerializedName("username")
    var name: String? = null

    @SerializedName("userId")
    @PrimaryKey
    var user_id: Int? = null

    @SerializedName("accountId")
    var acc_id: Int? = null

    @SerializedName("orgId")
    var orgid: Int? = null

    @SerializedName("userType")
    var usertype_id: Int? = null

    @Nullable
    @SerializedName("firstName")
    var firstname: String? = null

    @Nullable
    @SerializedName("middleName")
    var middlename: String? = null

    @SerializedName("lastName")
    var lastname: String? = null

    @SerializedName("tsppersonalid")
    var teacherid: Int? = null

    @SerializedName("teacheradminid")
    @Nullable
    var admin_id: Int? = null

    var access_token: String? = null

    @SerializedName("emailId")
    var email: String? = null

    @SerializedName("mobileNo")
    var mobileNo: String? = null

    @SerializedName("profileUrl")
    var profile_url: String? = null

    var isAdmin = false


    @Ignore
    @Nullable
    @SerializedName("accountManagementBean")
    var accountManagementBean: JsonArray? = null

    @Ignore
    @Nullable
    @SerializedName("teacherLoginBean")
    var teacherLoginBean: JsonArray? = null


    fun getUserType(): String = if (usertype_id == 1 || usertype_id == 2 || usertype_id == 3)
        "Teacher"
    else if (usertype_id == 4) "Student"
    else "Parent"


    fun connect_students(): String = if (usertype_id == 4) "2" else "1"
}