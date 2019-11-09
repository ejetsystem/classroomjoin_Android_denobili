package com.classroomjoin.app.templatePage

import com.google.gson.annotations.SerializedName


data class TemplateSelectionApiResponse(@SerializedName("status")
                                        val status: String,
                                        @SerializedName("data")
                                        val data: ArrayList<Template>,
                                        @SerializedName("error_code")
                                        val error_code: String,
                                        @SerializedName("error_response")
                                        val error_response: String)