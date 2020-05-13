package com.gprs.myapplication;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.media.VolumeProviderCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class alarmnotification extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10005";
    String message;
    Context context;
    Intent resultIntent;
    Ringtone ringtoneAlarm;
    int timer=0;
    String extStorageDirectory = Environment.getExternalStorageDirectory()
            .toString();
    File f = new File(extStorageDirectory + "/COVI19RELIEF/alarm/obj.dat");
    VolumeProviderCompat myVolumeProvider;


    ArrayList<String> name, desc, time;
    private MediaSessionCompat mediaSession;

    @Override
    public void onReceive(final Context context, Intent intent) {


        this.context = context;
        resultIntent = new Intent(context, notification.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(context, alarmTone);


        name = new ArrayList<>();
        desc = new ArrayList<>();
        time = new ArrayList<>();

        loadMap();

        mediaSession = new MediaSessionCompat(context, "PlayerService");
            mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                   MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                    .build());

        myVolumeProvider =
                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/100) {
                    @Override
                    public void onAdjustVolume(int direction) {
                        if(ringtoneAlarm.isPlaying())
                        ringtoneAlarm.stop();

                        mediaSession.setActive(false);

                    }
                };
        mediaSession.setPlaybackToRemote(myVolumeProvider);



        new CountDownTimer(3000, 3000)
        {
            public void onTick(long l) {}
            public void onFinish()
            {
                Log.println(Log.INFO,"Timer",String.valueOf(timer));
                if(timer==0) {

                    if(f.exists())

                    {
                        Log.println(Log.INFO,"Timer","enter");
                        loadMap();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        final String currentDateTime = dateFormat.format(new Date());
                        for (int i = 0; i < name.size(); i++) {
                            Log.println(Log.INFO,"array",time.get(i));
                            Log.println(Log.INFO,"cur",currentDateTime);
                            if (currentDateTime.equals(time.get(i))) {

                                Log.println(Log.INFO,"Timer","Matched");

                                mediaSession.setActive(true);

                                ringtoneAlarm.play();
                                notifyalarm(name.get(i));
                                timer=20;
                                Intent intent1 = new Intent(context, alarmremember.class);
                                intent1.putExtra("name", name.get(i));
                                intent1.putExtra("desc", desc.get(i));
                                context.startActivity(intent1);
                            }
                        }
                        start();
                    }
                    else
                        start();
                }
                else {
                    timer = timer - 1;
                    start();
                }
            }
        }.start();



    }

        private void loadMap () {
            try {
                FileInputStream fileInputStream = new FileInputStream(f);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                name = (ArrayList<String>) objectInputStream.readObject();
                desc = (ArrayList<String>) objectInputStream.readObject();
                time = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        void notifyalarm (String s){
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                    0 /* Request code */, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.drawable.report);
            mBuilder.setContentTitle("Alarm")
                    .setOnlyAlertOnce(true)
                    .setContentText(s)
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
            mNotificationManager.notify(11/* Request Code */, mBuilder.build());
        }

        void reset ( boolean set, int sec){



            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent;
            PendingIntent pendingIntent0 = null;

            // SET TIME HERE
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            myIntent = new Intent(context, alarmnotification.class);
            pendingIntent0 = PendingIntent.getBroadcast(context, 0, myIntent, 0);

            if (set) {

                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                sec * 1000, pendingIntent0);

            } else if (manager != null) {
                manager.cancel(pendingIntent0);
            }

        }


    }
