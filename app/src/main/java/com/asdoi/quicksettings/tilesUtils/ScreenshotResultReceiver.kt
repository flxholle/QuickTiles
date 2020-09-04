package com.asdoi.quicksettings.tilesUtils

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class ScreenshotResultReceiver(handler: Handler) : ResultReceiver(handler) {

    private var mReceiver: Receiver? = null

    interface Receiver {
        fun onReceiveResult(resultCode: Int, resultData: Bundle)

    }

    fun setReceiver(receiver: Receiver) {
        mReceiver = receiver
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        if (mReceiver != null) {
            mReceiver!!.onReceiveResult(resultCode, resultData)
        }
    }
}