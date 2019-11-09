package com.classroomjoin.app.helper_utils

import android.app.Activity
import android.support.v7.app.AlertDialog
import com.classroomjoin.app.R

object DialogExtensions {

    fun Activity.showErrorDialog(message:String, function:()->Unit){
        var alert = AlertDialog.Builder(this)
        alert.setTitle(this.getString(R.string.error))
        alert.setMessage(message)
        alert.setPositiveButton(this.getString(R.string.btn_retry)) {
            dialog, whichButton ->  function()
            dialog.dismiss()
        }
        alert.setNegativeButton(this.getString(R.string.btn_cancel)){
            dialog, whichButton ->  dialog.dismiss()
        }
        alert.show()
    }

    fun <T> Activity.showErrorDialogwithParam(message:String, function:(model: T)->Unit, model: T){
        var alert = AlertDialog.Builder(this)
        alert.setTitle(this.getString(R.string.error))
        alert.setMessage(message)
        alert.setPositiveButton(this.getString(R.string.btn_retry)) {
            dialog, whichButton ->  function(model)
            dialog.dismiss()
        }
        alert.setNegativeButton(this.getString(R.string.btn_cancel)){
            dialog, whichButton ->  dialog.dismiss()
        }
        alert.show()
    }

}