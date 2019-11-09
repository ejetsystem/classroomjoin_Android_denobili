package com.classroomjoin.app.addTemplatePage

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.LocalBroadcastManager
import android.view.Menu
import android.view.MenuItem
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DialogExtensions.showErrorDialog
import com.classroomjoin.app.templatePage.Template
import kotlinx.android.synthetic.main.add_template_page.*
import org.jetbrains.anko.AnkoLogger
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class AddTemplatePage : LocalizationActivity(), AnkoLogger {

    private var doneallbutton: MenuItem? = null
    private var type: Int = 1
    private var id: Int = 0
    private val viewmodel by lazy {
        TemplateViewModel(this@AddTemplatePage)
    }

    private val progressDialog by lazy {
        DialogUtil.showProgressDialog(this@AddTemplatePage)
    }

    companion object TEMPLATE_TYPE {
        var template_type_key = "com.classroom.android.template_type"
        var template_edit_id = "com.classroom.android.template.id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_template_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.addtemplate))

        if (intent.hasExtra(template_type_key)) {
            type = intent.getIntExtra(template_type_key, 0)
            if (type > 3) {
                template_heading_input.isEnabled = false
                template_heading_input.isFocusableInTouchMode = false
            }
            if (intent.hasExtra(template_edit_id)) {
                id = intent.getIntExtra(template_edit_id, 0)
                viewmodel.getTemplate(id, type).subscribe(getobserver)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        doneallbutton = menu.findItem(R.id.action_bar_done_all)
        return true
    }

    private val observer = object : Observer<TemplateResponse> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            this@AddTemplatePage.showErrorDialog(getString(R.string.some_error), this@AddTemplatePage::addTemplate)
            e.printStackTrace()
        }

        override fun onNext(event: TemplateResponse) {
            progressDialog.dismiss()
            if (event.status == "Success") {
                finish()
                val intentqq = Intent("temp_refresh")
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentqq)
            } else this@AddTemplatePage.showErrorDialog(event.error_message, this@AddTemplatePage::addTemplate)
        }
    }

    private val editobserver = object : Observer<TemplateResponse> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            this@AddTemplatePage.showErrorDialog(getString(R.string.some_error), this@AddTemplatePage::addTemplate)
            e.printStackTrace()
        }

        override fun onNext(event: TemplateResponse) {
            progressDialog.dismiss()
            if (event.status == "Success") {
                viewmodel.updateItem(getEditTemplateModel())
                val intentqq = Intent("temp_refresh")
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentqq)
                finish()
            } else this@AddTemplatePage.showErrorDialog(event.error_message, this@AddTemplatePage::addTemplate)
        }
    }

    private val deleteObserver = object : Observer<TemplateResponse> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            this@AddTemplatePage.showErrorDialog(getString(R.string.some_error), this@AddTemplatePage::addTemplate)
            e.printStackTrace()
        }

        override fun onNext(event: TemplateResponse) {
            progressDialog.dismiss()
            if (event.status == "Success") {
                viewmodel.deleteItemFromDatabase(id, type)
                val intentqq = Intent("temp_refresh")
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentqq)
                finish()
            } else this@AddTemplatePage.showErrorDialog(event.error_message, this@AddTemplatePage::addTemplate)
        }
    }

    private val getobserver = object : Observer<List<Template>> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(list: List<Template>) {
            template_heading_input.setText(list[0].subject)
            template_text_input.setText(list[0].message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_done_menu, menu)
        return true
    }

    private fun addTemplate() {
        if (NetworkHelper.isOnline(this@AddTemplatePage)) {
            progressDialog.show()
            if (id == 0) viewmodel.addTemplate(getTemplateModel()).subscribe(observer)
            else viewmodel.editTemplate(getEditTemplateModel()).subscribe(editobserver)
        } else
            showErrorDialog(getString(R.string.noInternet), this@AddTemplatePage::addTemplate)
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == doneallbutton!!.itemId) {
            var proceed = true
            if (template_heading_input.isInvalid(template_heading_layout, getString(R.string.template_heading_error))) proceed = false
            if (template_text_input.text.isEmpty()) {
                template_text_input.error = getString(R.string.template_text_error)
                proceed = false
            }
            if (proceed) addTemplate()
        }
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.action_bar_delete) {
            if (id != 0 && type <= 3) {
                progressDialog.show()
                viewmodel.deleteTemplate(id).subscribe(deleteObserver)
            } else if (type > 3) {
                Snackbar.make(template_text_layout,
                        getString(R.string.cannot_delete_attendance),
                        Snackbar.LENGTH_SHORT).show()
            } else
                Snackbar.make(template_text_layout,
                        getString(R.string.cannot_delete),
                        Snackbar.LENGTH_SHORT).show()
        }
        return true
    }

    private fun TextInputEditText.isInvalid(textInputLayout: TextInputLayout, error: String): Boolean {
        return if (this.text.isNullOrBlank()) {
            textInputLayout.error = error
            true
        } else false
    }

    private fun getTemplateModel(): AddTemplateModel {
        return AddTemplateModel(
                template_text_input.text.toString(),
                template_heading_input.text.toString(),
                type.toString(), viewmodel.org_id, viewmodel.admin_id,
                viewmodel.account_id, getCurrentDate()
        )

    }

    private fun getEditTemplateModel(): Template {
        return Template(
                id,
                type,
                viewmodel.account_id.toInt(),
                viewmodel.admin_id.toInt(),
                viewmodel.org_id.toInt(),
                template_text_input.text.toString(),
                template_heading_input.text.toString()
        )

    }


}

