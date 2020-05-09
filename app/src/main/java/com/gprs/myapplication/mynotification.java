package com.gprs.myapplication;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class mynotification extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10003";
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message;
    Context context;
    Intent resultIntent;


    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context=context;
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        resultIntent = new Intent(context , notification.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();


        FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    String str = "0";
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        str=str+data.getKey()+'\n';
                    }
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                            0 /* Request code */, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder = new NotificationCompat.Builder(context);
                    mBuilder.setSmallIcon(R.drawable.report);
                    mBuilder.setContentTitle("You got  new notification")
                            .setOnlyAlertOnce(true)
                            .setAutoCancel(false)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentIntent(resultPendingIntent);

                    mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


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

                    if(str.length()>1) {
                        assert mNotificationManager != null;
                        mNotificationManager.notify(33, mBuilder.build());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(context,mynotification.class);
        pendingIntent = PendingIntent.getBroadcast(context,0,myIntent,0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            3 * 1000, pendingIntent);


    }
}