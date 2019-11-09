package com.classroomjoin.app.teacherAddClasses

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.classroomjoin.app.R
import com.classroomjoin.app.classEditPage.AddClassModel
import com.classroomjoin.app.classEditPage.EditClassPage
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.BaseListFragment
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import kotlinx.android.synthetic.main.activity_plus_fab.*
import org.jetbrains.anko.*
import rx.Observer


class ClassListFragment : BaseListFragment(), ClassListAdapterDelegate.TouchcallbackListener, AnkoLogger {


    private var classmodel: AddClassModel? = AddClassModel()
    private var adapter: ClassListAdapter? = null
    private var presenter: ClassListPresenter? = null
    private var classname: String? = null
    private var progressdialog: ProgressDialog? = null

    private var context11:Context?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.activity_plus_fab, container!!, false)

        initviews(view)

        context11=view.context

        MainBus.getInstance().busObservable.ofType(ClassListEvent::class.java).subscribe(eventobserver)
        presenter = ClassListPresenter(context11!!)

        adapter = ClassListAdapter(null, this, context11!!, presenter!!.isAdmin())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context11!!)
        recyclerView.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(context11!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.imageResource = R.drawable.ic_add_white_24dp

        //error { "this is not an admin---->"+presenter!!.isAdmin() }

        LocalBroadcastManager.getInstance(context11!!).registerReceiver(broadCastReceiver,
                IntentFilter("custom-event-name"))
        if (presenter!!.isAdmin()) {
            error { "this is not an admin" }
            fab.visibility = View.GONE
        }
        fab.onClick {
            activity!!.startActivity<EditClassPage>()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        swipeContainer.isRefreshing = true
        presenter?.refreshdata()
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            onRefresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(context11!!)
                .unregisterReceiver(broadCastReceiver)
    }

    override fun editItem(myclass: ClassListModel) {
        classmodel?.name = myclass.name
        classmodel?.class_id = myclass.id.toString()
        activity!!.startActivity(activity!!
                .intentFor<EditClassPage>
                (EditClassPage.CLASS_ID_KEY to classmodel?.class_id,
                        EditClassPage.CLASS_NAME_KEY to classmodel?.name))
    }

    private val eventobserver = object : Observer<ClassListEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: ClassListEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.hide()
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.notifyDataSetChanged()
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT -> {
                    adapter!!.notifyDataSetChanged()
                    adapter!!.clearList()
                    if (adapter!!.itemCount == 0)
                        showNoResults(getString(R.string.no_classes_added))

                }
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }
                Event.POST_SUCCESS -> onRefresh()
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                Event.POST_FAILURE -> showError(event.error)
                else -> {
                }
            }

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

    private fun getData() {
        if (adapter!!.itemCount == 0) showLoading()
        presenter!!.getdata()
    }
}
