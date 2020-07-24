package com.denobili.app.studentallEventsPage

import com.google.gson.annotations.SerializedName


data class StudentEventsApiResponse(var status: String,
                                    @SerializedName
                                    ("data") var data: List<StudentEventModel>,
                                    var error_code: String? = null,
                                    var error_status: String? = null)