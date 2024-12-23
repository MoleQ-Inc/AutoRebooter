package com.moleq.autorebooter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * @author jzheng
 * @date 2024/12/18
 */
public class RequestRebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (manager.isEnabled()) {
            AccessibilityEvent e = AccessibilityEvent.obtain();
            e.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            e.setClassName(getClass().getName());
            e.setPackageName(context.getPackageName());
            manager.sendAccessibilityEvent(e);
        }
    }
}
