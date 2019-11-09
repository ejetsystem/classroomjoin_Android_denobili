package com.classroomjoin.app.otpReaderLibrary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.text.TextUtils
import android.util.Log


/**
 * Created by swarajpal on 13-12-2015.
 * BroadcastReceiver OtpReader for receiving and processing the SMS messages.
 */
class OtpReader : BroadcastReceiver() {


    companion object {

        /**
         * Constant TAG for logging key.
         */
        private val TAG = "OtpReader"

        /**
         * The bound OTP Listener that will be trigerred on receiving message.
         */
        private var otpListenerForgotPassword: OTPListenerForgotPassword? = null

        /**
         * The bound OTP Listener that will be trigerred on receiving message.
         */
        private var otpListenerSignup: OTPListenerSignup? = null

        /**
         * The Sender number string.
         */
        private var receiverString: String? = null

        /**
         * The Sender number string.
         */
        private var receiverString11: String? = null

        /**
         * Binds the sender string and listener for callback.
         *
         * @param listener
         * @param sender
         */
        fun bind(listener: OTPListenerForgotPassword, sender: String) {
            otpListenerForgotPassword = listener
            receiverString = sender
        }

        fun bind(listener: OTPListenerSignup, sender: String) {
            otpListenerSignup = listener
            receiverString11 = sender
        }

        /**
         * Unbinds the sender string and listener for callback.
         */
        fun unbind() {
            otpListenerForgotPassword = null
            receiverString = null
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {

            val pdusArr = bundle.get("pdus") as Array<Any>

            for (i in pdusArr.indices) {

                val currentMessage = SmsMessage.createFromPdu(pdusArr[i] as ByteArray)
                val senderNum = currentMessage.displayOriginatingAddress
                val message = currentMessage.displayMessageBody
                Log.i(TAG, "senderNum: $senderNum message: $message")

                if (!TextUtils.isEmpty(receiverString) && senderNum.contains(receiverString!!)) { //If message received is from required number.
                    //If bound a listener interface, callback the overriden method.
                    if (otpListenerForgotPassword != null) {
                        otpListenerForgotPassword!!.otpReceived(message)
                    }


                } else {

                    if (otpListenerSignup != null) {
                        otpListenerSignup!!.otpReceived(message)
                    }
                }


            }
        }
    }


}
