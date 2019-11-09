package com.classroomjoin.app.passwordConfirmation


import com.google.gson.annotations.SerializedName


class ConfirmPassModel {

    @SerializedName("userName")
    var userName: String? = null

    @SerializedName("password")
    var password: String? = null


    constructor(userName11: String?, updateDate11: String?) {
        this.userName = userName11
        this.password = updateDate11
    }

}
