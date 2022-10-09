package com.a2krocks.widget.services

import android.content.Context
import android.os.PowerManager

object WakeLocker {
    private var wakeLock: PowerManager.WakeLock? = null

    //wake the device
    fun acquire(context: Context) {
        if (wakeLock != null) {
            wakeLock.release()
        }
        val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "WIDGET: Wake lock acquired!"
        )
        wakeLock.acquire(1500)
    }

    fun release() {
        if (wakeLock != null) {
            wakeLock.release()
        }
        wakeLock = null
    }
}
