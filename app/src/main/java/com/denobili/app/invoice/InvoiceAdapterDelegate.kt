package com.denobili.app.invoice

import `in`.juspay.godel.analytics.GodelTracker
import `in`.juspay.juspaysafe.BrowserCallback
import `in`.juspay.juspaysafe.JuspaySafeBrowser
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.invoice.invoice_detail.InvoiceDetailActivity
import com.denobili.app.invoice.payment.HomeActivity
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.invoice_adapter.view.*
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class InvoiceAdapterDelegate(context: Context, userId: String, orgId: String?) : AdapterDelegate<List<DisplayItem>>() {
   var mContext:Context?=null
    var userId=userId
    var orgId=orgId
    var baseurl:String?=null

    internal val androidColors: IntArray

    init {
        androidColors = context.resources.getIntArray(R.array.androidcolors)
        mContext=context
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is ConnectedListModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.invoice_adapter, viewGroup, false)

        return StudentViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder

        val item = displayItems[i] as ConnectedListModel
         if(orgId=="58"){
            baseurl="ctps.edustacks.org/"
        }
        else if(orgId=="57"){
            baseurl="gomoh.edustacks.org/"
        }
        else if(orgId=="56"){
            baseurl="cmri.edustacks.org/"
        }
        else if(orgId=="55"){
            baseurl="bhuli1.edustacks.org/"
        }
        else if(orgId=="54"){
            baseurl="koradih.edustacks.org/"
        }
        else if(orgId=="51"){
            baseurl="maithon.edustacks.org/"
        }
        else if(orgId=="41"){
            baseurl="mugma.edustacks.org/"
        }

        if (!item.is_paid.equals("1")){
            holder.fee_lay.setOnClickListener{
                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                val currentDate = sdf.format(Date())
                var time=orgId+currentDate.toString()
                var path="https://"+baseurl+"index.php?r=fees/api/process&InvoiceId="+item.id+"&ParentId="+userId+"&amount="+item.balance_amount+"&OrderNo="+time
                val intent=Intent(mContext,HomeActivity::class.java)
                intent.putExtra("path",path)
                intent.putExtra("invoiceId",item.id)
                intent.putExtra("baseurl",baseurl+"index.php")
                intent.putExtra("amount",item.balance_amount)
                mContext?.startActivity(intent)
           /*   val intent=Intent(mContext,InvoiceDetailActivity::class.java)
                intent.putExtra("invoice_id",item.id)
                intent.putExtra("recipient",item.studentFullName)
                intent.putExtra("fee_month",getMonth(item.due_date.toString()))
                intent.putExtra("discount",item.studentFullName)
                intent.putExtra("status",item.is_paid)
                intent.putExtra("paymentDetail",item.studentFullName)
                intent.putExtra("adjustment",item.studentFullName)
                intent.putExtra("amount",item.invoice_amount)

                mContext?.startActivity(intent)*/

            }



        }
        else{}


        holder.invoiceId.text =item.total_amount.toString()
        holder.feeAmount.text = "#"+item.id.toString()
        holder.detail_lay.setOnClickListener{
            val intent=Intent(mContext,InvoiceDetailActivity::class.java)
            intent.putExtra("invoice_id",item.id)
            intent.putExtra("recipient",item.studentFullName)
            intent.putExtra("fee_month",getMonth(item.due_date.toString()))
            intent.putExtra("discount",item.studentFullName)
            intent.putExtra("status",item.is_paid)
            intent.putExtra("paymentDetail",item.studentFullName)
            intent.putExtra("adjustment",item.studentFullName)
            intent.putExtra("amount",item.total_amount)

            mContext?.startActivity(intent)
        }
        holder.feeMonth.text=getMonth(item.due_date.toString())
        if (item.is_paid.equals("1")){
            holder.fee_status.text = "PAID"
            //holder.fee_lay.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#009688"))
        }

        else{
            holder.fee_status.text = "PAY NOW"
            //holder.fee_lay.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#0099cc"))
        }



        //val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        //holder.student_name_short.solidColor = randomAndroidColor

    }
    val callBack = object: BrowserCallback() {
         override fun onPageStarted(view: WebView, url: String) {
            super.onPageStarted(view, url)
        }

        override fun onTransactionAborted(p0: JSONObject?) {
            GodelTracker.getInstance().trackPaymentStatus("123456", GodelTracker.FAILURE)
            JuspaySafeBrowser.exit()
            Toast.makeText(context, "Transaction cancelled.", Toast.LENGTH_LONG).show()
            // Payment was not completed by the user. So, let's present the retry option to the user.
            // this@NextActivity.retryPayment()
        }


        override fun endUrlReached(p0: WebView?, p1: JSONObject?) {
            GodelTracker.getInstance().trackPaymentStatus("123456", GodelTracker.SUCCESS)
            JuspaySafeBrowser.exit()
            // Required action performed
            Toast.makeText(context, "Transaction completed. Maybe failure/success.", Toast.LENGTH_LONG).show()
            // Payment step has been completed by the user. Take the user to the next step.
            //this@NextActivity.startNextActivity()        }


        }
    }
    @Throws(ParseException::class)
    private fun getMonth(date:String):String {
        val d = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)
        val cal = Calendar.getInstance()
        cal.setTime(d)
        val monthName = SimpleDateFormat("MMMM").format(cal.getTime())
        return monthName
    }
    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val feeAmount: TextView
        internal val invoiceId: TextView
        internal val feeMonth: TextView
        internal val fee_status: TextView
        internal val fee_lay: RelativeLayout
        internal val detail_lay: RelativeLayout



        init {
            invoiceId = itemView.invoice_id
            feeAmount = itemView.fee_amount
            feeMonth = itemView.month
            detail_lay=itemView.detail_lay
            fee_status=itemView.fee_status
            fee_lay=itemView.payLay
        }

    }

}