package com.denobili.app.socialGradeSelectionPage

import com.google.gson.annotations.SerializedName


data class SocialGradeSendModel(
        @SerializedName("orgId")
        var orgid: String?,
        @SerializedName("accountManagementId")
        var acc_id: String?,
        @SerializedName("socialGradeIdList")
        val social_ids: List<String>,
        @SerializedName("studentIdList")
        val studentid: List<String>,
        @SerializedName("classId")
        var classId: String?,
        @SerializedName("createDate")
        var createDate: String?
)