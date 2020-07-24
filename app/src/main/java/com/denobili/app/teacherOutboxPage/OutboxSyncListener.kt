package com.denobili.app.teacherOutboxPage



interface OutboxSyncListener {
    fun onSyncStateChanged(model: OutboxModel,position: Int)
}