package com.denobili.app.studentDiaryEventDetailPage

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.denobili.app.helper_utils.OfflineParentAttachmentAdapter
import com.denobili.app.helper_utils.OfflineParentAttachmentDelegate
import com.denobili.app.readStatus.ReadStatusModel
import com.denobili.app.readStatus.ReadStatusOTPEvent
import com.denobili.app.readStatus.ReadStatusPresenter
import com.denobili.app.studentDiaryPage.StudentDiaryEventModel
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_event_detail_new.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class DiaryEventDetailActivity : LocalizationActivity(), AnkoLogger, OfflineParentAttachmentDelegate.AddAttachmentListener {

    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object eventdiarydetail {
        val EVENT_ID_KEY = "student.event.id"
        val EVENT_TYPE_ID_KEY = "student.event.type_id"

    }

    private val progressdialog: ProgressDialog? by lazy {
        DialogUtil.showProgressDialog(this@DiaryEventDetailActivity)
    }
    private var type_id: Int? = null
    private var event_type_id: String? = null

    private val viewmodel: EventDetailViewModel by lazy {
        EventDetailViewModel(this@DiaryEventDetailActivity)
    }

    private var read_status_presenter: ReadStatusPresenter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail_new)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))
        read_status_presenter= ReadStatusPresenter(this)
        MainBus.getInstance().busObservable.ofType(ReadStatusOTPEvent::class.java).subscribe(eventobserver11)

        if (intent.hasExtra(EVENT_ID_KEY)) {
            type_id = intent.getIntExtra(EVENT_ID_KEY, 0)
            event_type_id= intent.getStringExtra(EVENT_TYPE_ID_KEY)
            getData(type_id!!,"2")
        } else showcontent()

       /* val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/
    }

    private val eventobserver11 = object : Observer<ReadStatusOTPEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: ReadStatusOTPEvent) {
            if(progressdialog!!.isShowing)progressdialog!!.hide()
            /* when (event.event) {
                 Event.SERVER_ERROR ->shownoresults(event.message)
                 Event.RESULT->{
                     showresults(event.studentDetail)
                 }
                 Event.NO_RESULT->shownoresults(event.message)
                 Event.NO_INTERNET->shownoresults(getString(R.string.noInternet))
                 else ->{
                 }
             }*/
        }
    }

    private fun getData(event_id: Int,eventType_id: String) {
        viewmodel.getDataFromDisk(event_id)!!
                .switchIfEmpty(viewmodel.getDataFromRemote(event_id,eventType_id))!!
                .compose(RxFunctions.applySchedulers())
                .takeIf { NetworkHelper.isOnline(this@DiaryEventDetailActivity) }
                ?.repeat(1)
                ?.subscribe(eventobserver)
                ?: shownoresults(getString(R.string.noInternet))
    }

    private val eventobserver = object : Observer<StudentDiaryEventModel> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            error { e.message }
            if (progressdialog!!.isShowing) progressdialog!!.hide()
            e.printStackTrace()
        }

        override fun onNext(event: StudentDiaryEventModel) {
            error { "on next studentDiaryEventModel called" }
            if (progressdialog!!.isShowing) progressdialog!!.hide()
            if (event.id == 0) shownoresults(getString(R.string.noResults))
            showresults(event)
        }
    }

    private fun showresults(studentDetail: StudentDiaryEventModel) {
        showcontent()
        event_message.text = studentDetail.message
        event_teacher_name.text = getString(R.string.from)+ studentDetail.teacher_name
        event_detail_date.text = studentDetail.date
        event_subject.text = getString(R.string.sub)+ studentDetail.subject
        if (studentDetail.attachments != null)
            if (studentDetail!!.attachments!!.isNotEmpty())
                showAttachments(studentDetail!!.attachments!!)

        read_status_presenter?.postdata(ReadStatusModel(studentDetail.id.toString(),"2".toString(),"1",getCurrentDate()))


    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun showAttachments(attachments: RealmList<OfflineAttachmentModel>) {
        recycler_attachments.layoutManager = LinearLayoutManager(this)
        recycler_attachments.isNestedScrollingEnabled = false
        println("Data-->RRRRRRRRRR--->"+attachments.size)
        recycler_attachments.adapter = OfflineParentAttachmentAdapter(attachments, this,this)
        recycler_attachments.visibility = View.VISIBLE
    }

    private fun showcontent() {
        event_detail_image_view.visibility = View.GONE
        event_detail_textview.visibility = View.GONE
        event_detail_content_loading.visibility = View.GONE
        event_detail_layout.visibility = View.VISIBLE
    }

    private fun shownoresults(error: String) {
        event_detail_layout.visibility = View.GONE
        event_detail_content_loading.hide()
        event_detail_image_view.visibility = View.VISIBLE
        event_detail_textview.visibility = View.VISIBLE
        event_detail_textview.text = error
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}