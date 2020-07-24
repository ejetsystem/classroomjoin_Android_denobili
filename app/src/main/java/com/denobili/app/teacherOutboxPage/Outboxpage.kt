package com.denobili.app.teacherOutboxPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.teacherMessageSendPage.SendModelResponse
import org.jetbrains.anko.AnkoLogger
import rx.Observer
import rx.Subscriber
import rx.Subscription

class Outboxpage : BaseListActivity(), AnkoLogger, OutboxSyncListener {

    var subscription: Subscription? = null
    private var adapter: TeacherOutboxAdapter? = null
    private var viewModel: OutboxViewModel? = null
    private var progressdialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        initviews()

        adapter = TeacherOutboxAdapter(null, this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@Outboxpage)
        recyclerView.isNestedScrollingEnabled = false

        progressdialog = DialogUtil.showProgressDialog(this@Outboxpage)

        viewModel = OutboxViewModel(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.outbox))

    }

    override fun onSyncStateChanged(model: OutboxModel, position: Int) {
        if (NetworkHelper.isOnline(this)) {
            viewModel!!.changeSyncState(model, 1)
            viewModel!!.send(model).subscribe(getSyncObserver(position, model))
        } else
            Snackbar.make(recyclerView, getString(R.string.noInternet), Snackbar.LENGTH_SHORT).show()

    }

    private fun getSyncObserver(position: Int, model: OutboxModel): Observer<SendModelResponse> {
        return object : Observer<SendModelResponse> {
            override fun onNext(t: SendModelResponse?) {
                if (t?.status == "Success") {
                    adapter!!.notifyItemRemoved(position)
                    viewModel!!.deleteItem(model)
                    showSnackbar("The item  with message" + model.message!!.substring(6) + ".. has been Synced")
                } else {
                    viewModel!!.changeSyncState(model, 0)
                    showSnackbar("Looks like the Syncing of the item  with message" + model.message!!.substring(6) + " .. failed,Please try again later")
                }
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                viewModel!!.changeSyncState(model, 0)
                showSnackbar("Looks like the Syncing of the item  with message" + model.message!!.substring(6) + " .. failed,Please try again later")

            }

            override fun onCompleted() {
            }

        }
    }


    override fun onRefresh() {
        super.onRefresh()
        swipeContainer.isRefreshing = false
        getData()
    }

    private val subscriber = object : Subscriber<List<OutboxModel>>() {
        override fun onNext(t: List<OutboxModel>?) {
            if (t!!.isEmpty())
                showNoResults(getString(R.string.outbox_some_error))
            else {
                adapter!!.addItems(true, t)
                showResults()
            }
        }

        override fun onError(e: Throwable?) {
            e?.printStackTrace()
            showError(getString(R.string.some_error))
        }

        override fun onCompleted() {
        }


    }


    private fun showLoading() {
        imageView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        textView.visibility = View.GONE
        contentLoadingProgressBar.visibility = View.VISIBLE
        contentLoadingProgressBar.show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null && !subscription!!.isUnsubscribed)
            subscription!!.unsubscribe()

    }

    private fun getData() {
        showLoading()
        subscription = viewModel!!.getData()
                .compose(RxFunctions.applySchedulers())
                .subscribe(subscriber)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
