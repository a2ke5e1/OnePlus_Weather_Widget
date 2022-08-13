package com.a2krocks.widget.services;


import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLocker {

    private static PowerManager.WakeLock wakeLock;

    //wake the device
    public static void acquire(Context context) {
        if (wakeLock != null) {
            wakeLock.release();
        }

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE,
                "WIDGET: Wake lock acquired!"
        );
        wakeLock.acquire(1500);
    }

    public static void release() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;
    }

}
