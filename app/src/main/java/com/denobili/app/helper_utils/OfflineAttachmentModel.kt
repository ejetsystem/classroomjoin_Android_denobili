package com.denobili.app.helper_utils

import com.denobili.app.R
import com.denobili.app.helper.FileTypes
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject


public open class OfflineAttachmentModel(): DisplayItem,RealmObject(){

    @SerializedName("originalFileName")var name:String?=null
    @SerializedName("attachmentUrl") var file_url:String?=null
    @SerializedName("attachmentId")var id: Int?=null
    @SerializedName("attachmentMappingId")var att_map_id:Int?=null
    @SerializedName("attachmentsType")var att_type:Int?=null
    var file_thumbnail:Int= R.drawable.ic_photo_black_24dp

    constructor(name:String,file_url:String,id:Int,att_map_id:Int,att_type:Int):this(){
        this.name=name
        this.file_url=file_url
        this.id=id
        this.att_map_id=att_map_id
        this.att_type=att_type
        this.file_thumbnail=getThumbnail(att_type)

    }

    fun getThumbnail(att_type: Int):Int=
            when(att_type){
                FileTypes.DOC.value-> R.drawable.ic_file_word
                FileTypes.JPG.value,FileTypes.PNG.value-> R.drawable.ic_photo_black_24dp
                FileTypes.PDF.value-> R.drawable.ic_file_pdf
                FileTypes.XLS.value,FileTypes.XLSX.value-> R.drawable.ic_file_excel
                FileTypes.TXT.value-> R.drawable.ic_file_document
                else -> R.drawable.ic_photo_black_24dp
            }

}