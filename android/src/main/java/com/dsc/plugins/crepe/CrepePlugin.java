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
            Log.d(TAG, "Parsing collector data JSON");
            Gson gson = new Gson();
            CollectorData collectorData = gson.fromJson(collectorDataJson, CollectorData.class);
            Log.d(TAG, "Successfully parsed collector data with " + collectorData.getDataFields().size() + " data fields");

            // Create a result object to store matches
            JSObject result = new JSObject();
            JSArray matches = new JSArray();

            // Iterate through data fields and try to match them
            for (Map.Entry<String, CollectorData.DataField> entry : collectorData.getDataFields().entrySet()) {
                CollectorData.DataField dataField = entry.getValue();
                String graphQuery = dataField.getGraphQuery();
                
                Log.d(TAG, "Processing data field: " + dataField.getName() + " (ID: " + dataField.getDatafieldId() + ")");
                
                if (graphQuery != null && !graphQuery.isEmpty()) {
                    Log.d(TAG, "Executing graph query: " + graphQuery);
                    // Execute the graph query
                    OntologyQuery query = OntologyQuery.deserialize(graphQuery);
                    Set<SugiliteEntity> queryResults = query.executeOn(uiSnapshot);
                    Log.d(TAG, "Query returned " + queryResults.size() + " results");

                    if (!queryResults.isEmpty()) {
                        // Create a match object
                        JSObject match = new JSObject();
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
                                Log.d(TAG, "Matched node: " + node.getText() + " (class: " + node.getClassName() + ")");
                            }
                        }
                        match.put("matchedNodes", matchedNodes);
                        matches.put(match);
                        Log.d(TAG, "Added match for data field: " + dataField.getName());
                    } else {
                        Log.d(TAG, "No matches found for data field: " + dataField.getName());
                    }
                } else {
                    Log.w(TAG, "Empty graph query for data field: " + dataField.getName());
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

}
