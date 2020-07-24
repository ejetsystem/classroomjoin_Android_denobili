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
import kotlinx.android.synthetic.main.setting_view.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.image
import org.jetbrains.anko.onClick
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class SmsCommunicationSettingPage : LocalizationActivity(), AnkoLogger {

    private val progressdialog: ProgressDialog by lazy {
        DialogUtil.showProgressDialog(this@SmsCommunicationSettingPage)
    }

    var model: CommunicationSettingModel? = null
    var clickatell_id: Int? = null
    var twillio_id: Int? = null

    val clickatell_vendorId = 2
    val twillio_vendorid = 1

    private var smsMain_check :Boolean = false
    private var smsAttendance_check :Boolean= false

    private val viewmodel by lazy {
        CommunicationSettingViewmodel(this@SmsCommunicationSettingPage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication_setting_sms)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.sms_communication_setting))

        classroom_settings_custom.check_settings.onClick {
            setCommunication(ActiveModel(clickatell_id!!,
                    if (classroom_settings_custom.chkState_settings.isChecked) 0 else 1))
        }

        twillio_settings.check_settings.onClick {
            setCommunication(ActiveModel(twillio_id!!,
                    if (twillio_settings.chkState_settings.isChecked) 0 else 1))
        }

        classroom_settings_custom.save_settings.onClick {
            if (classroom_settings_custom.validate())
                postCommunicationSetting(getClassroomSettings())
        }

        twillio_settings.save_settings.onClick {
            if (twillio_settings.validate())
                postCommunicationSetting(getTwillioSettings())
        }

        twillio_settings.checkBox_one.text="SMS"
        twillio_settings.checkBox_two.text="Attendance SMS"

        classroom_settings_custom.checkBox_one.text="SMS"
        classroom_settings_custom.checkBox_two.text="Attendance SMS"

        classroom_settings_custom.checkBox_one.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                //Do Whatever you want in isChecked
                smsMain_check=true

            }else
                smsMain_check=false

        }

        classroom_settings_custom.checkBox_two.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                smsAttendance_check=true
            }else
                smsAttendance_check=false
        }

        viewmodel.getdata("2").subscribe(getObserver)
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
                    this@SmsCommunicationSettingPage
                            .showErrorDialogwithParam(getString(R.string.some_error),
                                    this@SmsCommunicationSettingPage::postCommunicationSetting,
                                    model)
                    e.printStackTrace()
                }

                override fun onNext(response: CommunicationSettingAddResponse) {
                    if (progressdialog.isShowing) progressdialog.dismiss()
                    if (response.status == "Success")
                       // showresults(response.data)
                        viewmodel.getdata("2").subscribe(getObserver)

                    else this@SmsCommunicationSettingPage
                            .showErrorDialogwithParam(response.error_message,
                                    this@SmsCommunicationSettingPage::postCommunicationSetting,
                                    model)
                }
            }

    private fun activateObserver(model: ActiveModel) = object : Observer<CommunicationSettingDeactivate> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            showErrorDialogwithParam(getString(R.string.some_error),
                    this@SmsCommunicationSettingPage::setCommunication, model)
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
                    this@SmsCommunicationSettingPage::setCommunication, model)
        }
    }

    fun getClassroomSettings(): CommunicationSettingModel =
            CommunicationSettingModel(
                    classroom_settings_custom.getUsername(),
                    classroom_settings_custom.getPassword(),
                    classroom_settings_custom.getSenderId(),
                    viewmodel.org_id,
                    clickatell_id,
                    viewmodel.account_id,
                    "2",
                    clickatell_vendorId,
                    getCurrentDate(),smsAttendance_check,smsMain_check,false,false,false,false,false)

    fun getTwillioSettings(): CommunicationSettingModel =
            CommunicationSettingModel(
                    twillio_settings.getUsername(),
                    twillio_settings.getPassword(),
                    twillio_settings.getSenderId(),
                    viewmodel.org_id,
                    twillio_id,
                    viewmodel.account_id,
                    "2",
                    twillio_vendorid,
                    getCurrentDate(),smsAttendance_check,smsMain_check,false,false,false,false,false)

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }

    private fun showresults(model: List<CommunicationModel>) {
        showcontent()
        model.forEachIndexed { index, communicationModel ->
            if (communicationModel.vendor_id == twillio_vendorid) {
                twillio_id = communicationModel.id
                twillio_settings.setData(communicationModel.username,
                        communicationModel.password,
                        communicationModel.senderid)
                twillio_settings.save()
                twillio_settings.showSwitch()
                if (communicationModel.active_flag == 1) {
                    twillio_settings.activate()
                }


                if (communicationModel.smsMain == true)
                    classroom_settings_custom.checkBox_one.isChecked=true
                else
                    classroom_settings_custom.checkBox_one.isChecked=false

                if (communicationModel.attendanceSms == true)
                    classroom_settings_custom.checkBox_two.isChecked=true
                else
                    classroom_settings_custom.checkBox_two.isChecked=false

            } else {
                clickatell_id = communicationModel.id
                classroom_settings_custom.setData(communicationModel.username,
                        communicationModel.password,
                        communicationModel.senderid)
                classroom_settings_custom.save()
                classroom_settings_custom.showSwitch()
                if (communicationModel.active_flag == 1) {
                    classroom_settings_custom.activate()
                }


                if (communicationModel.smsMain == true)
                    classroom_settings_custom.checkBox_one.isChecked=true
                else
                    classroom_settings_custom.checkBox_one.isChecked=false

                if (communicationModel.attendanceSms == true)
                    classroom_settings_custom.checkBox_two.isChecked=true
                else
                    classroom_settings_custom.checkBox_two.isChecked=false
            }
        }
    }

    private fun showcontent() {
        comm_detail_content_loading.hide()
        comm_detail_imagevieew.visibility = View.GONE
        comm_detail_textview.visibility = View.GONE
        settings_detail.visibility = View.VISIBLE
    }

    private fun showError(error: String) {
        comm_detail_content_loading.hide()
        comm_detail_imagevieew.image = AppCompatResources.getDrawable(this, R.drawable.ic_error_black_24dp)
        comm_detail_textview.text = error
        comm_detail_imagevieew.visible()
        comm_detail_textview.visible()
    }

    fun postCommunicationSetting(model: CommunicationSettingModel) {
        if (NetworkHelper.isOnline(this@SmsCommunicationSettingPage)) {
            progressdialog.show()
            if (model.comm_id != null) viewmodel.editData(model).subscribe(editObserver(model))
            else viewmodel.addData(model).subscribe(editObserver(model))
        } else
            showErrorDialogwithParam(getString(R.string.noInternet), this::postCommunicationSetting, model)
    }

    private fun setCommunication(model: ActiveModel) {
        if (NetworkHelper.isOnline(this@SmsCommunicationSettingPage)) {
            progressdialog.show()
            viewmodel.activate(model.id
                    , model.flag).subscribe(activateObserver(model))
        } else
            showErrorDialogwithParam(getString(R.string.noInternet),
                    this@SmsCommunicationSettingPage::setCommunication, model)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    data class ActiveModel(val id: Int, val flag: Int)
}
