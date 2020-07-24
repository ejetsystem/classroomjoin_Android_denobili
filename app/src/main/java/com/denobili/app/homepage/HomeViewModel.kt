package com.denobili.app.homepage

import android.content.Context
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.loginPage.Userdata
import com.denobili.app.teacherOutboxPage.OutboxModel
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.deleteAll


class HomeViewModel internal constructor(private val context: Context)
    : Interactor(context) {

    fun clearData() {
        Userdata().deleteAll()
        OutboxModel().delete { query -> query.equalTo("account_id", account_id) }
        try {
            realm.deleteAll()
            //Realm file has been deleted.
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}