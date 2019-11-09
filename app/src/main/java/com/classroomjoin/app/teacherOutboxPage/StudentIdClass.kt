package com.classroomjoin.app.teacherOutboxPage

import io.realm.RealmObject


open class StudentIdClass():RealmObject() {
    var student_id:Int?=null

    constructor(student_id:Int):this(){
        this.student_id=student_id
    }
}