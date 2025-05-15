package com.dsc.plugins.crepe;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "CrepeEvents";
    private static AccessibilityNodeInfo latestRootNode;
    private static String latestAccessibilityData;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        latestRootNode = getRootInActiveWindow();
        if (latestRootNode != null) {
            latestAccessibilityData = latestRootNode.toString();
        }

        Log.d(TAG, "Latest Root Node: " + latestRootNode.toString());

        String eventType = AccessibilityEvent.eventTypeToString(event.getEventType());
        String packageName = (event.getPackageName() != null) ? event.getPackageName().toString() : "Unknown";
        String eventText = (event.getText() != null) ? event.getText().toString() : "No text";

        Log.d(TAG, "✅ Accessibility Event Captured:");
        Log.d(TAG, "   📌 Type: " + eventType);
        Log.d(TAG, "   📦 Package: " + packageName);
        Log.d(TAG, "   📝 Text: " + eventText);

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