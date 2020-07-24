package com.denobili.app.teacherAttachmentPage

import com.denobili.app.helper_utils.DisplayItem
import java.io.File

open class Attachment(name: String, file: File) : DisplayItem {
    var name: String? = name
    var file: File? = file
    var file_url: String? = null
    var id: Int? = null
}