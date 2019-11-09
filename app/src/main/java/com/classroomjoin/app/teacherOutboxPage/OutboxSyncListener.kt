package com.classroomjoin.app.teacherOutboxPage



interface OutboxSyncListener {
    fun onSyncStateChanged(model: OutboxModel,position: Int)
}