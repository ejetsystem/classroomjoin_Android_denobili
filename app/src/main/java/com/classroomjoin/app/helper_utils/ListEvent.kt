package com.classroomjoin.app.helper_utils

import android.util.Log

/*
  Created by Ajesh on 27/12/2016
*/
open class ListEvent<T> : BaseEvent {

    var results: List<T>? = null
    var error: String? = null

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event) {
        this.error = error
    }

    constructor(event: Event, results: List<T>, error: String) : super(event) {
        this.results = results
        this.error = error
    }

    constructor(error: String) : super(Event.ERROR) {
        this.error = error
    }

    constructor(results: List<T>) : super(Event.RESULT) {
        Log.e("called here", "now")
        this.results = results
    }
}
