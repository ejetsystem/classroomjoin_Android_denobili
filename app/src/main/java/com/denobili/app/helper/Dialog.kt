package com.denobili.app.helper

import android.app.Activity
import android.support.v7.app.AlertDialog
import com.denobili.app.R


open class Dialog {

    companion object

    public fun Activity.showErrorDialog(message:String, function:()->Unit){
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
}