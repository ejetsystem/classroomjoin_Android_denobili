package com.denobili.app.studentDiaryPage

import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class StudentDiaryEvents(event: Event): ListEvent<DisplayItem>(event) {

    var message:String?=null
    var totalPage1:Int?=null
    var totalElement1:Int?=null
    var resul:List<DisplayItem>?=ArrayList<DisplayItem>()


    constructor(event: Event, msg:String):this(event){
        message=msg
    }

    constructor(event: Event, list:ArrayList<DisplayItem>?=ArrayList()):this(event){
        resul=list
    }
    constructor(event: Event, list:ArrayList<DisplayItem>?=ArrayList(),totalPage:Int,totalElement:Int):this(event){
        resul=list
        totalPage1=totalPage
        totalElement1=totalElement
    }

}