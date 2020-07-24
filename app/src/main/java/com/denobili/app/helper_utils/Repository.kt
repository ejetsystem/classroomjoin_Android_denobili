package com.denobili.app.helper_utils

import rx.Observable


interface Repository<T> {

    fun getItem():Observable<T>

    fun getItems():Observable<List<T>>

    fun updateItem(item:T):Observable<Any>

    fun addItem(item: T):Observable<Any>

    fun addItems(list:List<T>):Observable<Any>

    fun deleteItem():Observable<Any>

}