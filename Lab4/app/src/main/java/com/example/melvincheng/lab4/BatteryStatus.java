package com.example.melvincheng.lab4;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;

public class BatteryStatus extends BroadcastReceiver {
    public BatteryStatus() {
    }
    int BATTERY_STATUS_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = "";
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            message += context.getString(R.string.charging);
        } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
            message += context.getString(R.string.full);
        } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING){
            message += context.getString(R.string.discharging);
        } else {
            message += context.getString(R.string.notCharging);
        }
        message += "\n";


        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            message += context.getString(R.string.usbPlugged);
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            message += context.getString(R.string.acPlugged);
        } else {
            message += context.getString(R.string.notPlugged);
        }
        message += "\n";


        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        if (health != BatteryManager.BATTERY_HEALTH_GOOD) {
            if (health == BatteryManager.BATTERY_HEALTH_COLD) {
                message += context.getString(R.string.cold);
            } else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
                message += context.getString(R.string.dead);
            } else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                message += context.getString(R.string.overheat);
            } else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                message += context.getString(R.string.overV);
            }
        } else {
            message += context.getString(R.string.good);
        }
        message += "\n";

        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        message += context.getString(R.string.temperature) + temperature;
        displayNotification(context, message, context.getString(R.string.batteryStatusTitle), BATTERY_STATUS_ID);

    }
    private void displayNotification(Context context, String message, String title, int id) {
        int icon = R.drawable.stat_sys_warning;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(icon);
        builder.setContentTitle(title);
        builder.setContentText(context.getString(R.string.batteryStatusLittleMessage));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        NotificationManager notificationManager;
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

}
