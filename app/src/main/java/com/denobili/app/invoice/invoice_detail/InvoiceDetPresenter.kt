package com.denobili.app.invoice.invoice_detail

import android.content.Context
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter

class InvoiceDetPresenter (context: Context,invoice_id:String) : Presenter() {

    private val interactor: InvoiceDetInteractor

    init {
        interactor = InvoiceDetInteractor(context, invoice_id,this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>, list1: ArrayList<DisplayItem>, list2: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(ConnectInvoice(Event.NO_RESULT))
        else Emit(ConnectInvoice(displayItem,list1,list2))
    }

    fun setError(error: String) {
        Emit(ConnectInvoice(Event.ERROR, error))
    }

    fun serverError() {
        Emit(ConnectInvoice(Event.SERVER_ERROR))
    }

    fun getdata() {
        interactor.getData1()
    }


    fun postDeleteditem() {
        Emit(ConnectInvoice(Event.DELETED_ITEM))
    }

    fun postDeletionFailure(error_message: String) {
        Emit(ConnectInvoice(Event.DELETION_FAILURE, error_message))

    }



}