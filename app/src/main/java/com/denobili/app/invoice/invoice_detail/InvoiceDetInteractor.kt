package com.denobili.app.invoice.invoice_detail

import android.content.Context
import com.denobili.app.helper.NetworkConnectionInterceptor
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.ApiInter
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.crashlytics.android.Crashlytics
import rx.Observable
import rx.Observer
import kotlin.collections.ArrayList

class InvoiceDetInteractor internal constructor(private val context: Context,private val invoice_id: String, private val presenter: InvoiceDetPresenter)
    : Interactor(context) {
    var apiInter: ApiInter?=null
    var orgId:String?=null



    /*fun getData() {
        getObservable().subscribe(Observer)
    }*/

    fun getData1() {

        Crashlytics.setUserIdentifier(userid)
        Crashlytics.setUserName(user_name)
        Crashlytics.setUserEmail(user_email)
        if (user_mobileNo != null)
            Crashlytics.setString("Mobile No:", user_mobileNo)
       if(org_id=="58"){
            orgId="https://ctps.edustacks.org/"
        }
        else if(org_id=="57"){
            orgId="https://gomoh.edustacks.org/"
        }
        else if(org_id=="56"){
            orgId="https://cmri.edustacks.org/"
        }
        else if(org_id=="55"){
            orgId="https://bhuli1.edustacks.org/"
        }
        else if(org_id=="54"){
            orgId="https://koradih.edustacks.org/"
        }
        else if(org_id=="51"){
            orgId="https://maithon.edustacks.org/"
        }
        else if(org_id=="41"){
            orgId="https://mugma.edustacks.org/"
        }
        val networkConnectionInterceptor = NetworkConnectionInterceptor(context)
        apiInter = ApiInter(networkConnectionInterceptor,orgId.toString())
        getObservable1().subscribe(Observer1)

    }

    /* *//*  private fun getObservable(): Observable<ConnectedStudentApiResponse> =
            apiInterface.getConnectedStudents(accesstoken, connect_students(), teacher_id).compose(RxFunctions.applySchedulers())
*/



    private fun getObservable1(): Observable<InputInvoiceDetResponse> =
            apiInter?.getInvoiceDetail("fees/api/view", invoice_id,teacher_id)?.compose(RxFunctions.applySchedulers())!!

    private val Observer1: Observer<InputInvoiceDetResponse>
        get() = object : Observer<InputInvoiceDetResponse> {
            override fun onCompleted() {
            }

            override fun onError(e:  Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: InputInvoiceDetResponse?) {

                    val list = ArrayList<DisplayItem>()
                val list1 = ArrayList<DisplayItem>()
                val list2=ArrayList<DisplayItem>()
                    // if(t.data!!.)
                    //list.addAll(t.data!!)
                    var st_codes: ArrayList<Int>? = ArrayList<Int>()
                    t?.data?.particulars?.forEachIndexed { index, myConnectedStudentModel ->
                        st_codes?.add(myConnectedStudentModel.id!!)

                        list.add(myConnectedStudentModel)
                    }
                var st_codes1: ArrayList<Int>? = ArrayList<Int>()
                t?.data?.transaction?.forEachIndexed { index, myConnectedStudentModel1 ->
                    st_codes1?.add(myConnectedStudentModel1.id!!)

                    list1.add(myConnectedStudentModel1)
                }
                var st_codes2: ArrayList<Int>? = ArrayList<Int>()
                t?.data?.particular_annual?.forEachIndexed { index, myConnectedStudentModel2 ->
                    st_codes2?.add(myConnectedStudentModel2.id!!)

                    list2.add(myConnectedStudentModel2)
                }
                    presenter.setData(list,list1,list2)
                // else{}

            }
        }





}
