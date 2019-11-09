package com.classroomjoin.app.teacherHomePage

import com.classroomjoin.app.helper_utils.OfflineAttachmentModel
import com.classroomjoin.app.helper_utils.Selected_Feature
import com.vicpin.krealmextensions.query


open class CommunicationSendModel() {
    var feature: Int = Selected_Feature.SMS.`val`
    var message: String? = ""
    var subject: String? = "Subject"
    var list: List<Int>? = null
    var parent_list: List<Int>? = null
    var diary_event_id: Int? = null
    var attachment_id: Int = 0
    var class_id: String? = null
    var scheduleTime: String? = null


    constructor(feature: Selected_Feature, message: String, subject: String, list: List<Int>, diary_event_id: Int) : this() {
        this.feature = feature.`val`
        this.message = message
        this.subject = subject
        this.list = list
        this.diary_event_id = diary_event_id
    }

    fun getAttachments(): List<OfflineAttachmentModel> =
            OfflineAttachmentModel().query { realmQuery -> realmQuery.equalTo("att_map_id", attachment_id) }
}