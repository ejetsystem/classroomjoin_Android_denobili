package com.denobili.app.outboxExpiredPage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.denobili.app.R

class OutboxExpiredPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_check_outbox)
    }
}
