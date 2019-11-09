package com.classroomjoin.app.teacherOutboxPage

import io.realm.RealmObject


open class SocialIdClass():RealmObject() {
    var student_id:String?=null

    constructor(student_id:String):this(){
        this.student_id=student_id
    }
}