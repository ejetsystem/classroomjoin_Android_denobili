package com.denobili.app.utils

import android.content.Context
import android.content.SharedPreferences

import com.denobili.app.R


class SharedPreferencesData(private val context: Context?) {

    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    val signup_otp: String?
        get() = pref.getString(context!!.getString(R.string.SIGNUP_OTP), null)

    val forgot_otp: String?
        get() = pref.getString(context!!.getString(R.string.FORGOT_OTP), null)

    val user_languafe: String?
        get() = pref.getString(context!!.getString(R.string.USER_LANGUAGE), null)
    val social_email: String?
        get() = pref.getString(context!!.getString(R.string.SOCIAL_EMAIL), null)

    val userTypeId: Int?
        get() = pref.getInt(context!!.getString(R.string.USER_TYPE), -1)

    val socialId: String?
        get() = pref.getString(context!!.getString(R.string.REGISTRATION_TYPE), null)
    val DiaryCount: String?
        get() = pref.getString(context!!.getString(R.string.diary), null)
    val messageCount: String?
        get() = pref.getString(context!!.getString(R.string.message), null)
    val smsCount: String?
        get() = pref.getString(context!!.getString(R.string.sms), null)
    val assignCount: String?
        get() = pref.getString(context!!.getString(R.string.assignement), null)
    val diaryC: Int?
        get() = pref.getInt(context!!.getString(R.string.diary_count), 0)
    val onBoard: String?
        get() = pref.getString(context!!.getString(R.string.WELCOME), null)

    init {
        pref = context!!.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun saveSignupOTP(auth: String) {
        editor.putString(context!!.getString(R.string.SIGNUP_OTP), auth)
        editor.apply()
    }
    fun saveDiary(auth: String) {
        editor.putString(context!!.getString(R.string.diary), auth)
        editor.apply()
    }
    fun saveDiaryCount(auth: Int) {
        editor.putInt(context!!.getString(R.string.diary_count), auth)
        editor.apply()
    }
    fun saveMessage(auth: String) {
        editor.putString(context!!.getString(R.string.message), auth)
        editor.apply()
    }
    fun saveSms(auth: String) {
        editor.putString(context!!.getString(R.string.sms), auth)
        editor.apply()
    }
    fun saveAssign(auth: String) {
        editor.putString(context!!.getString(R.string.assignement), auth)
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
    fun saveEmailId(auth: String) {
        editor.putString(context!!.getString(R.string.SOCIAL_EMAIL), auth)
        editor.apply()
    }
    fun welCome(auth: String) {
        editor.putString(context!!.getString(R.string.WELCOME), auth)
        editor.apply()
    }


}
