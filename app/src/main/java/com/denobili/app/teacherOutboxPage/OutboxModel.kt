package com.denobili.app.teacherOutboxPage

import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.denobili.app.helper_utils.Selected_Feature
import com.vicpin.krealmextensions.query
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


open class OutboxModel() : RealmObject(), DisplayItem {

    var feature: Int = Selected_Feature.SMS.`val`
    var message: String? = ""
    var subject: String? = "Subject"
    var list: RealmList<StudentIdClass>? = RealmList<StudentIdClass>()
    var list_social_ids: RealmList<SocialIdClass>? = RealmList<SocialIdClass>()
    var diary_event_id: Int? = null
    var gen_date: Date = Date()
    var shown_date: String? = null
    var attendance_date: String? = null
    var pre_list: RealmList<StudentIdClass>? = RealmList<StudentIdClass>()
    var abs_list: RealmList<StudentIdClass>? = RealmList<StudentIdClass>()
    var sync_state: Int = 0
    var account_id: String = "0"
    var class_id: String? = null
    @PrimaryKey
    var message_id: Int = 0
    var attachment_id: Int = 0
    var scheduleTime: String? = null

    constructor(account_id: String, realm: Realm) : this() {
        this.account_id = account_id
        this.shown_date = getCurrentTime()
        try {
            this.message_id = OutboxModel().query { realmQuery -> realmQuery.max("message_id") }.last().message_id + 1
        } catch (e: NoSuchElementException) {
            this.message_id = 1
        }
    }


    fun getAttachments(): List<OfflineAttachmentModel> =
            OfflineAttachmentModel().query { realmQuery -> realmQuery.equalTo("att_map_id", attachment_id) }

    fun getCurrentTime(): String {
        val dmyFormat = SimpleDateFormat("dd-MM-yyyy  HH:mm", Locale.getDefault())
        return dmyFormat.format(Date())
    }

}