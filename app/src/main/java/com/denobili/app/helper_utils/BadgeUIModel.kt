package com.denobili.app.helper_utils


open class BadgeUIModel
data class Success(val user_name: String?, val gender: String?, val dob: String?,
                   val img_url: String?,
                   val contact_details: String, val phone: String?, val email: String?) : BadgeUIModel()

