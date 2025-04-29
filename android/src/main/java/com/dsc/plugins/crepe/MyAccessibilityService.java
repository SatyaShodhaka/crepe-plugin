import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "AccessibilityService";
    private static String latestAccessibilityData = "";
    private static AccessibilityNodeInfo latestRootNode = null; // <-- Add this line

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        latestAccessibilityData = event.getText().toString();
        latestRootNode = getRootInActiveWindow(); // <-- Add this line
        Log.d(TAG, "Event: " + latestAccessibilityData);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service Interrupted");
    }

    public static String getLatestAccessibilityData() {
        return latestAccessibilityData;
    }

    public static AccessibilityNodeInfo getLatestRootNode() {
        return latestRootNode;
    }
}