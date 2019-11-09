package com.classroomjoin.app.emailSettingPage


interface CommunicationSettingListener {

    fun onCommunicationSettingSuccess()

    fun onCommunicationSettingFailed(error: String)

    fun onCommunicationSettingFetched(model: List<CommunicationModel>)

    fun onCommunicationSettingFetchingError(error: String)

}