package com.denobili.app.helper_utils;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import io.realm.Realm;

public abstract class Interactor {


    @Inject
    ApiInterface apiInterface;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Realm realm;

    private static final String ACCESSTOKENKEY = "com.classroom.accesstoken";
    private static final String USERIDKEY = "com.classroom.account_id";
    private static final String ORGID_KEY = "com.classroom.org_id";
    private static final String ACCOUNT_ID_KEY = "com.classroom.account.id";
    private static final String TEACHER_ID_KEY = "com.classroon.teacher.id";
    private static final String USERTYPE_KEY = "com.classroom.setting.usertype";
    private static final String ADMIND_ID_KEY = "com.classroom.setting.admin_id_key";
    private static final String USERNAME_KEY = "com.classroom.setting.username";
    private static final String USER_EMAIL_KEY = "com.classroom.setting.useremail";
    private static final String USER_SMS_KEY = "com.classroom.setting.sms";
    private static final String USER_MAIL_KEY = "com.classroom.setting.mail";
    private static final String USER_DIARY_KEY = "com.classroom.setting.diary";
    private static final String USER_SOCIALGRADE_KEY = "com.classroom.setting.social";
    private static final String USER_MESSAGE_KEY = "com.classroom.setting.message";
    private static final String USER_MOBILE_KEY = "com.classroom.setting.usermobile";

    private static final String STUDENT_ID = "com.classroom.setting.student_id";
    private static final String StUDENT_NAME = "com.classroom.setting.student_name";
    private static final String PROFILE_URL = "com.classroom.setting.profile_url";
    private static final String isAdminKey = "com.classroom.setting.isadmin";
    private static final String CLASS_NAME_KEY = "com.classroom.setting.classname";


    public Interactor(Context context) {
        ((Myapp) context.getApplicationContext()).getApiInterface().inject(this);
    }


    private String accesstoken;
    private String userid;
    private String name;
    private String account_id;
    private String org_id;
    private String teacher_id;
    private int user_type;
    private String admin_id;
    private String user_email;
    private String sms;
    private String diary;
    private String message;
    private String socialGrade;
    private String email;
    private String user_name;
    private String student_name;
    private String student_id;
    private String profile_url;
    private boolean isAdmin;
    private String classname;

    public String getClassname() {
        return sharedPreferences.getString(CLASS_NAME_KEY, null);

    }

    public void setClassname(String classname) {
        sharedPreferences.edit().putString(CLASS_NAME_KEY, classname).apply();
    }

    public boolean isAdmin() {
        return sharedPreferences.getBoolean(isAdminKey, false);
    }

    public void setAdmin(boolean admin) {
        sharedPreferences.edit().putBoolean(isAdminKey, admin).apply();
    }

    public String getProfile_url() {
        return sharedPreferences.getString(PROFILE_URL, null);
    }

    public void setProfile_url(String profile_url) {
        sharedPreferences.edit().putString(PROFILE_URL, profile_url).apply();
    }

    public String getStudent_name() {
        return sharedPreferences.getString(StUDENT_NAME, null);
    }

    public void setStudent_name(String student_name) {
        sharedPreferences.edit().putString(StUDENT_NAME, student_name).apply();

    }

    public String getStudent_id() {
        return sharedPreferences.getString(STUDENT_ID, null);
    }

    public void setStudent_id(String student_id) {
        sharedPreferences.edit().putString(STUDENT_ID, student_id).apply();
    }

    public String getTeacher_id() {
        return sharedPreferences.getString(TEACHER_ID_KEY, null);
    }

    public int getUser_type() {
        return sharedPreferences.getInt(USERTYPE_KEY, 1);
    }

    public void setUser_type(int user_type) {
        sharedPreferences.edit().putInt(USERTYPE_KEY, user_type).apply();
    }

    public void setTeacher_id(String teacher_id) {
        sharedPreferences.edit().putString(TEACHER_ID_KEY, teacher_id).apply();
    }

    public String getSms_id() {
        return sharedPreferences.getString(USER_SMS_KEY, null);
    }

    public void setSms_id(String sms_id) {
        sharedPreferences.edit().putString(USER_SMS_KEY, sms_id).apply();
    }
    public String getMail_id() {
        return sharedPreferences.getString(USER_MAIL_KEY, null);
    }

    public void setMail_id(String mail_id) {
        sharedPreferences.edit().putString(USER_MAIL_KEY, mail_id).apply();
    }
    public String getDiary_id() {
        return sharedPreferences.getString(USER_DIARY_KEY, null);
    }

    public void setDiary_id(String diary_id) {
        sharedPreferences.edit().putString(USER_DIARY_KEY, diary_id).apply();
    }
    public String getSocialGrade() {
        return sharedPreferences.getString(USER_SOCIALGRADE_KEY, null);
    }

    public void setSocialGrade(String socialg_id) {
        sharedPreferences.edit().putString(USER_SOCIALGRADE_KEY, socialg_id).apply();
    }
    public String getMessage_id() {
        return sharedPreferences.getString(USER_DIARY_KEY, null);
    }

    public void setMessage_id(String message_id) {
        sharedPreferences.edit().putString(USER_MESSAGE_KEY, message_id).apply();
    }
    public String getOrg_id() {
        return sharedPreferences.getString(ORGID_KEY, null);
    }

    public void setOrg_id(String org_id) {
        sharedPreferences.edit().putString(ORGID_KEY, org_id).apply();
    }

    public String getAdmin_id() {
        return sharedPreferences.getString(ADMIND_ID_KEY, null);
    }


    public String getUser_email() {
        return sharedPreferences.getString(USER_EMAIL_KEY, null);
    }

    public void setUser_email(String user_email) {
        sharedPreferences.edit().putString(USER_EMAIL_KEY, user_email).apply();
    }

    public String getUser_mobileNo() {
        return sharedPreferences.getString(USER_MOBILE_KEY, null);
    }

    public void setUser_mobileNo(String user_mobileNo) {
        sharedPreferences.edit().putString(USER_MOBILE_KEY, user_mobileNo).apply();
    }

    public String getUser_name() {
        return sharedPreferences.getString(USERNAME_KEY, null);
    }

    public void setUser_name(String user_name) {
        sharedPreferences.edit().putString(USERNAME_KEY, user_name).apply();
    }

    public void setAdmin_id(String id) {
        sharedPreferences.edit().putString(ADMIND_ID_KEY, id).apply();
    }

    public void setAccount_id(String account_id) {
        sharedPreferences.edit().putString(ACCOUNT_ID_KEY, account_id).apply();
    }

    public String getAccount_id() {
        return sharedPreferences.getString(ACCOUNT_ID_KEY, null);
    }

    public String getAccesstoken() {
        return sharedPreferences.getString(ACCESSTOKENKEY, null);
    }

    public void setAccesstoken(String accesstoken) {
        sharedPreferences.edit().putString(ACCESSTOKENKEY, accesstoken).apply();
    }

    public String getUserid() {
        return sharedPreferences.getString(USERIDKEY, null);
    }

    public void setUserid(String userid) {
        sharedPreferences.edit().putString(USERIDKEY, userid).apply();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Realm getRealm() {
        return realm;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }


}