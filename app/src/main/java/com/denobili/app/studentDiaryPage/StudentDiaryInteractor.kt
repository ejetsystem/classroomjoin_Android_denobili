package com.denobili.app.studentDiaryPage

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.saveAll
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentDiaryInteractor
    internal constructor(context: Context, private val presenter: Student_Diary_Presenter)
        : Interactor(context),AnkoLogger {

    fun getData(type:String,page: Int) {
        if(StudentDiaryEventModel().query { realmQuery -> realmQuery.equalTo("type",type).equalTo("student_id",student_id) }.isNotEmpty())emitList(type, 1,1)
        else refreshdata(type,page)
    }

    private fun emitList(type: String, totalPages: Int, totalElements: Int){
        val list= StudentDiaryEventModel().query { realmQuery -> realmQuery.equalTo("type",type).equalTo("student_id",student_id)}
        presenter.setData(getList(list),totalPages,totalElements)
    }

    private fun saveToDisk(list:List<StudentDiaryEventModel>,type: String){
        val result=ArrayList<StudentDiaryEventModel>()
        StudentEventModel().delete { realmQuery -> realmQuery.equalTo("type",type).equalTo("student_id",student_id)}
        list.forEachIndexed { index, studentDiaryEventModel ->
            studentDiaryEventModel.type=type
            studentDiaryEventModel.student_id=student_id
            result.add(index,studentDiaryEventModel)
        }
        result.saveAll()
    }

    fun refreshdata(type:String,page:Int){
        getObservable(type,page).subscribe(Observer(type))
    }

    private fun getObservable(event_type:String,page: Int):Observable<StudentDiaryApiResponse> =
            apiInterface.getDiaryEvents(accesstoken,student_id.toInt(),event_type,getCurrentDate(),page,"createDate,desc").compose(RxFunctions.applySchedulers())

    private fun Observer(type: String):Observer<StudentDiaryApiResponse> {
        return object : Observer<StudentDiaryApiResponse> {
            override fun onCompleted() {
            }
            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }
            override fun onNext(t: StudentDiaryApiResponse?) {
                if (t?.status == "Success") {
                    saveToDisk(t.data.content,type)
                    emitList(type,t.data.totalPages,t.data.totalElements)
                } else presenter.setError(t!!.error_status!!)
            }
        }
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun getList(list: List<StudentDiaryEventModel>):ArrayList<DisplayItem>{
        /*var previous_month=0
        list.forEachIndexed { index, studentDiaryEventModel ->
            val date=studentDiaryEventModel.date_alt
            if(date!=null) {
                val current_month = date!!.substring(0, 2)
                if (current_month.toInt() != previous_month) {
                    previous_month = current_month.toInt()
                    val monthString = DateFormatSymbols().months[previous_month - 1]
                    result_list.add(StudentDiaryDateModel(monthString))
                }
                studentDiaryEventModel.date_alt=studentDiaryEventModel.date_alt!!.removeRange(0,3)
                result_list.add(studentDiaryEventModel)
            }
        }*/

        Collections.sort(list, SortbyDateDiary())
        val result_list=ArrayList<DisplayItem>()
        result_list.addAll(list)
        return result_list
    }
}
