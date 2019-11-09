package com.classroomjoin.app.profilePage


sealed class ProfileUIModel
data class Success(val user_name: String?, val gender: String?, val dob: String?,
                   val img_url: String?,
                   val contact_details: String, val phone: String?, val email: String?) : ProfileUIModel()

data class Failure(val error: String) : ProfileUIModel()
class inProgress() : ProfileUIModel()
