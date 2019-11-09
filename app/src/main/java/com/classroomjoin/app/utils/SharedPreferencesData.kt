package com.classroomjoin.app.utils

import android.content.Context
import android.content.SharedPreferences

import com.classroomjoin.app.R


class SharedPreferencesData(private val context: Context?) {

    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    val signup_otp: String?
        get() = pref.getString(context!!.getString(R.string.SIGNUP_OTP), null)

    val forgot_otp: String?
        get() = pref.getString(context!!.getString(R.string.FORGOT_OTP), null)

    val user_languafe: String?
        get() = pref.getString(context!!.getString(R.string.USER_LANGUAGE), null)

    val userTypeId: Int?
        get() = pref.getInt(context!!.getString(R.string.USER_TYPE), -1)

    val socialId: String?
        get() = pref.getString(context!!.getString(R.string.REGISTRATION_TYPE), null)

    init {
        pref = context!!.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun saveSignupOTP(auth: String) {
        editor.putString(context!!.getString(R.string.SIGNUP_OTP), auth)
        editor.apply()
    }

    fun saveForgotOTP(auth: String) {
        editor.putString(context!!.getString(R.string.FORGOT_OTP), auth)
        editor.apply()
    }

    fun saveUserTypeId(auth: Int) {
        editor.putInt(context!!.getString(R.string.USER_TYPE), auth)
        editor.apply()
    }

    fun saveLanguage(auth: String) {
        editor.putString(context!!.getString(R.string.USER_LANGUAGE), auth)
        editor.apply()
    }

    fun saveSocialId(auth: String) {
        editor.putString(context!!.getString(R.string.REGISTRATION_TYPE), auth)
        editor.apply()
    }



}
