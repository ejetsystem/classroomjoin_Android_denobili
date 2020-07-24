package com.denobili.app.invoice.trans_detail

import android.os.Bundle
import com.denobili.app.R
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.DisplayItem
import org.jetbrains.anko.AnkoLogger

class TransDetail  : BaseListActivity(), AnkoLogger {
    var transList:ArrayList<DisplayItem>?=null
    private var adapter:TransAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trans_detail)
        initviews()
        transList= intent.getSerializableExtra("listTrans") as ArrayList<DisplayItem>
        println(transList)
        adapter = TransAdapter(null, this)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        adapter!!.addItems(true, transList)
        showResults()
    }
}