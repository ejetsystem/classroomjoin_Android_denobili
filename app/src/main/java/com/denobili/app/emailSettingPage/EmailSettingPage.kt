package com.denobili.app.emailSettingPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.DialogExtensions.showErrorDialogwithParam
import com.denobili.app.helper_utils.ViewExtension.visible
import com.denobili.app.smsCommunicationSettingPage.CommunicationSettingViewmodel
import kotlinx.android.synthetic.main.activity_communication_setting_page_email.*
import kotlinx.android.synthetic.main.setting_view.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.image
import org.jetbrains.anko.onClick
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class EmailSettingPage : LocalizationActivity(), AnkoLogger {

    private val progressdialog: ProgressDialog by lazy {
        DialogUtil.showProgressDialog(this@EmailSettingPage)
    }

    private var emailMain_check :Boolean = false
    private var emailAttendance_check :Boolean= false

    var model: CommunicationSettingModel? = null
    private var gmail_id: Int? = null
    var send_grid_id: Int? = null

    private val send_grid_vender_id = 1
    private val gmail_vendor_id = 2

    private val viewmodel by lazy {
        CommunicationSettingViewmodel(this@EmailSettingPage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication_setting_page_email)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.email_communication_setting))



        gmail_settings.check_settings.onClick {


            setCommunication(ActiveModel(this.gmail_id!!,
                    if (gmail_settings.chkState_settings.isChecked) 0 else 1))
        }

        send_grid_settings.check_settings.onClick {
            setCommunication(ActiveModel(send_grid_id!!,
                    if (send_grid_settings.chkState_settings.isChecked) 0 else 1))
        }

        gmail_settings.save_settings.onClick {
            if (gmail_settings.validate())
                postCommunicationSetting(getClassroomSettings())
        }

        send_grid_settings.save_settings.onClick {
            if (send_grid_settings.validate())
                postCommunicationSetting(getTwillioSettings())
        }

        send_grid_settings.checkBox_one.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                //Do Whatever you want in isChecked
                emailMain_check=true

            }else
                emailMain_check=false

        }

        send_grid_settings.checkBox_two.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                emailAttendance_check=true
            }else
                emailAttendance_check=false
        }
        viewmodel.getdata("1").subscribe(getObserver)
    }

    private val getObserver = object : Observer<CommunicationSettingResponse> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            if (progressdialog.isShowing) progressdialog.dismiss()
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
                    if (progressdialog.isShowing) progressdialog.dismiss()
                    this@EmailSettingPage
                            .showErrorDialogwithParam(getString(R.string.some_error),
                                    this@EmailSettingPage::postCommunicationSetting,
                                    model)
                    e.printStackTrace()
                }

                override fun onNext(response: CommunicationSettingAddResponse) {
                    if (progressdialog.isShowing) progressdialog.dismiss()
                   if (response.status == "Success")
                        //showresults(response.data)
                       viewmodel.getdata("1").subscribe(getObserver)

                   else this@EmailSettingPage
                            .showErrorDialogwithParam(response.error_message,
                                    this@EmailSettingPage::postCommunicationSetting,
                                    model)
                }
            }

    private fun activateObserver(model: ActiveModel) = object : Observer<CommunicationSettingDeactivate> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            if (progressdialog.isShowing) progressdialog.dismiss()
            showErrorDialogwithParam(getString(R.string.some_error),
                    this@EmailSettingPage::setCommunication, model)
            e.printStackTrace()
        }

        override fun onNext(response: CommunicationSettingDeactivate) {
            if (progressdialog.isShowing) progressdialog.dismiss()
            if (response.status == "Success") {
                if (model.id == send_grid_id) {
                    if (model.flag == 1) {
                        send_grid_settings.activate()
                    } else {
                        send_grid_settings.deactivate()
                    }
                } else {
                    if (model.flag == 0) {
                        gmail_settings.deactivate()
                    } else {
                        gmail_settings.activate()
                    }
                }
            } else showErrorDialogwithParam(response.error_message,
                    this@EmailSettingPage::setCommunication, model)
        }
    }

    private fun getClassroomSettings(): CommunicationSettingModel =
            CommunicationSettingModel(
                    gmail_settings.getUsername(),
                    gmail_settings.getPassword(),
                    gmail_settings.getSenderId(),
                    viewmodel.org_id,
                    this.gmail_id,
                    viewmodel.account_id,
                    "1",
                    send_grid_vender_id,
                    getCurrentDate(),false,false,
                    emailMain_check,emailAttendance_check,false,false,false)

    private fun getTwillioSettings(): CommunicationSettingModel =
            CommunicationSettingModel(
                    send_grid_settings.getUsername(),
                    send_grid_settings.getPassword(),
                    send_grid_settings.getSenderId(),
                    viewmodel.org_id,
                    send_grid_id,
                    viewmodel.account_id,
                    "1",
                    gmail_vendor_id,
                    getCurrentDate()
                    ,false,false,
                    emailMain_check,emailAttendance_check,false,false,false)

    private fun showresults(model: List<CommunicationModel>) {
        showcontent()
        model.forEachIndexed { index, communicationModel ->
            if (communicationModel.vendor_id == gmail_vendor_id) {
                send_grid_id = communicationModel.id
                send_grid_settings.setData(communicationModel.username,
                        communicationModel.password,
                        communicationModel.senderid)
                send_grid_settings.save()
                send_grid_settings.showSwitch()
                if (communicationModel.active_flag == 1) {
                    send_grid_settings.activate()
                }

                if (communicationModel.emailMain == true)
                    send_grid_settings.checkBox_one.isChecked=true
                else
                    send_grid_settings.checkBox_one.isChecked=false

                if (communicationModel.emailAttendance == true)
                    send_grid_settings.checkBox_two.isChecked=true
                else
                    send_grid_settings.checkBox_two.isChecked=false



            } else {
                this.gmail_id = communicationModel.id
                gmail_settings.setData(communicationModel.username,
                        communicationModel.password,
                        communicationModel.senderid)
                gmail_settings.save()
                gmail_settings.showSwitch()
                if (communicationModel.active_flag == 1) {
                    gmail_settings.activate()
                }


                if (communicationModel.emailMain == true)
                    send_grid_settings.checkBox_one.isChecked=true
                else
                    send_grid_settings.checkBox_one.isChecked=false

                if (communicationModel.emailAttendance == true)
                    send_grid_settings.checkBox_two.isChecked=true
                else
                    send_grid_settings.checkBox_two.isChecked=false
            }
        }
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
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
        if (NetworkHelper.isOnline(this@EmailSettingPage)) {
            progressdialog.show()
            if (model.comm_id != null)
                viewmodel.editData(model).subscribe(editObserver(model))
            else viewmodel.addData(model).subscribe(editObserver(model))
        } else
            showErrorDialogwithParam(getString(R.string.noInternet), this::postCommunicationSetting, model)
    }

    private fun setCommunication(model: ActiveModel) {
        if (NetworkHelper.isOnline(this@EmailSettingPage)) {
            progressdialog.show()
            viewmodel.activate(model.id
                    , model.flag).subscribe(activateObserver(model))
        } else
            showErrorDialogwithParam(getString(R.string.noInternet),
                    this@EmailSettingPage::setCommunication, model)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    data class ActiveModel(val id: Int, val flag: Int)


}
