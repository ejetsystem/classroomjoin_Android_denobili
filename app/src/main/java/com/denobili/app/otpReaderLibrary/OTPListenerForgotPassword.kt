package com.denobili.app.otpReaderLibrary

/**
 * Created by swarajpal on 13-12-2015.
 */
interface OTPListenerForgotPassword {

    fun otpReceived(messageText: String)
}
