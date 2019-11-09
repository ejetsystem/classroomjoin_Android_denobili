package com.classroomjoin.app

import android.os.Bundle
import android.view.MenuItem
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import kotlinx.android.synthetic.main.report_message_details.*
import org.jetbrains.anko.AnkoLogger

class ReportsMessageDetails : LocalizationActivity(), AnkoLogger {

    companion object eventdiarydetail {
        val STUDENT_NAME = "student_name"
        val CLASS_NAME = "class_name"
        val MESSAGE = "message"
        val MESSAGE1 = "message1"
        val STATUS = "status"
        val DATE = "date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_message_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setTitle(getString(R.string.message_details))

        if (intent.hasExtra(STUDENT_NAME)) {
            student_name_message.text = intent.getStringExtra(STUDENT_NAME)
        }
        if (intent.hasExtra(CLASS_NAME)) {
            class_name_message.text = intent.getStringExtra(CLASS_NAME)
        }
        if (intent.hasExtra(MESSAGE)) {
            if (intent.getStringExtra(MESSAGE) != null && !intent.getStringExtra(MESSAGE).equals(""))
                message_reports_data.text = intent.getStringExtra(MESSAGE)
            else
                message_reports_data.text = intent.getStringExtra(MESSAGE1)

        }
        if (intent.hasExtra(STATUS)) {
            if (intent.getStringExtra(STATUS) != null && intent.getStringExtra(STATUS).equals("1"))
                message_status.text = getString(R.string.read)
            else if (intent.getStringExtra(STATUS) != null && intent.getStringExtra(STATUS).equals("0"))
                message_status.text = getString(R.string.delivered)
        }
        if (intent.hasExtra(DATE)) {
            message_date.text = intent.getStringExtra(DATE)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}