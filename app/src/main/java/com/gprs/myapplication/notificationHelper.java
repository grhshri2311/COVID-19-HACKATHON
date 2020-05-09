package com.gprs.myapplication;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class notificationHelper {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public notificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public void createOngoingNotification(String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(mContext , Splash.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.report);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(101 /* Request Code */, mBuilder.build());

    }

    public void createNotification(String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(mContext , Splash.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.report);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1011", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(101 /* Request Code */, mBuilder.build());

    }


    public void createNotification(String title, String message1, String message2) {
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(mContext , home.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.report);
        mBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(message1)
                        .addLine(message2))
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(android.R.drawable.btn_star);
        builder.setContentTitle("This is title of notification");
        builder.setContentText("This is a notification Text");
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));

        Intent intent = new Intent(mContext, foundvictim.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 113,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        builder.setFullScreenIntent(pendingIntent, true);


        NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(115, builder.build());

    }
}