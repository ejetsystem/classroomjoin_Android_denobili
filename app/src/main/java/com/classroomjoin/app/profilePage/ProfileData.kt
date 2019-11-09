package com.classroomjoin.app.profilePage

import com.google.gson.annotations.SerializedName


data class ProfileData(
        @SerializedName("firstName") var firstName: String?,
        @SerializedName("middleName") var middleName: String?,
        @SerializedName("lastName") var lastName: String?,
        @SerializedName("dateOfBirth") var dob: String?,
        @SerializedName("gender") var gnder: String?,
        @SerializedName("address1") var add_1: String?,
        @SerializedName("address2") var add_2: String?,
        @SerializedName("city") var city: String?,
        @SerializedName("state") var state: String?,
        @SerializedName("country") var country: String?,
        @SerializedName("alternateMobileNo") var alternateMobileNo: String?,
        @SerializedName("lindLine") var lindLine: String? = null,
        @SerializedName("orgId") var id: String? = null,
        @SerializedName("userId") var user_id: String,
        @SerializedName("imageUrl") var imageUrl: String,
        @SerializedName("createDate") var createDate: String,
        @SerializedName("mobileNo") var mobileNo: String?,
        @SerializedName("emailId") var emailId: String?)