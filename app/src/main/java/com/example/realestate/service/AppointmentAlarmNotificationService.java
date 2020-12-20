package com.example.realestate.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by pvanngoc on 12/25/14.
 */
public class AppointmentAlarmNotificationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
}
