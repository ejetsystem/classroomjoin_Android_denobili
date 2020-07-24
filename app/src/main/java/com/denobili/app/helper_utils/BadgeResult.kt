package com.denobili.app.helper_utils

import com.denobili.app.profilePage.ProfileData
import com.denobili.app.profilePage.ProfileResult

sealed class BadgeResult
data class onSucces(val data: BadgeCountModel) : BadgeResult()