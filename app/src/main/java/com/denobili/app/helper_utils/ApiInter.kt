package com.denobili.app.helper_utils

import com.denobili.app.helper.NetworkConnectionInterceptor
import com.denobili.app.invoice.InputInvoiceResponse
import com.denobili.app.invoice.invoice_detail.InputInvoiceDetResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface ApiInter {
    @GET("index.php")
    fun getParentStudentList(
            @Query("r") token: String,
            @Query("id") teacher_id: String,
            @Query("sid") student_id: String


    ): Observable<InputInvoiceResponse>
            @GET("index.php")
    fun getInvoiceDetail(
            @Query("r") token: String,
            @Query("id") teacher_id: String,
            @Query("pid") parent_id: String
    ): Observable<InputInvoiceDetResponse>

    companion object{
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor,orgId:String): ApiInter{
            val okHttpClient= OkHttpClient.Builder()
                    .addInterceptor(networkConnectionInterceptor)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(orgId)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create(ApiInter::class.java)
        }
        const val tokenkey = "Authorization"

    }

}