package com.classroomjoin.app.helper_utils


public class Emit(event: Any) {
    init {
        MainBus.getInstance().post(event)
    }

}