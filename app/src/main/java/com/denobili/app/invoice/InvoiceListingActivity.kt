package com.denobili.app.invoice

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.ForceUpdateChecker
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.landing_page.LandingActivity
import com.denobili.app.myConnectedStudents.ConnectedStudentEvent
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import rx.Observer

class InvoiceListingActivity : BaseListActivity(), ForceUpdateChecker.OnUpdateNeededListener {

    private var adapter: InvoiceAdapter? = null
    private var presenter: InvoicePresenter? = null
    var newTextis: CharSequence? = null
    private var progressdialog: ProgressDialog? = null
    private var studentid: String? = null
    private var userId:String?=null
    private var ORG_ID="com.classroom.org_id"
    private var orgId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_list)
        userId=intent.getStringExtra("userId")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        orgId= sharedPreferences.getString(ORG_ID, "0")
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check()
        initviews()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.invoice))

        MainBus.getInstance().busObservable.ofType(ConnectedStudentEvent::class.java).subscribe(eventobserver)
        presenter = InvoicePresenter(this)

        adapter = InvoiceAdapter(null, this,userId.toString(),orgId)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(this@InvoiceListingActivity)

        // fab.imageResource = R.drawable.ic_person_add_white_24dp

        setUpItemTouchHelper()

        /*ItemClickSupport.addTo(recyclerView).setOnItemClickListener { recyclerView, position, v ->
            val model: ConnectedListModel = adapter?.getItem(position) as ConnectedListModel
            presenter?.register(model)
            //startActivity<HomeActivity>()
           // finish()

        }*/
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,

                        Manifest.permission.READ_EXTERNAL_STORAGE,

                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    }
                }).check()
/*
        showResults()
*/

        // onRefresh()
    }

    override fun onUpdateNeeded(updateUrl: String) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle(getString(R.string.app_name))

        // Display a message on alert dialog
        builder.setMessage("A new version of classroomJOIN app avaliable in PlayStore, please continue to update.")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Continue") { dialog, which ->
            // Do something when user press the positive button
            redirectStore(updateUrl!!);

            // Change the app background color
            // root_layout.setBackgroundColor(Color.RED)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No, thanks") { dialog, which ->
            // finish();
        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onRefresh() {
        super.onRefresh()
        getData()
    }

    private val eventobserver = object : Observer<ConnectedStudentEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: ConnectedStudentEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog?.dismiss()
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()

                    System.out.println("Data--->" + "results!!.size--->" + event.results!!.size)

                    /*if(event.results!!.size==1){
                       // var viewItem:View = recyclerView.getLayoutManager().findViewByPosition(1)
                      //  val selectedView = recyclerView.getChildAt(0)

                        val model: ConnectedListModel = adapter?.getItem(0) as ConnectedListModel
                        presenter?.register(model)
                        startActivity<Student_Home_Activity>()
                       // finish()
                    }*/
                }
                Event.NO_RESULT -> {
                    if (adapter!!.itemCount == 0)
                        showNoResults(getString(R.string.no_connected_students_added))
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

                    System.out.println("Data--->" + "results!!.121313--->" + event.results!!.size)

                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_connected_students_page, menu)
        return true
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
        // getData()
        onRefresh()

    }

    private fun getData() {
        if (NetworkHelper.isOnline(this@InvoiceListingActivity)) {
            if (adapter!!.itemCount == 0) showLoading()
            presenter!!.getdata()
        } else
            Emit(ConnectedStudentEvent(Event.NO_INTERNET))
    }

    private fun showdeletealert(position: Int) {
        var alert = AlertDialog.Builder(this@InvoiceListingActivity)
        alert.setTitle(getString(R.string.app_name))
        alert.setMessage(getString(R.string.delete_confirm))
        alert.setPositiveButton(getString(R.string.btn_yes)) { dialog, whichButton ->
            studentid = (adapter?.getItem(position) as ConnectedListModel).id.toString()
            deletStudent()
            adapter!!.notifyItemRemoved(position)
        }
        alert.setNegativeButton(getString(R.string.btn_no)) { dialog, whichButton ->
            dialog.dismiss()
            adapter?.notifyItemChanged(position)
        }
        alert.show()
    }

    private fun deletStudent() {
        if (NetworkHelper.isOnline(this@InvoiceListingActivity)) {
            progressdialog!!.show()
            presenter!!.deleteitem(studentid!!)
        } else
            showerrorDialog2(getString(R.string.noInternet))
    }

    private fun showerrorDialog2(message: String) {
        var alert = AlertDialog.Builder(this@InvoiceListingActivity)
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
        } else if (item.itemId == R.id.action_bar_logout) {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().clear().apply()
            startActivity(Intent(this@InvoiceListingActivity, LandingActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finishAffinity()
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
                xMark = AppCompatResources.getDrawable(this@InvoiceListingActivity, R.drawable.ic_delete_green_24dp)
                // xMark =  VectorDrawableCompat.create(getResources(), R.drawable.ic_delete_green_24dp_data, null);
                // xMark = AppCompatDrawableManager.get().getDrawable(this@InvoiceListingActivity, R.drawable.ic_delete_green_24dp_data);

                xMark!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                xMarkMargin = resources.getDimension(R.dimen.ic_clear_margin).toInt()
                initiated = true
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView
                if (viewHolder.adapterPosition == -1) {
                    return
                }

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


