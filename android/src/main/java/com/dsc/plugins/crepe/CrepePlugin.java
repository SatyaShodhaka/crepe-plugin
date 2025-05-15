package com.dsc.plugins.crepe;

import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

// Import your graphquery classes
import android.view.Display;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.dsc.plugins.crepe.graphquery.ontology.UISnapshot;
import com.dsc.plugins.crepe.graphquery.ontology.OntologyQuery;
import com.dsc.plugins.crepe.graphquery.ontology.SugiliteEntity;
import com.dsc.plugins.crepe.graphquery.model.Node;
import com.dsc.plugins.crepe.model.CollectorData;
import java.util.Set;
import com.google.gson.Gson;
import java.util.Map;

@CapacitorPlugin(name = "Crepe")
public class CrepePlugin extends Plugin {

    private Crepe implementation = new Crepe();
    private UISnapshot uiSnapshot;
    private static final String TAG = "CrepePlugin";

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void startAccessibility(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, MyAccessibilityService.class);
        context.startService(intent);
        call.resolve();
    }

    @PluginMethod
    public void stopAccessibility(PluginCall call) {
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
            OntologyQuery query = OntologyQuery.deserialize(pattern);
            Set<SugiliteEntity> results = query.executeOn(uiSnapshot);

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

    @PluginMethod
    public void matchCollectorData(PluginCall call) {
        if (uiSnapshot == null) {
            Log.e(TAG, "Graph query system not initialized");
            call.reject("Graph query system not initialized");
            return;
        }

        String collectorDataJson = call.getString("collectorData");
        if (collectorDataJson == null || collectorDataJson.isEmpty()) {
            Log.e(TAG, "Collector data is empty or null");
            call.reject("Collector data is required");
            return;
        }

        try {
            // Parse the collector data JSON
            Log.d(TAG, "Parsing collector data JSON: " + collectorDataJson);
            Gson gson = new Gson();
            CollectorData collectorData = gson.fromJson(collectorDataJson, CollectorData.class);

            if (collectorData == null) {
                Log.e(TAG, "Failed to parse collector data - result is null");
                call.reject("Failed to parse collector data");
                return;
            }

            // Get collectors and datafields
            Map<String, CollectorData.Collector> collectors = collectorData.getCollector();
            Map<String, CollectorData.DataField> dataFields = collectorData.getDatafield();

            if (collectors == null || dataFields == null) {
                Log.e(TAG, "Collectors or datafields map is null in collector data");
                call.reject("Invalid collector data format - missing collectors or datafields");
                return;
            }

            Log.d(TAG, "Successfully parsed collector data with " + collectors.size() +
                    " collectors and " + dataFields.size() + " data fields");

            // Create a result object to store matches
            JSObject result = new JSObject();
            JSArray matches = new JSArray();

            // Process each collector
            for (Map.Entry<String, CollectorData.Collector> collectorEntry : collectors.entrySet()) {
                String collectorId = collectorEntry.getKey();
                CollectorData.Collector collector = collectorEntry.getValue();

                Log.d(TAG, "Processing collector: " + collector.getAppName() +
                        " (ID: " + collectorId + ")");

                // Find all datafields for this collector
                for (Map.Entry<String, CollectorData.DataField> dataFieldEntry : dataFields.entrySet()) {
                    CollectorData.DataField dataField = dataFieldEntry.getValue();

                    // Skip datafields that don't belong to this collector
                    if (!collectorId.equals(dataField.getCollectorId())) {
                        continue;
                    }

                    String graphQuery = dataField.getGraphQuery();
                    Log.d(TAG, "Processing data field: " + dataField.getName() +
                            " (ID: " + dataField.getDatafieldId() + ")");

                    if (graphQuery != null && !graphQuery.isEmpty()) {
                        Log.d(TAG, "Executing graph query: " + graphQuery);
                        // Execute the graph query
                        OntologyQuery query = OntologyQuery.deserialize(graphQuery);
                        Set<SugiliteEntity> queryResults = query.executeOn(uiSnapshot);
                        Log.d(TAG, "Query returned " + queryResults.size() + " results");

                        if (!queryResults.isEmpty()) {
                            // Create a match object
                            JSObject match = new JSObject();
                            match.put("collectorId", collectorId);
                            match.put("collectorName", collector.getAppName());
                            match.put("datafieldId", dataField.getDatafieldId());
                            match.put("name", dataField.getName());
                            match.put("graphQuery", graphQuery);

                            // Add the matched nodes
                            JSArray matchedNodes = new JSArray();
                            for (SugiliteEntity entity : queryResults) {
                                if (entity.getEntityValue() instanceof Node) {
                                    Node node = (Node) entity.getEntityValue();
                                    JSObject nodeObj = new JSObject();
                                    nodeObj.put("text", node.getText());
                                    nodeObj.put("contentDescription", node.getContentDescription());
                                    nodeObj.put("viewId", node.getViewId());
                                    nodeObj.put("packageName", node.getPackageName());
                                    nodeObj.put("className", node.getClassName());
                                    nodeObj.put("boundsInScreen", node.getBoundsInScreen());
                                    nodeObj.put("isClickable", node.getClickable());
                                    nodeObj.put("isEditable", node.getEditable());
                                    nodeObj.put("isScrollable", node.getScrollable());
                                    matchedNodes.put(nodeObj);
                                    Log.d(TAG, "Matched node: " + node.getText() +
                                            " (class: " + node.getClassName() + ")");
                                }
                            }
                            match.put("matchedNodes", matchedNodes);
                            matches.put(match);
                            Log.d(TAG, "Added match for data field: " + dataField.getName() +
                                    " in collector: " + collector.getAppName());
                        } else {
                            Log.d(TAG, "No matches found for data field: " + dataField.getName() +
                                    " in collector: " + collector.getAppName());
                        }
                    } else {
                        Log.w(TAG, "Empty graph query for data field: " + dataField.getName() +
                                " in collector: " + collector.getAppName());
                    }
                }
            }

            result.put("matches", matches);
            Log.d(TAG, "Completed matching process. Found " + matches.length() + " total matches");
            call.resolve(result);
        } catch (Exception e) {
            Log.e(TAG, "Failed to process collector data: " + e.getMessage(), e);
            call.reject("Failed to process collector data: " + e.getMessage());
        }
    }

    @PluginMethod
    public void requestAccessibilityPermission(PluginCall call) {
        try {
            Context context = getContext();
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to open accessibility settings: " + e.getMessage());
        }
    }

    @PluginMethod
    public void isAccessibilityServiceEnabled(PluginCall call) {
        try {
            Context context = getContext();
            String serviceName = context.getPackageName() + "/" + MyAccessibilityService.class.getCanonicalName();
            int accessibilityEnabled = android.provider.Settings.Secure.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED,
                    0);

            boolean isEnabled = false;
            if (accessibilityEnabled == 1) {
                String settingValue = android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                if (settingValue != null) {
                    isEnabled = settingValue.contains(serviceName);
                }
            }

            JSObject ret = new JSObject();
            ret.put("enabled", isEnabled);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Failed to check accessibility service status: " + e.getMessage());
        }
    }

}
