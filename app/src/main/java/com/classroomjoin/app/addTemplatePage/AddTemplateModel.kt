package com.classroomjoin.app.addTemplatePage

import com.google.gson.annotations.SerializedName


class AddTemplateModel(

        @SerializedName("templateText")
        var templateText: String,

        @SerializedName("templateTextHeading")
        var templateTextHeading: String,

        @SerializedName("templateTypeId")
        var templateTypeId: String,

        @SerializedName("orgId")
        var orgId: String?,

        @SerializedName("teachersAdminId")
        var teachersAdminId: String?,

        @SerializedName("accountManagementId")
        var accountManagementId: String?,

        @SerializedName("createDate")
        var createDate: String?)