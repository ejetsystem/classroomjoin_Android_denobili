package com.classroomjoin.app.helper_utils


enum class Event {
    RESULT,
    NO_INTERNET,
    SERVER_ERROR,
    ERROR,
    NO_RESULT,
    POST_SUCCESS,
    POST_FAILURE,
    DELETED_ITEM,
    WISHLIST_CHANGE,
    INITAL_DATA_LOADED,
    BUY_NOW,
    CART_OK,
    CART_ERROR,
    DELETION_FAILURE,
    COM_DE_ACTIVATE_SUCCESS,
    COM_DE_ACTIVATE_FAILED_,
    SAVED_TO_OUTBOX;


    override fun toString(): String {
        return super.toString()
    }
}
