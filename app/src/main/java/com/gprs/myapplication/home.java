package com.gprs.myapplication;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 111;
    RelativeLayout mystatus,selfassess,dashmap,updates,case_report,helpline,donate,scan,medstore,epass,admission,quora;
    Toolbar toolbar;
    TextView confirm,death;

    private FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        BroadcastReceiver br = new MyBroadcastReciever();





        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        getApplicationContext().registerReceiver(br, filter);
        sendBroadcast(getIntent());


        final SharedPreferences pref;
        final SharedPreferences.Editor editor;

        getdetails();

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startAlarm(false);
            startAlarm(true);
        }




        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor=pref.edit();

        if(pref.getString("user","").equals("")){
            startActivity(new Intent(home.this,login.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            finish();
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(home.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }

        int flag=0;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == 100) {
                flag=1;
            }
        }

        if(flag==0)
       new notificationHelper(this).createOngoingNotification("COVID19RELIEF","Stay Safe from COVID-19");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDateTime = dateFormat.format(new Date()); // Find todays date

        if(!pref.getString("today","").equals(currentDateTime)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    editor.putString("today",currentDateTime);
                    editor.commit();
                    Intent i = new Intent(home.this, stepstofollow.class);
                    startActivity(i);
                }
            }, 10000);

        }

        quora=findViewById(R.id.quora);
        donate=findViewById(R.id.donate);
        helpline=findViewById(R.id.helpline);
        mystatus=findViewById(R.id.mystatus);
        scan=findViewById(R.id.scan);;
        dashmap=findViewById(R.id.dashmap);
        epass=findViewById(R.id.epass);

        selfassess=findViewById(R.id.selfassess);
        confirm=findViewById(R.id.confirm);
        death=findViewById(R.id.death);
        updates=findViewById(R.id.updates);
        medstore=findViewById(R.id.medstore);
        admission=findViewById(R.id.admission);


            APIextract apIextract = new APIextract(this, confirm, death);



        case_report=findViewById(R.id.case_report);
        setToolBar();





        quora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,quora.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        epass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,epass.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        admission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,hospital.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        medstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,Medstore.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,victimalert.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        dashmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,MapsActivity.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        case_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,cases_report.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,news.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        selfassess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,self_assess.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });


        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,helpline.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,donate.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
      mystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView cur=findViewById(R.id.textView);
                TextView confirm=findViewById(R.id.confirm);
                TextView death=findViewById(R.id.death);
                ImageView icon=findViewById(R.id.imageView);
                LinearLayout l1=findViewById(R.id.linearLayout0);
                LinearLayout l2=findViewById(R.id.linearLayout);
                LinearLayout l3=findViewById(R.id.linearLayout2);

                mystatus.setBackground(getDrawable(R.drawable.customborder_home));

                Intent intent=new Intent(home.this,mystatus.class);
                Pair[]pairs=new Pair[7];
                pairs[0]=new Pair<View, String>(icon,"icon");
                pairs[1]=new Pair<View, String>(cur,"currrentstatus");
                pairs[2]=new Pair<View, String>(confirm,"active");
                pairs[3]=new Pair<View, String>(death,"death");
                pairs[4]=new Pair<View, String>(l1,"linear1");
                pairs[5]=new Pair<View, String>(l2,"linear2");
                pairs[6]=new Pair<View, String>(l3,"linear3");

                //wrap the call in API level 21 or higher
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(home.this,pairs);
                    startActivity(intent,options.toBundle());
                }
                    }

        });

    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        final SharedPreferences pref;
        final SharedPreferences.Editor editor;

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Long count1=dataSnapshot.getChildrenCount();
                       if(count1>=1) {
                           menu.getItem(0).setIcon(R.drawable.ic_notifications_red_24dp);
                           mystatus.setBackground(getDrawable(R.drawable.customborder));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        if (id==R.id.notify){
            item.setIcon(R.drawable.ic_notifications_black_24dp);
            startActivity(new Intent(home.this,notification.class));
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

                editor.clear();
                editor.apply();
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

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent0 = null;

        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(this,alarmnotification.class);
        pendingIntent0 = PendingIntent.getBroadcast(this,0,myIntent,0);

        if(set){

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent0);

        }
        else
        if (manager!= null) {
            manager.cancel(pendingIntent0);
        }


        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        calendar = Calendar.getInstance();
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

        myIntent = new Intent(home.this,mynotification.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);


        if(set){

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent1);

        }
        else {
            if (manager != null) {
                manager.cancel((pendingIntent1));
            }
        }

            myIntent = new Intent(home.this,helpneeded.class);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);


            if(set){

                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                1 * 1000, pendingIntent2);

            }
            else {
                if (manager != null) {
                    manager.cancel((pendingIntent2));
                }

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

    void  getdetails(){
        final SharedPreferences pref;
        final SharedPreferences.Editor editor;

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();



        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user","")).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer status=dataSnapshot.getValue(Integer.class);
                if(status!=null && status==1) {
                    editor.putString("status", "victim");
                    editor.commit();
                }
                else {
                    editor.putString("status", "normal");
                    editor.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRegistrationHelper userRegistrationHelper=dataSnapshot.getValue(UserRegistrationHelper.class);
                if(userRegistrationHelper!=null) {
                    editor.putString("role", userRegistrationHelper.getRole());
                    editor.commit();
                }
                else {
                    editor.putString("role", "not defined");
                    editor.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void alarm(View view) {
        startActivity(new Intent(this,Alarm.class));
    }

    public void firstresponder(View view) {
        startActivity(new Intent(home.this,firstresponder.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }
}
