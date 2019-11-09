package com.classroomjoin.app.studentDiaryPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.studentallEventsPage.StudentEventModel
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

    fun getData(type:String) {
        if(StudentDiaryEventModel().query { realmQuery -> realmQuery.equalTo("type",type).equalTo("student_id",student_id) }.isNotEmpty())emitList(type)
        else refreshdata(type)
    }

    private fun emitList(type:String){
        val list= StudentDiaryEventModel().query { realmQuery -> realmQuery.equalTo("type",type).equalTo("student_id",student_id)}
        presenter.setData(getList(list))
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

    fun refreshdata(type:String){
        getObservable(type).subscribe(Observer(type))
    }

    private fun getObservable(event_type:String):Observable<StudentDiaryApiResponse> =
            apiInterface.getDiaryEvents(accesstoken,student_id.toInt(),event_type,getCurrentDate(),"createDate,desc").compose(RxFunctions.applySchedulers())

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
                    emitList(type)
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
