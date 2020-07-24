package com.denobili.app.teacherCommunicationReports

import com.google.gson.annotations.SerializedName


class CommunicationReportContent(@SerializedName("content")val content:List<ReportModel>)