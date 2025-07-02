
package com.medtahabakri.plugins.capedometer;

import android.app.Service;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class StepService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }

        NotificationUtils.createNotificationChannel(this);
        startForeground(1, NotificationUtils.getNotification(this));
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float steps = event.values[0];
        Log.d("StepService", "Steps in background: " + steps);
        
        Intent intent = new Intent("StepUpdateBroadcast");
        intent.putExtra("steps", steps);
        sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
