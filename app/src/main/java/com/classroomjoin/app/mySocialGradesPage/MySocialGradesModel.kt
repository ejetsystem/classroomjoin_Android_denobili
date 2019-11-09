package com.classroomjoin.app.mySocialGradesPage

import android.support.annotation.Nullable
import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MySocialGradesModel() : DisplayItem, RealmObject() {
    @SerializedName("socialGradeName")
    var name: String = ""
    @SerializedName("id")
    @PrimaryKey
    var id: Int? = 0
    @SerializedName("socialgradetype")
    var type: String = ""
    @SerializedName("point")
    var point: Int? = 0
    @Nullable
    @SerializedName("flag")
    var flag: Int? = 0

    @SerializedName("activeFlag")
    var activeFlag = 1

    @SerializedName("orgId")
    @Nullable
    var org_id: Int? = 0
    var isselected: Boolean = false

    constructor(name: String, id: Int?, type: String, point: Int, flag: Int?, org_id: Int?) : this() {
        this.name = name
        if (id != null) this.id = id
        this.type = type
        this.point = point
        this.flag = flag
        this.org_id = org_id
    }
}