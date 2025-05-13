package com.dsc.plugins.crepe;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
    private static AccessibilityNodeInfo latestRootNode;
    private static String latestAccessibilityData;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        latestRootNode = getRootInActiveWindow();
        if (latestRootNode != null) {
            latestAccessibilityData = latestRootNode.toString();
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Accessibility Service interrupted");
    }

    public static AccessibilityNodeInfo getLatestRootNode() {
        return latestRootNode;
    }

    public static String getLatestAccessibilityData() {
        return latestAccessibilityData;
    }
}