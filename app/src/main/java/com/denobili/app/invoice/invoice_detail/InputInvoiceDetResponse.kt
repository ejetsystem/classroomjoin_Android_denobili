package com.denobili.app.invoice.invoice_detail

import android.os.Parcel
import android.os.Parcelable
import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class InputInvoiceDetResponse (
    @SerializedName("data")
    var data: ConnectedList1Model? = null)
class ConnectedList1Model(
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
        var balance_amount: String ?,
        @SerializedName("particular_annual")
var particular_annual: ArrayList<ParticularAnnualModel> ?,
        @SerializedName("particulars")
        var particulars: ArrayList<ParticularsModel> ?,
        @SerializedName("transaction")
        var transaction: ArrayList<TransModel> ?

)
class ParticularsModel(
        @SerializedName("id")
        var id: Int ?,
        @SerializedName("invoice_id")
        var invoice_id: String ?,
        @SerializedName("name")
        var name: String ?,
        @SerializedName("description")
        var description: String ?,
        @SerializedName("amount")
        var amount: String ?,
        @SerializedName("fine_amount")
        var fine_amount: String ?):DisplayItem
class ParticularAnnualModel(
        @SerializedName("id")
        var id: Int ?,
        @SerializedName("course_id")
        var invoice_id: String ?,
        @SerializedName("name")
        var name: String ?,
        @SerializedName("amount")
        var amount: String ?):DisplayItem

class TransModel( @SerializedName("id")
                  var id: Int ?,
                  @SerializedName("invoice_id")
                  var invoice_id: String ?,
                  @SerializedName("transaction_id")
                  var transaction_id: String ?,
                  @SerializedName("description")
                  var description: String ?,
                  @SerializedName("amount")
                  var amount: String ?,
                  @SerializedName("tax_amount")
                  var tax_amount: String ?,
                  @SerializedName("payment_type")
                  var payment_type: String ?,
                  @SerializedName("created_at")
                  var created_at: String ?
                  ):DisplayItem,Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(invoice_id)
        parcel.writeString(transaction_id)
        parcel.writeString(description)
        parcel.writeString(amount)
        parcel.writeString(tax_amount)
        parcel.writeString(payment_type)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransModel> {
        override fun createFromParcel(parcel: Parcel): TransModel {
            return TransModel(parcel)
        }

        override fun newArray(size: Int): Array<TransModel?> {
            return arrayOfNulls(size)
        }
    }
}
