package com.denobili.app.helper_utils
import android.view.View

object ViewExtension {

    fun View.invisible(){
        this.visibility=View.GONE
    }

    fun View.visible(){
        this.visibility=View.VISIBLE
    }

}