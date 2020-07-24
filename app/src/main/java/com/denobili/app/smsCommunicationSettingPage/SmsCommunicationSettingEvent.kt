package com.denobili.app.smsCommunicationSettingPage

import com.denobili.app.emailSettingPage.CommunicationModel
import com.denobili.app.emailSettingPage.CommunicationSettingModel
import com.denobili.app.helper_utils.BaseEvent
import com.denobili.app.helper_utils.Event


class SmsCommunicationSettingEvent(event: Event): BaseEvent(event) {

    var model: CommunicationSettingModel?=null
    var error:String?=null
    var result: List<CommunicationModel>?=null

    constructor(event: Event, model: CommunicationSettingModel):this(event){
        this.model=model
    }

    constructor(event: Event, model: List<CommunicationModel>):this(event){
        this.result=model
    }

    constructor(event: Event, error: String):this(event){
        this.error=error
    }
}