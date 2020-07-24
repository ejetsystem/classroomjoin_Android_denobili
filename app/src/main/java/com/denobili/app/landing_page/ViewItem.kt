package com.denobili.app.landing_page

import java.io.Serializable

class ViewItem : Serializable {
    var imageId: Int
        internal set
    var imageDrawable: Int
        internal set

    constructor(id: Int, drawable: Int) {
        this.imageId = id
        this.imageDrawable = drawable
    }
}