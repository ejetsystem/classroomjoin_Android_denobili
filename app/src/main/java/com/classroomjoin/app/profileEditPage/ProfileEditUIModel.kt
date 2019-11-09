package com.classroomjoin.app.profileEditPage


sealed class ProfileEditUIModel
data class onEditSuccess(val img_url: String?,
                         val gender: String?,
                         val dob: String?,
                         val firstName: String?,
                         val middleName: String?,
                         val lastName: String?,
                         val alternateMobileNo: String?,
                         val lindLine: String?,
                         val address1: String?,
                         val address2: String?,
                         val city: String?,
                         val state: String?,
                         val country: String?
) : ProfileEditUIModel()

data class onEditFailure(val error: String) : ProfileEditUIModel()
class inProgress() : ProfileEditUIModel()
