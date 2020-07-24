package com.denobili.app.classEditPage

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import kotlinx.android.synthetic.main.dialog_add_class.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observer

class EditClassPage : LocalizationActivity(), AnkoLogger {

    companion object EDIT_CLASS {
        const val CLASS_ID_KEY = "com.edit.class.id"
        const val CLASS_NAME_KEY = "com.edit.class.name"
    }

    var classname: String? = null

    private var classmodel: AddClassModel? = AddClassModel()
    private var presenter: EditClassPresenter? = null
    private var progressdialog: ProgressDialog? = null
    private var add: Boolean = true

    private var doneallbutton: MenuItem? = null
    private var delete_button: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_class)

        if (intent.hasExtra(CLASS_ID_KEY)) {
            classmodel?.class_id = intent.getStringExtra(CLASS_ID_KEY)
        }
        if (intent.hasExtra(CLASS_NAME_KEY)) {
            classname = intent.getStringExtra(CLASS_NAME_KEY)
            error { classname.toString() }
            input_class_classname.setText(classname)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.addeditClass))

        if (classmodel?.class_id != null) add = false

        if (!add) supportActionBar?.title = getString(R.string.edit_class)

        MainBus.getInstance().busObservable.ofType(EditClassEvent::class.java).subscribe(eventobserver)
        presenter = EditClassPresenter(this)
        progressdialog = DialogUtil.showProgressDialog(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_done_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        doneallbutton = menu.findItem(R.id.action_bar_done_all)
        delete_button = menu.findItem(R.id.action_bar_delete)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == doneallbutton!!.itemId) {
            if (input_class_classname.text.isNotBlank() && input_class_classname.text.isNotEmpty() && input_class_classname.text.trim().length < 14) {
                classname = input_class_classname.text.toString()
                if (add) addClass()
                else editClass()
            } else if (input_class_classname.text.trim().length > 14) {
                input_class_classname.error = getString(R.string.class_name_length_error)
            } else {
                input_class_classname.error = getString(R.string.class_name_invalid)

            }
        } else if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == delete_button!!.itemId) {
            if (classmodel?.class_id != null) showdeletealert()
            else Snackbar.make(input_class_classname, R.string.item_doesnt_exist, Snackbar.LENGTH_SHORT).show()
        }
        return true
    }

    private fun editClass() {
        if (NetworkHelper.isOnline(this)) {
            progressdialog!!.show()
            classmodel?.name = classname
            presenter!!.postdata(classmodel!!)
        } else
            showerrorDialog2(getString(R.string.noInternet))
    }

    private fun addClass() {
        if (NetworkHelper.isOnline(this)) {
            progressdialog!!.show()
            presenter!!.postdata(AddClassModel(classname))
        } else
            showerrorDialog(getString(R.string.noInternet))
    }

    private fun deleteClass() {
        if (NetworkHelper.isOnline(this)) {
            progressdialog!!.show()
            presenter!!.deleteclass(classmodel?.class_id)
        } else
            showerrorDialog(getString(R.string.noInternet))
    }

    private fun showerrorDialog(message: String?) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            addClass()
        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun showerrorDialog2(message: String?) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            editClass()
        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

    private val eventobserver = object : Observer<EditClassEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: EditClassEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {

                Event.POST_SUCCESS -> sendMessage()

                Event.DELETED_ITEM -> {
                    finish()
                }
                Event.DELETION_FAILURE -> {
                    showerrorDialog2(event.error)
                }

                Event.POST_FAILURE -> showerrorDialog(event.error)
                else -> {
                }
            }
        }
    }

    private fun sendMessage() {
        finish()
        Log.d("sender", "Broadcasting message")
        val intent = Intent("custom-event-name")
        // You can also include some extra data.
        //intent.putExtra("message", "This is my message!")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun showdeletealert() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(R.string.delete_confirm)
        alert.setPositiveButton(R.string.btn_yes) { dialog, whichButton ->
            classmodel?.class_id = classmodel?.class_id
            deleteClass()

        }
        alert.setNegativeButton(R.string.btn_no) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }
}
