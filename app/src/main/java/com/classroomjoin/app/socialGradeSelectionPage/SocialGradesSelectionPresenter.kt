package com.classroomjoin.app.socialGradeSelectionPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.teacherOutboxPage.OutboxModel
import com.classroomjoin.app.teacherOutboxPage.SocialIdClass
import com.classroomjoin.app.teacherOutboxPage.StudentIdClass
import com.vicpin.krealmextensions.save


class SocialGradesSelectionPresenter(private var context: Context) : Presenter() {

    private val interactor: SocialGradeSelectionInteractor

    init {
        interactor = SocialGradeSelectionInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(SocialGradeSelectionEvent(Event.NO_RESULT))
        else Emit(SocialGradeSelectionEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(SocialGradeSelectionEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(SocialGradeSelectionEvent(Event.SERVER_ERROR))
    }

    fun getdata() {
        interactor.getData()
    }

    fun postdata(model: SocialGradeSendModel) {
        if (NetworkHelper.isOnline(context)) interactor.postData(model)
        else {
            var outboxModel = OutboxModel(interactor.account_id, interactor.realm)
            outboxModel.feature = 4
            try {
                model.studentid.forEachIndexed { index, s ->
                    outboxModel.list?.add(index, StudentIdClass(Integer.parseInt(s)))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            model.social_ids.forEachIndexed { index, s ->
                outboxModel.list_social_ids?.add(index, SocialIdClass(s))
            }
            outboxModel.message = context.getString(R.string.social_grades_of) + (model.studentid.size - 1) + context.getString(R.string.students_text)
            outboxModel.save()
            postOutbox()
        }

    }

    private fun postOutbox() {
        Emit(SocialGradeSelectionEvent(Event.SAVED_TO_OUTBOX))
    }

    fun postsuccess() {
        Emit(SocialGradeSelectionEvent(Event.POST_SUCCESS))
    }

    fun postFailure(message: String) {
        Emit(SocialGradeSelectionEvent(Event.POST_FAILURE, message))
    }

    fun refreshdata() {
        interactor.refresh()
    }

}