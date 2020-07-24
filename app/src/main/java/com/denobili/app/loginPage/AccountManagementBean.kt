package com.denobili.app.loginPage

import android.support.annotation.Nullable
import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject


open class AccountManagementBean : RealmObject(), DisplayItem {

    var userId: Int? = null

    @SerializedName("accountManagementId")
    var accountManagementId: Int? = null

    @SerializedName("accountLabel")
    var accountLabel: String? = null

    @SerializedName("orgId")
    var orgId: Int? = null

    @SerializedName("userLoginUserId")
    var userLoginUserId: Int? = null

    @Nullable
    @SerializedName("activeFlag")
    var activeFlag: Int? = null

    @Nullable
    @SerializedName("role")
    var role: String? = null

    @Nullable
    @SerializedName("createDate")
    var createDate: String? = null







}