package com.classroomjoin.app.studentEventDetailPage

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.*
import com.classroomjoin.app.readStatus.ReadStatusModel
import com.classroomjoin.app.readStatus.ReadStatusOTPEvent
import com.classroomjoin.app.readStatus.ReadStatusPresenter
import com.classroomjoin.app.studentallEventsPage.StudentEventModel
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_event_detail_new.*
import org.jetbrains.anko.AnkoLogger
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : LocalizationActivity(),AnkoLogger, OfflineParentAttachmentDelegate.AddAttachmentListener {

    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object{
        val EVENT_ID_KEY="student.event.id"
        val EVENT_TYPE_ID_KEY="student.event.type"

    }

    private var progressdialog: ProgressDialog?=null
    private var eventid:Int?=null
    private var type_id:String?=null
    private var presenter: EventDetailPresenter?=null
    private var read_status_presenter: ReadStatusPresenter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail_new)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))

        progressdialog= DialogUtil.showProgressDialog(this@EventDetailActivity)
        MainBus.getInstance().busObservable.ofType(EventDetailFetch::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(ReadStatusOTPEvent::class.java).subscribe(eventobserver11)
        presenter= EventDetailPresenter(this@EventDetailActivity)
        read_status_presenter= ReadStatusPresenter(this@EventDetailActivity)

        if(intent.hasExtra(EVENT_ID_KEY))
            eventid=intent.getIntExtra(EVENT_ID_KEY,0)

        if(intent.hasExtra(EVENT_TYPE_ID_KEY))
            type_id=intent.getStringExtra(EVENT_TYPE_ID_KEY)

        if(eventid!=null){
            presenter?.getdata(eventid!!,type_id!!)
        }else showcontent()

        /*val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/
    }

    private val eventobserver = object : Observer<EventDetailFetch> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: EventDetailFetch) {
            if(progressdialog!!.isShowing)progressdialog!!.hide()
            when (event.event) {
                Event.SERVER_ERROR ->shownoresults(event.message)
                Event.RESULT->{
                    invalidateOptionsMenu()
                    showresults(event.studentDetail)

                    read_status_presenter?.postdata(ReadStatusModel(eventid.toString(),type_id,"1",getCurrentDate()))

                }
                Event.NO_RESULT->shownoresults(event.message)
                Event.NO_INTERNET->shownoresults(getString(R.string.noInternet))
                else ->{
                }
            }
        }
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

    private fun showresults(studentDetail: StudentEventModel?) {
        showcontent()
        event_message.text = studentDetail!!.message
        event_teacher_name.text = getString(R.string.from)+studentDetail.teacher_name
        event_detail_date.text = studentDetail.date
        event_subject.text = getString(R.string.sub)+studentDetail.subject
        if(studentDetail.attachments!=null)
            if(studentDetail!!.attachments!!.isNotEmpty())
                showAttachments(studentDetail!!.attachments!!)



    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun showAttachments(attachments:RealmList<OfflineAttachmentModel>){
        recycler_attachments.layoutManager= LinearLayoutManager(this)
        recycler_attachments.isNestedScrollingEnabled = false
        recycler_attachments.adapter= OfflineParentAttachmentAdapter(attachments, this,this)
        recycler_attachments.visibility=View.VISIBLE
    }

    private fun showcontent(){
        event_detail_image_view.visibility=View.GONE
        event_detail_textview.visibility=View.GONE
        event_detail_content_loading.visibility=View.GONE
        event_detail_layout.visibility=View.VISIBLE
    }

    private fun shownoresults(error: String){
        event_detail_layout.visibility=View.GONE
        event_detail_content_loading.hide()
        event_detail_image_view.visibility=View.VISIBLE
        event_detail_textview.visibility=View.VISIBLE
        event_detail_textview.text = error
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId==android.R.id.home){
            finish()
        }
        return true
    }
}
