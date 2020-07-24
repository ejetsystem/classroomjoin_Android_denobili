package com.denobili.app.studentSearchPage

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.vicpin.krealmextensions.queryAsObservable
import io.realm.Case
import org.jetbrains.anko.AnkoLogger
import rx.Observable


class SearchViewModel
internal constructor(context: Context)
    : Interactor(context), AnkoLogger {


    fun getData(query: String): Observable<List<StudentEventModel>> {
        try {
            return StudentEventModel().queryAsObservable { realmQuery ->
                realmQuery.beginGroup() // <--
                        .equalTo("student_id", student_id)
                        .endGroup() // <--
                        .beginGroup() // <--
                        .contains("message", query, Case.INSENSITIVE)
                        .or()
                        .contains("subject", query, Case.INSENSITIVE)
                        .or()
                        .contains("teacher_name", query, Case.INSENSITIVE)
                        .endGroup() // <--
                        .findAll();
            }.compose(RxFunctions.applySchedulers())
        } catch (e: Exception) {
            e.printStackTrace()
            val result = ArrayList<StudentEventModel>()
            return Observable.just(result)
        }
    }

}