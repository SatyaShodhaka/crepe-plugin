package com.dsc.plugins.crepe;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "AccessibilityService";
    private static String latestAccessibilityData = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Capture UI content here
        latestAccessibilityData = event.getText().toString();
        Log.d(TAG, "Event: " + latestAccessibilityData);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service Interrupted");
    }

    public static String getLatestAccessibilityData() {
        return latestAccessibilityData;
    }
}
