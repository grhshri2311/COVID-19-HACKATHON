package com.gprs.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.internal.location.zzas;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 111;
    RelativeLayout mystatus,selfassess,dashmap,updates,case_report,helpline,donate;
    Toolbar toolbar;
    TextView confirm,death;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(home.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }

        SharedPreferences pref;
        SharedPreferences.Editor editor;

        startAlarm(false);
        startAlarm(true);
        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode

        if(pref.getString("user","").equals("")){
            startActivity(new Intent(home.this,login.class));
            finish();
        }

        int flag=0;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == 100) {
                flag=1;
            }
        }

        if(flag==0)
       new notificationHelper(this).createNotification("COVID19RELIEF","Stay Safe from COVID-19");

        donate=findViewById(R.id.donate);
        helpline=findViewById(R.id.helpline);
        mystatus=findViewById(R.id.mystatus);
        dashmap=findViewById(R.id.dashmap);

        selfassess=findViewById(R.id.selfassess);
        confirm=findViewById(R.id.confirm);
        death=findViewById(R.id.death);
        updates=findViewById(R.id.updates);

        APIextract apIextract=new APIextract(this,confirm,death);
        location();
        case_report=findViewById(R.id.case_report);
        setToolBar();







        dashmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,MapsActivity.class));
            }
        });

        case_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,cases_report.class));
            }
        });

        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,website.class));
            }
        });

        selfassess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,self_assess.class));
            }
        });


        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,helpline.class));
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,donate.class));
            }
        });
        mystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,mystatus.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logoutmenu){
            startAlarm(false);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(101);
            Exit();
        }
        if(id==R.id.translate){
           SharedPreferences pref;
            SharedPreferences.Editor editor;
            pref = getApplicationContext().getSharedPreferences("language", 0); // 0 - for private mode
            editor = pref.edit();
            if (pref.getString("lang", "").equals("")) {
                editor.putString("lang", "hi");
                editor.commit();
                setAppLocale("hi");
            } else {
                editor.putString("lang", "");
                editor.commit();
                setAppLocale("en");
            }
            startActivity(new Intent(home.this,home.class));
            finish();
        }
        if(id==R.id.share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I recommend Covid-19Relief app to fight against Covid-19.Please download and share it using this link Android:'%LINK%'");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
        return true;
    }

    private void setToolBar() {
        androidx.appcompat.widget.Toolbar tb =findViewById(R.id.hometoolbar);
        setSupportActionBar(tb);
    }

    public void Exit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences pref;
                SharedPreferences.Editor editor;

                pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
                editor = pref.edit();

                editor.putString("user","");
                editor.commit();
                startActivity(new Intent(home.this,login.class));
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Exit1();
    }

    public void Exit1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void startAlarm(boolean set) {

        notificationHelper notice = new notificationHelper(home.this);

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(home.this,AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);


        if(set){

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            30 * 1000, pendingIntent);

        }
        else
        if (manager!= null) {
            manager.cancel(pendingIntent);
        }

    }

    void location(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    if(task.getResult()!=null){
                        Location location=task.getResult();

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(home.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5



                        String city = addresses.get(0).getLocality();
                        String state=addresses.get(0).getAdminArea();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else{

                        Toast.makeText(home.this,"Please turn on GPS and accept location permission",Toast.LENGTH_LONG).show();
                    }
                }
                else{

                    Toast.makeText(home.this,"Please turn on GPS",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(home.this,"Permission granted",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(home.this,home.class));
                                 } else {
                    Toast.makeText(home.this,"Permission denied",Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
