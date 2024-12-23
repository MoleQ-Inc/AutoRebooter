package com.moleq.autorebooter;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * @author jzheng
 * @date 2024/12/12
 */
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_ANNOUNCEMENT && accessibilityEvent.getClassName().equals(RequestRebootReceiver.class.getName())) {
            try {
                performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
                for (int i = 0; i < 10; i++) {
                    List<AccessibilityNodeInfo> restartNodeList = getRootInActiveWindow().findAccessibilityNodeInfosByText("Restart");
                    if (restartNodeList != null && !restartNodeList.isEmpty()) {
                        AccessibilityNodeInfo restartBtn = restartNodeList.get(0);
                        for (int j = 0; j < 5; j++) {
                            if (restartBtn == null) {
                                return;
                            }
                            if (restartBtn.getActionList().stream().anyMatch(action -> action.getId() == 16)) {
                                restartBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                break;
                            }
                            restartBtn = restartBtn.getParent();
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
