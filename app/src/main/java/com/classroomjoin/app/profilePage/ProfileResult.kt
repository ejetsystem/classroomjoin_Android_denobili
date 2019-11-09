package com.classroomjoin.app.profilePage


sealed class ProfileResult
data class onSucces(val data: ProfileData) : ProfileResult()
data class onFailure(val error: String) : ProfileResult()
class inFlight() : ProfileResult()
