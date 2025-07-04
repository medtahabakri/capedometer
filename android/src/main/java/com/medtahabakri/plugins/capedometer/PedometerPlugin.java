package com.medtahabakri.plugins.capedometer;

import java.util.List;
import java.util.ArrayList;

import android.Manifest;
import android.os.Build;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.JSObject;

@CapacitorPlugin(name = "Pedometer")
public class PedometerPlugin extends Plugin implements SensorEventListener {
    
    // public static int STOPPED = 0;
    // public static int STARTING = 1;
    // public static int RUNNING = 2;

    // private int status = 0;

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private PluginCall savedCall;

    private BroadcastReceiver stepReceiver;

    @Override
    public void load() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                float steps = intent.getFloatExtra("steps", 0);

                JSObject data = new JSObject();
                data.put("steps", steps);
                notifyListeners("stepUpdate", data);
            }
        };


        ContextCompat.registerReceiver(getContext(), stepReceiver, new IntentFilter("StepUpdateBroadcast"), ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @PluginMethod
    public void start(PluginCall call) {
        savedCall = call;
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            call.resolve();
        } else {
            call.reject("Step sensor not available");
        }
    }

    @PluginMethod
    public void stop(PluginCall call) {
        sensorManager.unregisterListener(this);
        call.resolve();
    }

    @PluginMethod
    public void startBackground(PluginCall call) {
        Intent intent = new Intent(getContext(), StepService.class);
        ContextCompat.startForegroundService(getContext(), intent);
        call.resolve();
    }

    @PluginMethod
    public void stopBackground(PluginCall call) {
        Intent intent = new Intent(getContext(), StepService.class);
        getContext().stopService(intent);
        call.resolve();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float steps = event.values[0];

        notifyListeners("stepUpdate", new JSObject().put("steps", steps));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }

    @PluginMethod
    public void checkPermission(PluginCall call) {
        boolean hasActivityRecognition =
        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;

        boolean hasForegroundService = true;
        boolean hasPhysicalActivityService = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasForegroundService =
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;

            hasPhysicalActivityService =
                ContextCompat.checkSelfPermission(getContext(), "android.permission.FOREGROUND_SERVICE_PHYSICAL_ACTIVITY") == PackageManager.PERMISSION_GRANTED;
        }

        JSObject result = new JSObject();
        result.put("granted", hasActivityRecognition && hasForegroundService && hasPhysicalActivityService);
        call.resolve(result);
        
    }


    @PluginMethod
    public void requestPermission(PluginCall call) {
        List<String> permissionsToRequest = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.FOREGROUND_SERVICE)
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE);
            }

            // Use string literal for API 34+ permission
            if (ContextCompat.checkSelfPermission(getContext(), "android.permission.FOREGROUND_SERVICE_PHYSICAL_ACTIVITY")
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add("android.permission.FOREGROUND_SERVICE_PHYSICAL_ACTIVITY");
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                getActivity(),
                permissionsToRequest.toArray(new String[0]),
                999
            );
        }
        JSObject result = new JSObject();
        result.put("granted", true); // You may want to verify after request
        call.resolve(result);
    }

    // Handle the result of the permission request
    @Override
    public void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            return;
        }

        if (requestCode == 1234) {
            JSObject result = new JSObject();
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            result.put("granted", granted);
            if (granted) {
                savedCall.resolve(result);
            } else {
                savedCall.reject("Permission not granted");
            }
        }
    }
    
    @PluginMethod
    public void getStepsBetween(PluginCall call) {
        call.reject("Step sensor query not available in android");
    }

//    @PluginMethod
//    public void getStoredSteps(PluginCall call) {
//        SharedPreferences prefs = getContext().getSharedPreferences("hra_step_counter_prefs", Context.MODE_PRIVATE);
//        int steps = prefs.getFloat("steps", 0);
//        JSObject ret = new JSObject();
//        ret.put("steps", steps);
//        call.resolve(ret);
//    }

    @Override
    protected void handleOnDestroy() {
        // Unregister receiver
        if (stepReceiver != null) {
            getContext().unregisterReceiver(stepReceiver);
            Intent intent = new Intent(getContext(), StepService.class);
            getContext().stopService(intent);
        }
    }
}
