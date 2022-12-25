package com.Sadraafzar.Messenger;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.Sadraafzar.Messenger.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class FontOverride extends Application {
    public static String NOTIFICATION_CHANNEL_ID = "sadra_afzar";
    public void onCreate()
    {
        super.onCreate();
        creatnotificationchanel();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()

                .setDefaultFontPath("IRANSans(FaNum).ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    private void creatnotificationchanel() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }


    }
}