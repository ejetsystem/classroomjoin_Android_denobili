package com.classroomjoin.app.signUpPage


import com.google.gson.annotations.SerializedName


class SignUpModel {

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("emailId")
    var email: String? = null

    @SerializedName("mobileNo")
    var phone: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("registrationTypeId")
    var register_type = "3"

    @SerializedName("userTypeId")
    var user_type = "2"

    @SerializedName("createDate")
    var createDate: String? = null

    @SerializedName("socialId")
    var socialId: String? = null

    /*1;"Admin"
    2;"Individual Teacher"
    3;"Teacher"
    4;"Student"
    5;"Parent"
    6;"Staff"*/
    constructor(firstName11: String, email: String,  phone: String, password: String,
                user_type: String,createDate11: String,reg_type:String,social_Id:String) {
        this.firstName = firstName11
        this.email = email
        this.phone = phone
        this.password = password
        this.user_type = user_type
        this.createDate = createDate11
        this.register_type=reg_type
        this.socialId=social_Id
    }

    constructor(firstName11: String,email: String, password: String, user_type: String,
                createDate11: String,reg_type:String,social_Id:String) {
        this.firstName = firstName11
        this.email = email
        this.password = password
        this.user_type = user_type
        this.createDate = createDate11
        this.register_type=reg_type
        this.socialId=social_Id
    }

    constructor(email: String?,  phone: String?) {
        this.email = email
        this.phone = phone
    }

}
