package com.denobili.app.teacherAttachmentPage

import com.denobili.app.helper_utils.OfflineAttachmentModel


data class FileAttachmentResponse(var status: String,
                                  var data: List<OfflineAttachmentModel>,
                                  var error_message: String,
                                  var error_code: String)