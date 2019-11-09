package com.classroomjoin.app.teacherMessageSendPage

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Pair
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.FileChooser
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.*
import com.classroomjoin.app.socialGradeSelectionPage.SocialGradeSelectionPage
import com.classroomjoin.app.teacherAttachmentPage.AttachmentViewModel
import com.classroomjoin.app.teacherAttachmentPage.FileAttachmentResponse
import com.classroomjoin.app.teacherHomePage.CommunicationSendModel
import com.classroomjoin.app.teacherHomePage.TemplateSelectionListener
import com.classroomjoin.app.teacherHomePage.onFeatureChangedListener
import com.classroomjoin.app.teacherOutboxPage.Outboxpage
import com.classroomjoin.app.templatePage.TemplateSelectionEvent
import com.classroomjoin.app.templatePage.TemplateSelectionPresenter
import com.classroomjoin.app.utils.SublimePickerFragment

import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.saveAll
import kotlinx.android.synthetic.main.activity_image_chooser.*
import kotlinx.android.synthetic.main.activity_teacher_communication.*
import kotlinx.android.synthetic.main.content_teacher_communication_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import rx.Observer
import java.io.*
import java.util.*


class TeacherCommunicationPage : BaseListActivity(), onFeatureChangedListener, TemplateSelectionListener, SublimePickerFragment.Callback, AnkoLogger, OfflineAttachmentDelegate.AddAttachmentListener {


    companion object studentList {
        var studentListKey = "com.user.classroom.studentlist"
        var attachment_list_key: String = "com.user.classroom.attachmentlist"
        val CLASSIDKEY: String = "com.students.class"
        var parentListKey = "com.user.classroom.parentlist"

    }

    private val tabIcons = intArrayOf(
            R.drawable.ic_sms_white,
            R.drawable.ic_message_white,
            R.drawable.ic_mail_white,
            R.drawable.ic_leftnav_diary_white,
            R.drawable.ic_leftnav_social_white
    )
    private val tabTexts = arrayOf(
            R.string.sms,
            R.string.message,
            R.string.mail,
            R.string.diary,
            R.string.grade)


    private val sendModel: CommunicationSendModel = CommunicationSendModel()
    private var list: List<Int>? = ArrayList<Int>()
    private var parent_list: List<Int>? = ArrayList<Int>()

    private var progressdialog: ProgressDialog? = null
    private var adapter: TemplateSelectionCommunicationAdapter? = null
    private var presenter: TeacherMessageSendPresenter? = null
    private var templateSelectionPresenter: TemplateSelectionPresenter? = null
    private var attachmentAdapter: OfflineAttachmentAdapter? = null
    private var classid: String? = "0"
    private var event_type: Int = -1
    var scheduleTime: String? = null
    private val REQUEST_CAMERA: Int = 1234
    private val SELECT_FILE: Int = 4321
    private var call_back: SublimePickerFragment.Callback? = null
    var viewmodel: AttachmentViewModel? = null
    var finalFile: File? = null
    var imageUri: Uri? = null

    private var is_primary: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_communication)
        initviews()
        setupTabLayout()
        supportActionBar?.setTitle(getString(R.string.sendmessage))
        call_back = this
        adapter = TemplateSelectionCommunicationAdapter(null, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@TeacherCommunicationPage, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.isNestedScrollingEnabled = false

        viewmodel = AttachmentViewModel(this@TeacherCommunicationPage)

        attachmentAdapter = OfflineAttachmentAdapter(null, this)
        recycler_attachments.adapter = attachmentAdapter
        recycler_attachments.layoutManager = LinearLayoutManager(this@TeacherCommunicationPage, LinearLayoutManager.HORIZONTAL, false)
        recycler_attachments.isNestedScrollingEnabled = false


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        MainBus.getInstance().busObservable.ofType(TemplateSelectionEvent::class.java).subscribe(templateevent_observer)
        templateSelectionPresenter = TemplateSelectionPresenter(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))

        progressdialog = DialogUtil.showProgressDialog(this)
        event_diary_type.text = getString(R.string.select_preference)



        if (intent.hasExtra(CLASSIDKEY)) {
            classid = intent.getStringExtra(CLASSIDKEY)
        }

        template_button_content.onClick {
            if (include_content.visibility == View.VISIBLE)
                include_content.visibility = View.GONE
            else {
                include_content.visibility = View.VISIBLE
                getTemplates()
            }
        }

        attachment_icon.onClick {
            galleryIntent()

        }

        attachment_icon_camera.onClick {
            cameraIntent()

        }

        counter_fab.onClick {
            checkMessageFeature()
        }

        MainBus.getInstance().busObservable.ofType(TeacherCommSendEvent::class.java).subscribe(eventobserver)
        presenter = TeacherMessageSendPresenter(this)
        schedule_time_date.setOnClickListener(onClickListener_button);// set listner to your radio button
        primary_mobile.setOnClickListener(onClickListener_button);// set listner to your radio button
        alternate_mobile.setOnClickListener(onClickListener_button);// set listner to your radio button


    }

    private fun cameraIntent() {

        var values = ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_file)), SELECT_FILE)
    }

    private var onClickListener_button: View.OnClickListener = View.OnClickListener { view ->

        if (view.id == R.id.schedule_time_date) {
            println("Data--->" + "schedule_time_date")

            val pickerFrag = SublimePickerFragment()
            pickerFrag.setCallback(call_back)

            val optionsPair = getOptions()
            // Valid options
            val bundle = Bundle()
            bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second)
            pickerFrag.setArguments(bundle)

            pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
            pickerFrag.show(supportFragmentManager, "SUBLIME_PICKER")

        } else if (view.id == R.id.primary_mobile) {

            is_primary = true

        } else if (view.id == R.id.alternate_mobile) {

            is_primary = false

        }
    }

    // Validates & returns SublimePicker options
    internal fun getOptions(): Pair<Boolean, SublimeOptions> {
        val options = SublimeOptions()
        var displayOptions = 0

        displayOptions = displayOptions or SublimeOptions.ACTIVATE_DATE_PICKER or SublimeOptions.ACTIVATE_TIME_PICKER

        options.setDisplayOptions(displayOptions)

        return Pair(if (displayOptions != 0) java.lang.Boolean.TRUE else java.lang.Boolean.FALSE, options)
    }


    override fun onCancelled() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        println("Data--->" + "onCancelled")
        send_now.isChecked = true
        schedule_time_date.text = getString(R.string.schedlue_time)
    }

    override fun onDateTimeRecurrenceSet(selectedDate: SelectedDate?, hourOfDay: Int, minute: Int, recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?, recurrenceRule: String?) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        println("Data--->" + "onDateTimeRecurrenceSet--->" + selectedDate.toString())

        schedule_time_date.text = ""

        var hour: String = hourOfDay.toString()
        if (hour.length == 1)
            hour = "0" + hour

        var minute11: String = minute.toString()
        if (minute11.length == 1)
            minute11 = "0" + minute11

        var day: String = selectedDate!!.getStartDate().get(Calendar.DATE).toString()
        if (day.length == 1)
            day = "0" + day

        var month: Int = selectedDate!!.getStartDate().get(Calendar.MONTH)
        month = month + 1


        var month11: String = month.toString()

        if (month11.length == 1)
            month11 = "0" + month11

        var year: String = selectedDate!!.getStartDate().get(Calendar.YEAR).toString()

        scheduleTime = year + "-" + month11 + "-" + day + " at " + hour + ":" + minute11 + ":00"
        schedule_time_date.text = getString(R.string.scheduled_on) + scheduleTime

    }

    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onTemplateSelected(message: String, id: Int, type_id: Int) {
        message_to_send_here.text.clear()
        message_to_send_here.setText(message.toString())
        if (include_content.visibility == View.VISIBLE) include_content.visibility = View.GONE
    }


    private fun getSelectedDiaryEvent() {
        if (diary_type_assignment.isChecked) sendModel.diary_event_id = 2
        else if (diary_type_diary.isChecked) sendModel.diary_event_id = 1
        else if (diary_type_events.isChecked) sendModel.diary_event_id = 4
        else if (diary_type_notice.isChecked) sendModel.diary_event_id = 3
    }

    private val templateevent_observer = object : Observer<TemplateSelectionEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: TemplateSelectionEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT ->
                    showNoResults()
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }

                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                else -> {
                }
            }
        }
    }


    private fun showLoading() {
        imageView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        textView.visibility = View.GONE
        contentLoadingProgressBar.visibility = View.VISIBLE
        contentLoadingProgressBar.show()
    }


    private fun getTemplates() {
        var type: Int? = null
        when (sendModel.feature) {
            Selected_Feature.SMS.`val` -> {
                type = 2
            }
            Selected_Feature.NOTIFICATION_FEATURE.`val` -> {
                type = 3
            }
            Selected_Feature.EMAIL.`val` -> {
                type = 1
            }

        }
        showLoading()
        templateSelectionPresenter!!.getdata(type!!)
    }

    private fun setupTabLayout() {
        for (i in 0..4) {
            tabs_feature.addTab(tabs_feature.newTab().setText(tabTexts[i]).setIcon(tabIcons[i]))
        }
        tabs_feature.addOnTabSelectedListener(tablistener)
        tabs_feature.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#ffffff"))

    }


    private val tablistener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            when (tab!!.position) {
                0 -> featureSelected(Selected_Feature.SMS)
                1 -> featureSelected(Selected_Feature.NOTIFICATION_FEATURE)
                2 -> featureSelected(Selected_Feature.EMAIL)
                3 -> featureSelected(Selected_Feature.DIARY)
                4 -> {
                    featureSelected(Selected_Feature.SOCIAL_GRADE)
                    startActivity(intentFor<SocialGradeSelectionPage>(SocialGradeSelectionPage.STUDENTSKEY to list, SocialGradeSelectionPage.CLASSIDKEY to classid))
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            when (tab!!.position) {
                0 -> featureSelected(Selected_Feature.SMS)
                1 -> featureSelected(Selected_Feature.NOTIFICATION_FEATURE)
                2 -> featureSelected(Selected_Feature.EMAIL)
                3 -> featureSelected(Selected_Feature.DIARY)
                4 -> {
                    featureSelected(Selected_Feature.SOCIAL_GRADE)
                    startActivity(intentFor<SocialGradeSelectionPage>(SocialGradeSelectionPage.STUDENTSKEY to list, SocialGradeSelectionPage.CLASSIDKEY to classid))
                }
            }
        }

    }

    override fun featureSelected(feature: Selected_Feature) {
        sendModel.feature = feature.`val`
        when (sendModel.feature) {
            Selected_Feature.SMS.`val` -> {
                event_diary_type1.visibility = View.VISIBLE
                radio_group_mobile.visibility = View.VISIBLE
                sendModel.diary_event_id = null
                hideDiaryEventType()
                hideAttachments()
                showTemplates()
                event_diary_type.text = getString(R.string.select_preference)
            }
            Selected_Feature.NOTIFICATION_FEATURE.`val` -> {

                event_diary_type1.visibility = View.GONE
                radio_group_mobile.visibility = View.GONE
                sendModel.diary_event_id = null
                hideDiaryEventType()
                showAttachment()
                showTemplates()
                event_type = 3
                event_diary_type.text = getString(R.string.select_preference)

                println("DATA--->event_type--->" + event_type)
            }
            Selected_Feature.EMAIL.`val` -> {
                event_diary_type1.visibility = View.GONE
                radio_group_mobile.visibility = View.GONE
                sendModel.diary_event_id = null
                hideDiaryEventType()
                showAttachment()
                showTemplates()
                event_type = 2
                event_diary_type.text = getString(R.string.select_preference)

                println("DATA--->event_type--->" + event_type)

            }
            Selected_Feature.DIARY.`val` -> {
                event_diary_type1.visibility = View.GONE
                radio_group_mobile.visibility = View.GONE
                showDiaryEventType()
                showAttachment()
                hideTemplates()
                event_type = 1
                println("DATA--->event_type--->" + event_type)
                event_diary_type.text = getString(R.string.diary_type)


            }
        }
    }


    private fun showAttachment() {
        attachment_icon.visibility = View.VISIBLE
        attachment_icon_camera.visibility = View.VISIBLE
        showAttachedFiles()
    }

    private fun showAttachedFiles() {
        if (attachmentAdapter!!.itemCount != 0) recycler_attachments.visibility = View.VISIBLE
    }

    private fun refreshAttachments() {
        attachmentAdapter!!.removeall()
        attachmentAdapter!!.addItems(true, sendModel.getAttachments())
    }

    private fun showTemplates() {
        template_button_content.visibility = View.VISIBLE
        include_content.visibility = View.GONE


    }

    private fun hideTemplates() {
        template_button_content.visibility = View.GONE
        include_content.visibility = View.GONE

    }

    private fun hideAttachments() {
        attachment_icon.visibility = View.GONE
        attachment_icon_camera.visibility = View.GONE

        recycler_attachments.visibility = View.GONE

    }

    private fun showDiaryEventType() {
        //event_diary_type.visibility = View.VISIBLE
        radio_group_diary.visibility = View.VISIBLE
        radio_group_schedule.visibility = View.GONE
        // event_diary_type.text=getString(R.string.diary_type)

    }

    private fun hideDiaryEventType() {
        //event_diary_type.visibility = View.GONE

        send_now.isChecked = true
        schedule_time_date.text = getString(R.string.schedlue_time)
        scheduleTime = null

        radio_group_diary.visibility = View.GONE
        radio_group_schedule.visibility = View.VISIBLE
        event_diary_type.text = getString(R.string.select_preference)


    }


    private val eventobserver = object : Observer<TeacherCommSendEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: TeacherCommSendEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {

                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                Event.POST_SUCCESS -> showSuccessDialog(event.error)
                Event.POST_FAILURE -> DialogUtil.showerrorDialog(event.error, this@TeacherCommunicationPage::sendMessage, this@TeacherCommunicationPage)
                Event.SAVED_TO_OUTBOX -> {
                    Snackbar.make(recyclerView, R.string.saved_to_outbox, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_outbox, listener)
                            .show()
                    message_to_send_here.text.clear()
                    if (sendModel.feature != Selected_Feature.SMS.`val`) {
                        attachmentAdapter!!.clear()
                        sendModel.attachment_id = 0
                        recycler_attachments.visibility = View.GONE
                    }
                }
                else -> {
                }
            }
        }
    }

    private val listener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            startActivity<Outboxpage>()
        }

    }


    private fun showSuccessDialog(message: String?) {

        var alert = android.support.v7.app.AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.app_name))
        alert.setMessage(message)
        alert.setPositiveButton(getString(R.string.btn_ok)) { dialog, whichButton ->
            message_to_send_here.text.clear()
            send_now.isChecked = true
            schedule_time_date.text = getString(R.string.schedlue_time)
            scheduleTime = null
            if (sendModel.feature != Selected_Feature.SMS.`val`) {
                attachmentAdapter!!.clear()
                sendModel.attachment_id = 0
                recycler_attachments.visibility = View.GONE
            }
            dialog.dismiss()
        }
        alert.show()
    }


    private fun checkMessageFeature() {
        var proceed = true
        when (sendModel.feature) {
            Selected_Feature.SMS.`val`, Selected_Feature.NOTIFICATION_FEATURE.`val`, Selected_Feature.EMAIL.`val` -> {
                if (!validateMessage())
                    proceed = validateMessage()
                if (proceed) {
                    sendMessage()
                }
            }
            Selected_Feature.SOCIAL_GRADE.`val` -> {
                startActivity(intentFor<SocialGradeSelectionPage>(SocialGradeSelectionPage.STUDENTSKEY to list, SocialGradeSelectionPage.CLASSIDKEY to classid))
            }
            Selected_Feature.DIARY.`val` -> {

                getSelectedDiaryEvent()

                if (!validateMessage())
                    proceed = validateMessage()

                if (proceed) {
                    sendMessage()
                }
            }
        }

    }


    private fun validateMessage(): Boolean {
        if (message_to_send_here.text.isNullOrBlank()) {
            message_to_send_here.error = getString(R.string.valid_message_to_send)
            return false
        } else if (message_to_send_here.text.trim().length < 8) {
            message_to_send_here.error = getString(R.string.valid_message_to_send_charecter_limit)
            return false
        } else {
            sendModel.message = message_to_send_here.text.toString()
            return true
        }
    }

    private fun sendMessage() {

        if (is_primary) {

            if (intent.hasExtra(studentListKey)) {
                list = intent.getIntegerArrayListExtra(studentListKey)
                sendModel.list = list!!
            }
        } else {

            if (intent.hasExtra(parentListKey)) {
                parent_list = intent.getIntegerArrayListExtra(parentListKey)
                sendModel.parent_list = parent_list!!
            }
        }
        progressdialog!!.show()
        sendModel.class_id = classid

        if (send_now.isChecked) sendModel.scheduleTime = null
        if (schedule_time_date.isChecked) sendModel.scheduleTime = scheduleTime


        presenter?.send(sendModel, is_primary)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK) {
             if (data != null) sendModel.attachment_id = data.getIntExtra(attachment_list_key, 0)
             refreshAttachments()
             showAttachedFiles()
         }
     }*/

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    onSelectFromGalleryResult(data)
                }
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data)
            }
        }
    }

/*
    private fun onCaptureImageResult(data: Intent?) {
        error { "this is here" }
        val thumbnail = data?.extras?.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        finalFile = File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")
        val fo: FileOutputStream
        // addFile(finalFile!!)
        showSuccessDialog1(finalFile!!)

        try {
            finalFile!!.createNewFile()
            fo = FileOutputStream(finalFile)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }
*/

    private fun onCaptureImageResult(data: Intent?) {

        var thumbnail = MediaStore.Images.Media.getBitmap(
                getContentResolver(), imageUri)

        println("onCaptureImageResult--->" + thumbnail.width + "*" + thumbnail.height)

        //   val thumbnail = data?.extras?.get("data") as Bitmap

        thumbnail = Bitmap.createScaledBitmap(thumbnail, 800, 800, true)

        var mtx = Matrix()
        var angle = 90
        mtx.preRotate(angle.toFloat());
        thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, 800, 800, mtx,
                false)
        //image_preview.setImageBitmap(thumbnail)
        //thumbnail=getCroppedBitmap(thumbnail)
        println("onCaptureImageResult getCroppedBitmap--->" + thumbnail.width + "*" + thumbnail.height)

        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        finalFile = File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")

        showSuccessDialog1(finalFile!!)

        val fo: FileOutputStream
        try {
            finalFile!!.createNewFile()
            fo = FileOutputStream(finalFile)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                val uri = data.data
                if (uri != null) {
                    var path = FileChooser.getPath(this, uri)
                    if (path != null) {
                        var file = File(FileChooser.getPath(this, uri))
                        val file_size = Integer.parseInt((file.length() / 1024).toString())
                        var filename = file.name
                        try {
                            val returnCursor = contentResolver.query(uri, null, null, null, null)
                            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                            returnCursor.moveToFirst()
                            filename = returnCursor.getString(nameIndex)
                            returnCursor.close()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        if (checkFileType(getMimeType(uri)) && file_size / 1024 < 25)
                        // addFile(file = file)
                            showSuccessDialog1(file)
                        else if (!checkFileType(getMimeType(uri))) Snackbar.make(recycler_home, getString(R.string.support_filetype_error), Snackbar.LENGTH_LONG).show()
                        else Snackbar.make(recycler_home, getString(R.string.file_size_large), Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(recycler_home, getString(R.string.file_issue), Snackbar.LENGTH_LONG).show()

                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
                Snackbar.make(recycler_home, getString(R.string.file_issue), Snackbar.LENGTH_LONG).show()

            }

        }
    }

    /* private fun onSelectFromGalleryResult(data: Intent?) {
         var bm: Bitmap? = null
         if (data != null) {
             try {
                 bm = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                 bm = Bitmap.createScaledBitmap(bm,  800, 800,true)
                // image_preview.setImageBitmap(bm)
                 // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                 val tempUri = getImageUri(applicationContext, bm)

                 error { tempUri.toString() }
                 // CALL THIS METHOD TO GET THE ACTUAL PATH
                 finalFile = File(getRealPathFromURI(tempUri))

                 showSuccessDialog1(finalFile!!)

             } catch (e: IOException) {
                 e.printStackTrace()
             }

         }
     }*/

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun showSuccessDialog1(file_image: File) {


        var alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        alert.setMessage(getString(R.string.upload_file))
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            // finish()

            var images: ArrayList<MultipartBody.Part>? = ArrayList<MultipartBody.Part>()
            val filePart = MultipartBody.Part.createFormData(file_image!!.getName(), file_image!!.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file_image))
            images!!.add(filePart)

            if (images!!.isNotEmpty()) {
                if ((NetworkHelper.isOnline(this))) {
                    progressdialog!!.show()
                    viewmodel!!.uploadAttachment(images, sendModel.attachment_id, event_type).subscribe(Observer_upload)
                } else Snackbar.make(recycler_home, getString(R.string.noInternet), Snackbar.LENGTH_SHORT).show()
            } else Snackbar.make(recycler_home, getString(R.string.no_images), Snackbar.LENGTH_SHORT).show()

        }

        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }

    private fun checkFileType(value: String): Boolean =
            when (value) {
                ApprovedFileTypes.DOC.value,
                ApprovedFileTypes.PDF.value,
                ApprovedFileTypes.JPG.value,
                ApprovedFileTypes.PNG.value,
                ApprovedFileTypes.DOCX.value,
                ApprovedFileTypes.TXT.value,
                ApprovedFileTypes.XLS.value,
                ApprovedFileTypes.XLSX.value -> {
                    true
                }
                else -> false
            }


    enum class ApprovedFileTypes constructor(val value: String) {
        PDF("application/pdf"),
        JPG("image/jpeg"),
        PNG("image/png"),
        DOC("application/msword"),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        TXT("text/plain"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        XLS("application/vnd.ms-excel");
    }

    fun getMimeType(uri: Uri): String {
        var mimeType: String? = null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            val cr: ContentResolver = applicationContext.contentResolver
            mimeType = cr.getType(uri);
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private val Observer_upload: Observer<FileAttachmentResponse>
        get() = object : Observer<FileAttachmentResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                progressdialog!!.dismiss()
                Snackbar.make(recycler_home, getString(R.string.some_error), Snackbar.LENGTH_SHORT).show()
                e?.printStackTrace()
            }

            override fun onNext(t: FileAttachmentResponse) {
                progressdialog!!.dismiss()
                if (t.status == "Success") {
                    OfflineAttachmentModel().delete { realmQuery -> realmQuery.equalTo("att_map_id", sendModel.attachment_id) }
                    t.data.saveAll()
                    /* val intent = Intent()
                     intent.putExtra(TeacherCommunicationPage.attachment_list_key, t.data[0].att_map_id!!)
                     setResult(Activity.RESULT_OK, intent)
                     finish()*/

                    sendModel.attachment_id = t.data[0].att_map_id!!

                    refreshAttachments()
                    showAttachedFiles()
                } else {
                    Snackbar.make(recycler_home, getString(R.string.some_error), Snackbar.LENGTH_SHORT).show()
                }

            }
        }

}





