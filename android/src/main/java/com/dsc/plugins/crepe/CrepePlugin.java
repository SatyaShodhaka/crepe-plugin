package com.dsc.plugins.crepe;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

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
}


