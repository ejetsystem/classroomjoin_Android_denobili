package com.classroomjoin.app.helper_utils


import com.classroomjoin.app.addSocialGrade.AddSocialGradeResponse
import com.classroomjoin.app.addTemplatePage.AddTemplateModel
import com.classroomjoin.app.addTemplatePage.TemplateResponse
import com.classroomjoin.app.attendancePage.AttendanceApiResponse
import com.classroomjoin.app.attendanceSummaryPage.AttendanceSummaryResponse
import com.classroomjoin.app.classEditPage.AddClassModel
import com.classroomjoin.app.classEditPage.AddClassResponse
import com.classroomjoin.app.classEditPage.DeleteClassResponse
import com.classroomjoin.app.emailSettingPage.CommunicationSettingAddResponse
import com.classroomjoin.app.emailSettingPage.CommunicationSettingDeactivate
import com.classroomjoin.app.emailSettingPage.CommunicationSettingModel
import com.classroomjoin.app.emailSettingPage.CommunicationSettingResponse
import com.classroomjoin.app.loginPage.LoginApiResponse
import com.classroomjoin.app.loginPage.LoginModel
import com.classroomjoin.app.loginPage.signupGoogle.SignInGoogleModel
import com.classroomjoin.app.loginPage.signupGoogle.SignInGoogleReponser
import com.classroomjoin.app.myConnectedStudents.ConnectedStudentApiResponse
import com.classroomjoin.app.mySocialGradesPage.AddSocialGradeModel
import com.classroomjoin.app.mySocialGradesPage.DeleteSocialModel
import com.classroomjoin.app.mySocialGradesPage.MySocialGradesResponse
import com.classroomjoin.app.mystudentsPage.StudentApiResponse
import com.classroomjoin.app.passwordConfirmation.ConfirmPassModel
import com.classroomjoin.app.postAttendancePage.PostAttendence
import com.classroomjoin.app.postAttendancePage.checkAttendance.AttendanceMicroApiResponse
import com.classroomjoin.app.postAttendancePage.checkAttendance.AttendanceMicroModel
import com.classroomjoin.app.profileEditPage.ProfilePostReponse
import com.classroomjoin.app.profilePage.ProfileApiResponse
import com.classroomjoin.app.profilePage.ProfileData
import com.classroomjoin.app.readStatus.ReadStatusModel
import com.classroomjoin.app.readStatus.ReadStatusReponser
import com.classroomjoin.app.signUpPage.SignUpApiResponse
import com.classroomjoin.app.signUpPage.SignUpModel
import com.classroomjoin.app.signUpPage.getOTPsignup.SignupOTPModel
import com.classroomjoin.app.socialGradeSelectionPage.SocialGradeSendModel
import com.classroomjoin.app.studentCodePage.InputCodeModel
import com.classroomjoin.app.studentCodePage.InputStudentCodeResponse
import com.classroomjoin.app.studentCodePage.RegisterFirebaseRequest
import com.classroomjoin.app.studentDetailPage.AddStudentModel
import com.classroomjoin.app.studentDetailPage.AddStudentResponse
import com.classroomjoin.app.studentDetailPage.DeleteStudentResponse
import com.classroomjoin.app.studentDetailPage.StudentDetailsApiResponse
import com.classroomjoin.app.studentDiaryEventDetailPage.StudentDiaryEventApiResponse
import com.classroomjoin.app.studentDiaryPage.StudentDiaryApiResponse
import com.classroomjoin.app.studentEventDetailPage.StudentEventApiResponse
import com.classroomjoin.app.studentInboxPage.StudentInboxApiResponse
import com.classroomjoin.app.studentSocialGradeReportPage.SocialGradeReportResponse
import com.classroomjoin.app.studentallEventsPage.StudentEventsApiResponse
import com.classroomjoin.app.teacherAddClasses.ClassListApiResponse
import com.classroomjoin.app.teacherAttachmentPage.AttachmentResponse
import com.classroomjoin.app.teacherAttachmentPage.FileAttachmentResponse
import com.classroomjoin.app.teacherCommunicationReports.ReportReponse
import com.classroomjoin.app.teacherMessageSendPage.*
import com.classroomjoin.app.teacherSocialGradeReport.SocialGradeClassReportResponse
import com.classroomjoin.app.templatePage.TemplateSelectionApiResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.adapter.rxjava.Result
import retrofit2.http.*
import rx.Observable

interface ApiInterface {

    companion object {

        //Production
        val url = "http://api.classroomjoin.com/app/"


        const val tokenkey = "Authorization"
    }

    //####################################################

    //User validating service in Login
    @FormUrlEncoded
    @POST("checkuser")
    fun validateLoginEmail(
            @Field("userName") userName: String): Observable<JsonObject>

    //####################################################

    // Login service
    @POST("login")
    fun login(
            @Body loginModel: LoginModel): Observable<Result<LoginApiResponse>>

    //####################################################

    // Google login service
    @POST("signupgoogle")
    fun google_signup(
            @Body loginModel: SignInGoogleModel): Observable<SignUpApiResponse>

    //####################################################

    // Signup service
    @POST("signup")
    fun signup(
            @Body signUpModel: SignUpModel): Observable<SignUpApiResponse>

    //####################################################

    //OTP generating service in Forgot password
    @FormUrlEncoded
    @POST("otpgenerate")
    fun forgotpassword(
            @Field("userName") userName: String): Observable<JsonObject>

    //####################################################

    //OTP generating service in Signup
    @POST("otpsendmservices")
    fun validatesignupOTP(
            @Body otPverifyModel: SignupOTPModel): Observable<JsonObject>

    //####################################################


    //Update password service
    @POST("forgotpassword")
    fun confirmpassword(
            @Body otPverifyModel: ConfirmPassModel): Observable<JsonObject>

    //####################################################

    //Teacher - Get all classes list with respect Teacher id
    @GET("teacher/{teacher_id}/{account_id}/class")
    fun getMyClassess(
            @Header(tokenkey) token: String,
            @Path("teacher_id") teacher_id: String,
            @Path("account_id") account_id: String
    ): Observable<ClassListApiResponse>

    //####################################################

    //Teacher - Add a new class
    @POST("class")
    fun addClass(
            @Header(tokenkey) token: String,
            @Body addClassModel: AddClassModel
    ): Observable<AddClassResponse>

    //####################################################

    //Teacher - Edit a class
    @FormUrlEncoded
    @PUT("class")
    fun editClass(
            @Header(tokenkey) token: String,
            @Field("className") classname: String?,
            @Field("classId") classid: String?,
            @Field("updateDate") updateDate: String?,
            @Field("accountManagementId") accountManagementId: Int?
    ): Observable<AddClassResponse>

    //####################################################

    //Teacher - Delete a class
    @DELETE("class/{class_id}")
    fun deleteClass(
            @Header(tokenkey) token: String,
            @Path("class_id") classid: String?
    ): Observable<DeleteClassResponse>

    //####################################################

    //Teacher - Get List of students in a class
    @GET("class/{teacher_id}/{class_id}/students")
    fun getStudents(
            @Header(tokenkey) token: String,
            @Path("teacher_id") teacher_id: String,
            @Path("class_id") user_id: String
    ): Observable<StudentApiResponse>

    //####################################################

    //Teacher - Add a student in selected class
    @POST("student")
    fun addStudent(
            @Header(tokenkey) accesstoken: String,
            @Body addStudentModel: AddStudentModel): Observable<AddStudentResponse>

    //####################################################

    //Teacher - Edit a student in selected class
    @FormUrlEncoded
    @PUT("student")
    fun editStudent(
            @Header(tokenkey) accesstoken: String,
            @Field("studentId") studentid: Int,
            @Field("firstName") firstName: String?,
            @Field("middleName") middleName: String?,
            @Field("lastName") lastName: String?,
            @Field("parentMobile") parentMobile: String?,
            @Field("admissionNo") admission_id: Int,
            @Field("rollNo") roll_no: Int?,
            @Field("updateDate") mobile_parent: String
    ): Observable<AddStudentResponse>

    //####################################################

    //Teacher - Get student details based on student ID
    @GET("student/{student_id}")
    fun getStudentDetails(
            @Header(tokenkey) token: String,
            @Path("student_id") student_id: String
    ): Observable<StudentDetailsApiResponse>

    //####################################################

    // Login service
    @POST("attendancecheck")
    fun checkAttendance(
            @Header(tokenkey) token: String,
            @Body attendanceMicroModel: AttendanceMicroModel): Observable<Result<AttendanceMicroApiResponse>>

    //####################################################

    //Teacher - Student attendance
    @POST("attendance")
    fun postattendence(
            @Header(tokenkey) token: String,
            @Body postAttendence: PostAttendence): Observable<SendModelResponse>

    //####################################################

    //Teacher - Send SMS
    @POST("sendsms")
    fun sendSms(
            @Header(tokenkey) token: String,
            @Body model: SmsSendModel
    ): Observable<SendModelResponse>

    //####################################################

    //Teacher - Send Message
    @POST("sendmessage")
    fun sendNotification(
            @Header(tokenkey) token: String,
            @Body model: NotifySendModel
    ): Observable<SendModelResponse>

    //####################################################

    //Teacher - Send Email
    @POST("sendemail")
    fun sendMail(
            @Header(tokenkey) token: String,
            @Body model: EmailSendModel
    ): Observable<SendModelResponse>

    //####################################################

    //Teacher - Send Diary event
    @POST("senddiary")
    fun sendDiary(
            @Header(tokenkey) token: String,
            @Body model: DiarySendModel
    ): Observable<SendModelResponse>

    //####################################################


    //Teacher - Send Diary event
    @POST("sendsmstoparentaltmobile")
    fun sendAltername(
            @Header(tokenkey) token: String,
            @Body model: AlternateSendModel
    ): Observable<SendModelResponse>

    //####################################################


    @Multipart
    @POST("uploadAttachment")
    fun uploadAttachment(
            @Header(tokenkey) token: String,
            @Part("orgId") org_id: Int,
            @Part("userId") userid: Int,
            @Part("accountManagementId") account_id: Int,
            @Part images: List<MultipartBody.Part>,
            @Part("attachmentMappingId") att_id: Int,
            @Part("eventType") eventType: Int,
            @Part("createDate") createDate: String
    ): Observable<FileAttachmentResponse>

    //####################################################

    //Teacher - Profile - Upload Profile picture
    @Multipart
    @POST("uploadProfile")
    fun uploadImage(
            @Header(tokenkey) token: String,
            @Part("orgId") org_id: Int,
            @Part("userId") account_id: Int,
            @Part images: MultipartBody.Part,
            @Part("createDate") createDate: String
    ): Observable<AttachmentResponse>

    //####################################################

    //Teacher - Send Social grades
    @POST("assignsocialgradeslist")
    fun sendSocialGrades(
            @Header(tokenkey) token: String,
            @Body model: SocialGradeSendModel
    ): Observable<SendModelResponse>

    //####################################################

    //Teacher - View profile
    @GET("personaldetail/{user_id}")
    fun personal_details(
            @Header(tokenkey) token: String,
            @Path("user_id") org_id: Int
    ): Observable<ProfileApiResponse>

    //####################################################

    //Teacher - profile edit
    @POST("personaldetail")
    fun updateProfileData(
            @Header(tokenkey) token: String,
            @Body userInput: ProfileData): Observable<ProfilePostReponse>

    //####################################################

    //Teacher - Reports - SMS Accounts
    @FormUrlEncoded
    @POST("smsaccount")
    fun getCommunicationReportsSMS(
            @Header(tokenkey) token: String,
            @Field("accountManagementId") account_id: String,
            @Field("classId") class_id: String,
            @Field("createDate") createDate: String,
            @Query("sort") query: String
    ): Observable<ReportReponse>

    //####################################################

    //Teacher - Reports - Message Accounts
    @FormUrlEncoded
    @POST("accountmessage")
    fun getCommunicationReportsMessage(
            @Header(tokenkey) token: String,
            @Field("accountManagementId") account_id: String,
            @Field("classId") class_id: String,
            @Field("createDate") createDate: String,
            @Query("sort") query: String
    ): Observable<ReportReponse>

    //####################################################

    //Teacher - Reports - Email Accounts
    @FormUrlEncoded
    @POST("emailaccount")
    fun getCommunicationReportsEmail(
            @Header(tokenkey) token: String,
            @Field("accountManagementId") account_id: String,
            @Field("classId") class_id: String,
            @Field("createDate") createDate: String,
            @Query("sort") query: String
    ): Observable<ReportReponse>

    //####################################################

    //Teacher - Reports - Attendance summary
    @FormUrlEncoded
    @POST("attendancesummary/{class_id}/{user_id}")
    fun getAttendanceSummary(
            @Header(tokenkey) token: String,
            @Path("user_id") user_id: String,
            @Path("class_id") student_id: String,
            @Field("month") month: String,
            @Field("year") year: String
    ): Observable<AttendanceSummaryResponse>

    //####################################################

    //Teacher - Reports - Social Grades
    @GET("socialgradeclass/{id}")
    fun getSocialGradeReport(
            @Header(tokenkey) token: String,
            @Path("id") studentid: String
    ): Observable<SocialGradeClassReportResponse>

    //####################################################

    //Teacher - My Social Grades
    @GET("socialgrade/{org_id}")
    fun getSocialgrades(
            @Header(tokenkey) token: String,
            @Path("org_id") org_id: String
    ): Observable<MySocialGradesResponse>

    //####################################################

    //Teacher - My Social Grades - Add Social grade
    @POST("socialgrade")
    fun addSocialgrade(
            @Header(tokenkey) token: String,
            @Body loginModel: AddSocialGradeModel
    ): Observable<AddSocialGradeResponse>

    //####################################################

    //Teacher - My Social Grades - Edit Social grade
    @FormUrlEncoded
    @PUT("socialgrade")
    fun editSocialgrade(
            @Header(tokenkey) token: String,
            @Field("id") id: Int,
            @Field("socialGradeName") name: String,
            @Field("point") point: Int,
            @Field("flag") type: String,
            @Field("orgId") org_id: Int,
            @Field("accountManagementId") account_id: Int,
            @Field("updateDate") updateDate: String
    ): Observable<AddSocialGradeResponse>

    //####################################################


    //Teacher - My Social Grades - Delete Social grade
    @POST("socialgradeactive")
    fun deleteSocialgrade(
            @Header(tokenkey) token: String,
            @Body loginModel: DeleteSocialModel
    ): Observable<AddSocialGradeResponse>

    //####################################################

    //1;"Email"
    //2;"SMS"
    //3;"Message"
    //Teacher - Get messaging templates
    //Teacher - Templates - Get Templates
    @GET("templatedata/{account_id}/{type_id}")
    fun getTemplates(
            @Header(tokenkey) token: String,
            @Path("account_id") accountid: String,
            @Path("type_id") type: Int
    ): Observable<TemplateSelectionApiResponse>

    //####################################################

    //Teacher - Templates - Edit Templates
    @FormUrlEncoded
    @PUT("templatedata")
    fun editTemplate(
            @Header(tokenkey) token: String,
            @Field("templateId") id: Int,
            @Field("templateTextHeading") heading: String,
            @Field("templateText") text: String,
            @Field("templateTypeId") type_id: Int,
            @Field("accountManagementId") account_id: Int,
            @Field("updateDate") updateDate: String
    ): Observable<TemplateResponse>

    //####################################################

    //Teacher - Templates - Delete Templates
    @DELETE("templatedata/{id}")
    fun deleteTemplate(
            @Header(tokenkey) token: String,
            @Path("id") id: Int
    ): Observable<TemplateResponse>

    //####################################################

    //Teacher - Templates - Add Templates
    @POST("templatedata")
    fun addTemplate(
            @Header(tokenkey) token: String,
            @Body templateModel: AddTemplateModel
    ): Observable<TemplateResponse>

    //####################################################

    //Teacher - API Settings - Email
    @GET("communicationsetting/{org_id}/{type_id}")
    fun getComSettings(
            @Header(tokenkey) token: String,
            @Path("org_id") org_id: String,
            @Path("type_id") type_id: String
    ): Observable<CommunicationSettingResponse>

    //####################################################


    //Teacher - API Settings - Add Email settings
    @POST("communicationsetting")
    fun addCommunicationSetting(
            @Header(tokenkey) token: String,
            @Body model: CommunicationSettingModel
    ): Observable<CommunicationSettingAddResponse>


    //####################################################
    //Teacher - API Settings - Edit Email settings
    @FormUrlEncoded
    @PUT("communicationsetting")
    fun putCommunicationSetting(
            @Header(tokenkey) token: String,
            @Field("id") comm_id: Int,
            @Field("userName") username: String,
            @Field("password") password: String,
            @Field("senderId") sender_id: String,
            @Field("orgId") org_id: String?,
            @Field("updateDate") updateDate: String?,
            @Field("attendanceSms") attendanceSms: Boolean,
            @Field("smsMain") smsMain: Boolean?,
            @Field("emailAttendance") emailAttendance: Boolean?,
            @Field("emailMain") emailMain: Boolean?,
            @Field("messageAttendancePresent") messageAttendancePresent: Boolean?,
            @Field("messageAttendanceAbsent") messageAttendanceAbsent: Boolean?,
            @Field("messageMain") messageMain: Boolean?

    ): Observable<CommunicationSettingAddResponse>

    //####################################################

    //Teacher - API Settings - Activate/Deactivate
    @FormUrlEncoded
    @PUT("communicationsettingflag")
    fun setCommunicationSetting(
            @Header(tokenkey) token: String,
            @Field("id") key: String,
            @Field("activeFlag") flag: Int,
            @Field("updateDate") updateDate: String?

    ): Observable<CommunicationSettingDeactivate>

    //####################################################

    //Parent - get Student List
    @GET("parentallstudent/{userId}")
    fun getParentStudentList(
            @Header(tokenkey) token: String,
            @Path("userId") teacher_id: String
    ): Observable<ConnectedStudentApiResponse>

    //####################################################

    //Parent - get Student List - Add Student
    @POST("connect")
    fun connectStudent(
            @Header(tokenkey) token: String,
            @Body code: InputCodeModel
    ): Observable<InputStudentCodeResponse>

    //####################################################


    //Parent - get Student List - Delete a connected student
    @DELETE("student/{student_id}")
    fun deleteStudent(
            @Header(tokenkey) token: String,
            @Path("student_id") student_id: String
    ): Observable<DeleteStudentResponse>

    //####################################################

    //Parent - Register Firebase Token
    @POST("registerFirebaseToken")
    fun registerFirebase(
            @Header(tokenkey) token: String,
            @Body request: RegisterFirebaseRequest
    ): Observable<RegisterFirebaseResponse>

    //####################################################

    //Parent - Student - All details
    @FormUrlEncoded
    @POST("events")
    fun getEvents(
            @Header(tokenkey) token: String,
            @Field("typeId") event_type: Int,
            @Field("studentId") student_id: Int,
            @Field("createDate") createDate: String,
            @Query("sort") query: String

    ): Observable<StudentEventsApiResponse>
    //####################################################


    //Parent - Events - Read status
    @POST("readstatus")
    fun readStatus(
            @Header(tokenkey) token: String,
            @Body code: ReadStatusModel
    ): Observable<ReadStatusReponser>

    //####################################################

    //Parent - Student - Inbox
    @FormUrlEncoded
    @POST("events")
    fun getInbox(
            @Header(tokenkey) token: String,
            @Field("typeId") event_type: Int,
            @Field("studentId") student_id: Int,
            @Field("createDate") createDate: String,
            @Query("sort") query: String
    ): Observable<StudentInboxApiResponse>

    //####################################################

    /* "Diary"
            2    "Assignment"
            3    "Notice"
            4    "Event"*/

    //Parent - Student - Soical Grades
    @GET("socialgradestudent/{student_id}/{year}/{month}")
    fun getSocialGradeStudent(
            @Header(tokenkey) token: String,
            @Path("student_id") student_id: String,
            @Path("month") month: String,
            @Path("year") year: String
    ): Observable<SocialGradeReportResponse>

    //####################################################

    //Parent - Student - Attendance
    @FormUrlEncoded
    @POST("student/{student_id}/attendance")
    fun getAttendance(
            @Header(tokenkey) token: String,
            @Path("student_id") student_id: String,
            @Field("month") month: String,
            @Field("year") year: String
    ): Observable<AttendanceApiResponse>

    //####################################################

    //Parent - Student - Diary
    @FormUrlEncoded
    @POST("studentdiary?")
    fun getDiaryEvents(
            @Header(tokenkey) token: String,
            @Field("studentId") student_id: Int,
            @Field("eventTypeId") event_type: String,
            @Field("createDate") createDate: String,
            @Query("sort") query: String
    ): Observable<StudentDiaryApiResponse>

    //####################################################

    //Parent - Student - Normal event details
    @GET("allevent/{event_id}/{eventTypeId}")
    fun getEventDetails(
            @Header(tokenkey) token: String,
            @Path("event_id") event_id: Int,
            @Path("eventTypeId") eventTypeId: String
    ): Observable<StudentEventApiResponse>

    //####################################################

    //Parent - Student - Diary event details
    @GET("allevent/{event_id}/{eventTypeId}")
    fun getDiaryEventDetails(
            @Header(tokenkey) token: String,
            @Path("event_id") event_id: Int,
            @Path("eventTypeId") eventTypeId: String
    ): Observable<StudentDiaryEventApiResponse>

    //####################################################


}