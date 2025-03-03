package com.dsc.plugins.crepe;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Crepe")
public class CrepePlugin extends Plugin {

    @PluginMethod
    public void requestAccessibilityPermission(PluginCall call) {
        Context context = getActivity().getApplicationContext();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        call.resolve();
    }

    @PluginMethod
    public void startAccessibilityService(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, CrepeAccessibilityService.class);
        context.startService(intent);
        call.resolve();
    }

    @PluginMethod
    public void stopAccessibilityService(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, CrepeAccessibilityService.class);
        context.stopService(intent);
        call.resolve();
    }

    @PluginMethod
    public void getAccessibilityData(PluginCall call) {
        String data = CrepeAccessibilityService.getLatestAccessibilityData();
        call.resolve(new JSObject().put("data", data));
    }

    // Method to Start Floating Widget
    @PluginMethod
    public void startFloatingWidget(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, WidgetService.class);
        context.startService(intent);
        call.resolve();
    }

    // Method to Stop Floating Widget
    @PluginMethod
    public void stopFloatingWidget(PluginCall call) {
        Context context = getContext();
        Intent intent = new Intent(context, WidgetService.class);
        context.stopService(intent);
        call.resolve();
    }
}
