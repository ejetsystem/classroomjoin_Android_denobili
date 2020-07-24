package com.denobili.app.invoice

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName

data class ConnectedListModel(

@SerializedName("id")
val id: Int?,

@SerializedName("academic_year_id")
val academic_year_id: String?,

@SerializedName("user_type")
val user_type: String?,

@SerializedName("table_id")
val table_id: String?,

@SerializedName("fee_id")
val fee_id: String,

@SerializedName("subscription_id")
val subscription_id: String?,

@SerializedName("name")
var name: String ?,
@SerializedName("description")
var description: String ?,
@SerializedName("subscription_type")
var subscription_type: String ?,
@SerializedName("start_date")
var start_date: String ?,
@SerializedName("end_date")
var end_date: String ?,
@SerializedName("due_date")
var due_date: String ?,
@SerializedName("total_amount")
var total_amount: String ?,
@SerializedName("invoice_amount")
var invoice_amount: String ?,
@SerializedName("studentFullName")
var studentFullName: String ?,
@SerializedName("amount_paid")
var amount_paid: String ?,
@SerializedName("is_paid")
var is_paid: String ?,
@SerializedName("parentFullName")
var parentFullName: String ?,
@SerializedName("balance_amount")
var balance_amount: String ?) : DisplayItem





