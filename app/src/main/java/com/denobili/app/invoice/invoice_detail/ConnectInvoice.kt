package com.denobili.app.invoice.invoice_detail

import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class ConnectInvoice: ListEvent<DisplayItem> {


    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

    constructor(event: Event, results: List<DisplayItem>, error: String) : super(event, results, error) {}

    constructor(error: String) : super(error) {}

    constructor(results: List<DisplayItem>,result: List<DisplayItem>,res: List<DisplayItem>) : super(results,result,res) {}


}
