package com.denobili.app.studentallEventsPage

import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class StudentEvents(event: Event): ListEvent<DisplayItem>(event) {

    var message:String?=null
    var resul:List<DisplayItem>?=ArrayList<DisplayItem>()


    constructor(event: Event, msg:String):this(event){
        message=msg
    }

    constructor(event: Event, list:ArrayList<DisplayItem>?=ArrayList()):this(event){
        resul=list
    }

}