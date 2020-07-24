package com.denobili.app.studentInboxPage

import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.google.gson.annotations.SerializedName


data class StudentInboxModel(@SerializedName("id")val id:Int,
                             @SerializedName("teachername")
                             val teacher_name:String?=null,
                             @SerializedName("communicationtext")
                             val message:String?=null,
                             @SerializedName("communicationsubject")
                             val subject:String?=null,
                             @SerializedName("sendingdate")
                             val date:String?=null,
                             @SerializedName("eventtype")
                             val event_type:Int,
                             @SerializedName("statusflag")
                             val status_flag:Int,
                             @SerializedName("studentgrade")
                             val grade:String?=null,
                             @SerializedName("attachmentResponseBean")
                             val attachments:List<OfflineAttachmentModel>?): DisplayItem