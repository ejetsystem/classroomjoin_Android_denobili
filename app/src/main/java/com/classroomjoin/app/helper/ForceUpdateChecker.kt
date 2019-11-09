package com.classroomjoin.app.helper

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class ForceUpdateChecker(private val context: Context,
                         private val onUpdateNeededListener: OnUpdateNeededListener?) {

    interface OnUpdateNeededListener {
        fun onUpdateNeeded(updateUrl: String)
    }

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            val currentVersionCode = Integer.parseInt(remoteConfig.getString(KEY_CURRENT_VERSIONCODE))

            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
            val appVersionCode = getAppVersionCode(context)

            if (currentVersionCode > appVersionCode && onUpdateNeededListener != null) {
                onUpdateNeededListener.onUpdateNeeded(updateUrl)
            }
        }
    }


    private fun getAppVersionCode(context: Context): Int {
        var result = -1

        try {
            result = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message)
        }

        return result
    }

    class Builder(private val context: Context) {
        private var onUpdateNeededListener: OnUpdateNeededListener? = null

        fun onUpdateNeeded(onUpdateNeededListener: OnUpdateNeededListener): Builder {
            this.onUpdateNeededListener = onUpdateNeededListener
            return this
        }

        fun build(): ForceUpdateChecker {
            return ForceUpdateChecker(context, onUpdateNeededListener)
        }

        fun check(): ForceUpdateChecker {
            val forceUpdateChecker = build()
            forceUpdateChecker.check()
            return forceUpdateChecker
        }
    }

    companion object {

        private val TAG = ForceUpdateChecker::class.java.simpleName

        val KEY_UPDATE_REQUIRED = "force_update_required"
        val KEY_UPDATE_URL = "force_update_store_url"
        val KEY_CURRENT_VERSIONCODE = "force_update_current_versionCode"

        fun with(context: Context): Builder {
            return Builder(context)
        }
    }
}