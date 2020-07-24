package com.denobili.app.manageAccountsPackage

import android.app.ProgressDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.ItemClickSupport
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.landing_page.LandingActivity
import com.denobili.app.loginPage.Userdata
import com.denobili.app.mystudentsPage.MyStudentModel
import kotlinx.android.synthetic.main.activity_plus_fab.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import rx.Observer

class ManageAccountsPage : BaseListActivity() {

    private var adapter: ManageAccountsAdapter? = null
    private var presenter: ManageAccountPresenter? = null
    private var progressdialog: ProgressDialog? = null
    private var studentid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_fab)
        initviews()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.accounts))

        MainBus.getInstance().busObservable.ofType(ManageAccountsEvent::class.java).subscribe(eventobserver)
        presenter = ManageAccountPresenter(this)

        adapter = ManageAccountsAdapter(null, this)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(this@ManageAccountsPage)

        fab.imageResource = R.drawable.ic_add_white_24dp
        fab.onClick {
            startActivity<LandingActivity>()
        }
        setUpItemTouchHelper()

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener { recyclerView, position, v ->
            val user = adapter!!.getItem(position) as Userdata
            presenter!!.setUser(user)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        getData()
    }

    private val eventobserver = object : Observer<ManageAccountsEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: ManageAccountsEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog?.dismiss()
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT -> {
                    if (adapter!!.itemCount == 0)
                        showNoResults()
                }
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)

                Event.DELETED_ITEM -> {
                    showSnackbar(getString(R.string.deletion_success))
                    onRefresh()
                }
                Event.DELETION_FAILURE -> {
                    showerrorDialog2(getString(R.string.deletion_failure))
                    onRefresh()
                }


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

    private fun showdeletealert(position: Int) {
        var alert = AlertDialog.Builder(this@ManageAccountsPage);
        alert.setTitle(getString(R.string.app_name))
        alert.setMessage(getString(R.string.delete_confirm))
        alert.setPositiveButton(R.string.btn_yes) { dialog, whichButton ->

            studentid = (adapter?.getItem(position) as MyStudentModel).id.toString()
            deletStudent()
            adapter!!.notifyItemRemoved(position)
        }
        alert.setNegativeButton(R.string.btn_no) { dialog, whichButton ->
            dialog.dismiss()
            adapter?.notifyItemChanged(position)
        }
        alert.show()
    }

    private fun deletStudent() {
        if (NetworkHelper.isOnline(this@ManageAccountsPage)) {
            progressdialog!!.show()
            presenter!!.deleteitem(studentid!!)
        } else
            showerrorDialog2(getString(R.string.noInternet))
    }

    private fun showerrorDialog2(message: String) {
        var alert = AlertDialog.Builder(this@ManageAccountsPage);
        alert.setTitle(getString(R.string.error))
        alert.setMessage(message)
        alert.setPositiveButton(getString(R.string.btn_retry)) { dialog, whichButton ->
            deletStudent()
        }
        alert.setNegativeButton(getString(R.string.btn_cancel)) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun setUpItemTouchHelper() {

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val position: Int = viewHolder!!.adapterPosition
                showdeletealert(position)

            }

            internal var background: Drawable
            internal var xMark: Drawable?
            internal var xMarkMargin: Int = 0
            internal var initiated: Boolean = false

            init {
                background = ColorDrawable(Color.RED)
                xMark = AppCompatResources.getDrawable(this@ManageAccountsPage, R.drawable.ic_delete_green_24dp)
                xMark!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                xMarkMargin = getResources().getDimension(R.dimen.ic_clear_margin).toInt()
                initiated = true
            }

            // not important, we don't want drag & drop
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.adapterPosition == -1) {
                    // not interested in those
                    return
                }

                // draw red background
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)
                // draw x mark
                val itemHeight = itemView.bottom - itemView.top
                val intrinsicWidth = xMark!!.intrinsicWidth
                val intrinsicHeight = xMark!!.intrinsicWidth
                val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                val xMarkRight = itemView.right - xMarkMargin
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkBottom = xMarkTop + intrinsicHeight
                xMark!!.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                xMark!!.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }
}

