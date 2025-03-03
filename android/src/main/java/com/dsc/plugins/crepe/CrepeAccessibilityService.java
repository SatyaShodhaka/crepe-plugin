package com.dsc.plugins.crepe;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class CrepeAccessibilityService extends AccessibilityService {
    private static final String TAG = "CrepeAccessibilityService";
    private static String latestAccessibilityData = "";

    @Override
    public void onServiceConnected() {
        Log.d(TAG, "Accessibility Service Connected!");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
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
        Log.e(TAG, "Accessibility Service Interrupted");
    }

    public static String getLatestAccessibilityData() {
        return latestAccessibilityData;
    }
}
