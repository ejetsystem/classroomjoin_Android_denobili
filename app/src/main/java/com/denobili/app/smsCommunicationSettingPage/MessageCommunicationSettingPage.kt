package com.denobili.app.smsCommunicationSettingPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.emailSettingPage.*
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.DialogExtensions.showErrorDialogwithParam
import com.denobili.app.helper_utils.ViewExtension.visible
import kotlinx.android.synthetic.main.activity_communication_setting_sms.*
import kotlinx.android.synthetic.main.message_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.image
import org.jetbrains.anko.onClick
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class MessageCommunicationSettingPage : LocalizationActivity(), AnkoLogger {

    private val progressdialog: ProgressDialog by lazy {
        DialogUtil.showProgressDialog(this@MessageCommunicationSettingPage)
    }

    var model: CommunicationSettingModel? = null
    var clickatell_id: Int? = null
    var twillio_id: Int? = null

    val clickatell_vendorId = 2
    val twillio_vendorid = 1

    private var messageMain: Boolean = false
    private var messageAttendancePresent: Boolean = false
    private var messageAttendanceAbsent: Boolean = false

    private val viewmodel by lazy {
        CommunicationSettingViewmodel(this@MessageCommunicationSettingPage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.sms_communication_setting))


        save_message_settings.onClick {
            postCommunicationSetting(getTwillioSettings())
        }


        checkBox_message_main.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                //Do Whatever you want in isChecked
                messageMain = true

            } else
                messageMain = false

        }

        checkBox_present.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                messageAttendancePresent = true
            } else
                messageAttendancePresent = false
        }

        checkBox_absent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                messageAttendanceAbsent = true
            } else
                messageAttendanceAbsent = false
        }


        viewmodel.getdata("3").subscribe(getObserver)
    }

    private val getObserver = object : Observer<CommunicationSettingResponse> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            showError(getString(R.string.some_error))
            e.printStackTrace()
        }

        override fun onNext(response: CommunicationSettingResponse) {
            if (progressdialog.isShowing) progressdialog.dismiss()
            if (response.status == "Success")
                showresults(response.data)
            else showError(response.error_message)
        }
    }

    private fun editObserver(model: CommunicationSettingModel) =
            object : Observer<CommunicationSettingAddResponse> {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    this@MessageCommunicationSettingPage
                            .showErrorDialogwithParam(getString(R.string.some_error),
                                    this@MessageCommunicationSettingPage::postCommunicationSetting,
                                    model)
                    e.printStackTrace()
                }

                override fun onNext(response: CommunicationSettingAddResponse) {
                    if (progressdialog.isShowing) progressdialog.dismiss()
                    if (response.status == "Success")
                    // showresults(response.data)
                        viewmodel.getdata("3").subscribe(getObserver)
                    else this@MessageCommunicationSettingPage
                            .showErrorDialogwithParam(response.error_message,
                                    this@MessageCommunicationSettingPage::postCommunicationSetting,
                                    model)
                }
            }

    private fun activateObserver(model: ActiveModel) = object : Observer<CommunicationSettingDeactivate> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            showErrorDialogwithParam(getString(R.string.some_error),
                    this@MessageCommunicationSettingPage::setCommunication, model)
            e.printStackTrace()
        }

        override fun onNext(response: CommunicationSettingDeactivate) {
            if (progressdialog.isShowing) progressdialog.dismiss()
            if (response.status == "Success") {
                if (model.id == twillio_id) {
                    if (model.flag == 1) {
                        twillio_settings.activate()
                    } else {
                        twillio_settings.deactivate()
                    }
                } else {
                    if (model.flag == 0) {
                        classroom_settings_custom.deactivate()
                    } else {
                        classroom_settings_custom.activate()
                    }
                }
            } else showErrorDialogwithParam(response.error_message,
                    this@MessageCommunicationSettingPage::setCommunication, model)
        }
    }

    fun getClassroomSettings(): CommunicationSettingModel =
            CommunicationSettingModel(
                    "",
                    "",
                    "",
                    viewmodel.org_id,
                    clickatell_id,
                    viewmodel.account_id,
                    "3",
                    clickatell_vendorId,
                    getCurrentDate(), false, false, false, false, messageAttendancePresent, messageAttendanceAbsent, messageMain)

    fun getTwillioSettings(): CommunicationSettingModel =
            CommunicationSettingModel(
                    "",
                    "",
                    "",
                    viewmodel.org_id,
                    twillio_id,
                    viewmodel.account_id,
                    "3",
                    twillio_vendorid,
                    getCurrentDate(), false, false, false, false, messageAttendancePresent, messageAttendanceAbsent, messageMain)

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }

    private fun showresults(model: List<CommunicationModel>) {
        showcontent()
        model.forEachIndexed { index, communicationModel ->

            twillio_id = communicationModel.id

            if (communicationModel.messageMain == true)
                checkBox_message_main.isChecked = true
            else
                checkBox_message_main.isChecked = false

            if (communicationModel.messageAttendancePresent == true)
                checkBox_present.isChecked = true
            else
                checkBox_present.isChecked = false

            if (communicationModel.messageAttendanceAbsent == true)
                checkBox_absent.isChecked = true
            else
                checkBox_absent.isChecked = false


        }
    }

    private fun showcontent() {
        comm_detail_content_loading_msg.hide()
        comm_detail_imagevieew_msg.visibility = View.GONE
        comm_detail_textview_msg.visibility = View.GONE
        // settings_detail.visibility = View.VISIBLE
    }

    private fun showError(error: String) {
        comm_detail_content_loading_msg.hide()
        comm_detail_imagevieew_msg.image = AppCompatResources.getDrawable(this, R.drawable.ic_error_black_24dp)
        comm_detail_textview_msg.text = error
        comm_detail_imagevieew_msg.visible()
        comm_detail_textview_msg.visible()
    }

    fun postCommunicationSetting(model: CommunicationSettingModel) {
        if (NetworkHelper.isOnline(this@MessageCommunicationSettingPage)) {
            progressdialog.show()
            if (model.comm_id != null) viewmodel.editData(model).subscribe(editObserver(model))
            else viewmodel.addData(model).subscribe(editObserver(model))
        } else
            showErrorDialogwithParam(getString(R.string.noInternet), this::postCommunicationSetting, model)
    }

    private fun setCommunication(model: ActiveModel) {
        if (NetworkHelper.isOnline(this@MessageCommunicationSettingPage)) {
            progressdialog.show()
            viewmodel.activate(model.id
                    , model.flag).subscribe(activateObserver(model))
        } else
            showErrorDialogwithParam(getString(R.string.noInternet),
                    this@MessageCommunicationSettingPage::setCommunication, model)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    data class ActiveModel(val id: Int, val flag: Int)
}
