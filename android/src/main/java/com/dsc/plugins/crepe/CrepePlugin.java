package com.dsc.plugins.crepe;

import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.PluginMethod;

// Import your graphquery classes
import android.view.Display;
import android.view.WindowManager;
import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import com.dsc.plugins.crepe.graphquery.ontology.UISnapshot;
import com.dsc.plugins.crepe.graphquery.ontology.OntologyQuery;
import com.dsc.plugins.crepe.graphquery.ontology.SugiliteEntity;
import com.dsc.plugins.crepe.graphquery.model.Node;
import java.util.Set;

@CapacitorPlugin(name = "Crepe")
public class CrepePlugin extends Plugin {

    private Crepe implementation = new Crepe();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void startAccessibilityService(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, MyAccessibilityService.class);
        context.startService(intent);
        call.resolve();
    }

    @PluginMethod
    public void stopAccessibilityService(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, MyAccessibilityService.class);
        context.stopService(intent);
        call.resolve();
    }

    @PluginMethod
    public void getAccessibilityData(PluginCall call) {
        // Example logic to retrieve data from the service
        String data = MyAccessibilityService.getLatestAccessibilityData();
        call.resolve(new JSObject().put("data", data));
    }

    private UISnapshot uiSnapshot;
    private static final String TAG = "CrepePlugin";

    @PluginMethod
    public void initializeGraphQuery(PluginCall call) {
        AccessibilityNodeInfo rootNode = MyAccessibilityService.getLatestRootNode();
        Log.d(TAG, "Initializing graph query. Root node available: " + (rootNode != null));
        if (rootNode != null) {
            // Get the Display instance
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();

            // Get package and activity names from the root node
            String packageName = rootNode.getPackageName() != null ? rootNode.getPackageName().toString() : "";
            String activityName = ""; // We can get this from the root node's window info if needed

            // Initialize UISnapshot with all required parameters
            uiSnapshot = new UISnapshot(display, rootNode, true, packageName, activityName);

            JSObject ret = new JSObject();
            ret.put("success", true);
            call.resolve(ret);
        } else {
            call.reject("No root node available");
        }
    }

    @PluginMethod
    public void updateSnapshot(PluginCall call) {
        AccessibilityNodeInfo rootNode = MyAccessibilityService.getLatestRootNode();
        if (rootNode != null && uiSnapshot != null) {
            try {
                uiSnapshot.update(rootNode);
                JSObject ret = new JSObject();
                ret.put("success", true);
                call.resolve(ret);
            } catch (Exception e) {
                call.reject("Failed to update snapshot: " + e.getMessage());
            }
        } else {
            call.reject("No root node or snapshot available");
        }
    }

    @PluginMethod
    public void queryGraph(PluginCall call) {
        if (uiSnapshot == null) {
            call.reject("Graph query system not initialized");
            return;
        }

        String pattern = call.getString("pattern");
        if (pattern == null || pattern.isEmpty()) {
            call.reject("Query pattern is required");
            return;
        }

        try {
            // Create and execute the query
            OntologyQuery query = OntologyQuery.parseQuery(pattern);
            Set<SugiliteEntity> results = query.execute(uiSnapshot);

            // Convert results to JSON
            JSObject ret = new JSObject();
            JSArray resultArray = new JSArray();

            for (SugiliteEntity entity : results) {
                if (entity.getEntityValue() instanceof Node) {
                    Node node = (Node) entity.getEntityValue();
                    JSObject nodeObj = new JSObject();

                    // Add node properties
                    nodeObj.put("text", node.getText());
                    nodeObj.put("contentDescription", node.getContentDescription());
                    nodeObj.put("viewId", node.getViewId());
                    nodeObj.put("packageName", node.getPackageName());
                    nodeObj.put("className", node.getClassName());
                    nodeObj.put("boundsInScreen", node.getBoundsInScreen());
                    nodeObj.put("isClickable", node.getClickable());
                    nodeObj.put("isEditable", node.getEditable());
                    nodeObj.put("isScrollable", node.getScrollable());

                    resultArray.put(nodeObj);
                }
            }

            ret.put("results", resultArray);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Query failed: " + e.getMessage());
        }
    }

}
