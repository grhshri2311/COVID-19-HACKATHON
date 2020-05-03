package com.gprs.myapplication;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class AlarmNotificationReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10002";
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message;
    Context context;
    Intent resultIntent;
    private RequestQueue queue;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        resultIntent = new Intent(context , Splash.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        queue = Volley.newRequestQueue(context);
        new AlarmNotificationReceiver.RetrieveFeedTask1().execute();

    }

    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("https://api.covid19india.org/data.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

            }
            return null;
        }

        protected void onPostExecute(String response) {



            Log.i("INFO", response);
            try{
                SharedPreferences pref;
                SharedPreferences.Editor editor;

                pref = context.getSharedPreferences("update", 0); // 0 - for private mode
                editor = pref.edit();
                editor.putBoolean("alarm",true);
                editor.commit();
                final FirebaseDatabase database=FirebaseDatabase.getInstance();
                final DatabaseReference reference=database.getReference("Users").child(pref.getString("user",""));
            JSONObject jsonObject = null;

            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonArray=object.getJSONArray("statewise");
            for(int i=0;i<jsonArray.length();i++){
                jsonObject=jsonArray.getJSONObject(i);
                if(jsonObject.getString("state").equals("Haryana"))
                break;

            }

            int newactive=Integer.valueOf(jsonObject.getString("confirmed"))-pref.getInt("last",0);
                int newdeath=Integer.valueOf(jsonObject.getString("deaths"))-pref.getInt("last1",0);
                int newrecover=Integer.valueOf(jsonObject.getString("recovered"))-pref.getInt("last2",0);

                editor.putInt("last",Integer.valueOf(jsonObject.getString("confirmed")));
                editor.putInt("last1",Integer.valueOf(jsonObject.getString("deaths")));
                editor.putInt("last2",Integer.valueOf(jsonObject.getString("recovered")));
                editor.commit();
            message="India\n Total cases : "+jsonArray.getJSONObject(0).getString("confirmed")+"\n Total recovered : "+jsonArray.getJSONObject(0).getString("recovered")+"\n Total death : "+jsonArray.getJSONObject(0).getString("deaths");
            message=message+"\nHaryana \n New cases : "+newactive+"\n New Recovered "+newrecover+"\n New Death : "+newdeath;
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                        0 /* Request code */, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.report);
                mBuilder.setContentTitle("In your location")
                        .setContentText("Updates of past  6 hours")
                        .setAutoCancel(false)
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Latest Updates ").bigText(message))
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
                assert mNotificationManager != null;
                mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
                pref = context.getSharedPreferences("user", 0);
                if(!pref.getString("user","").equals("")) {
                    notificationHelper notice = new notificationHelper(context);

                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent myIntent;
                    PendingIntent pendingIntent = null;

                    // SET TIME HERE
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    myIntent = new Intent(context, AlarmNotificationReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);


                    manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() +
                                    60*2 * 1000, pendingIntent);

                }

            } catch (Exception e) {
                e.printStackTrace();


            }

        }
    }
}