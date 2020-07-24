package com.denobili.app.helper

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AlertDialog
import com.denobili.app.R

object DialogUtil {



    fun showProgressDialog(context: Context): ProgressDialog {
        val pDialog = ProgressDialog(context)
        pDialog.setTitle("Connecting to Server")
        pDialog.setMessage("Please wait while we are Connecting to Server...")
        pDialog.setCancelable(false)
        return pDialog
    }

    fun showerrorDialog(message: String?,function:()->Unit,context: Context){
        var alert = AlertDialog.Builder(context)
        alert.setTitle(context.getString(R.string.error))
        alert.setMessage(message)
        alert.setPositiveButton(context.getString(R.string.btn_retry)) {
            dialog, whichButton ->  function()
            dialog.dismiss()
        }
        alert.setNegativeButton(context.getString(R.string.btn_cancel)){
            dialog, whichButton ->  dialog.dismiss()
        }
        alert.show()
    }





}
