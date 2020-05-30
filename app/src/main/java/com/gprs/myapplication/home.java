package com.gprs.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
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

import static com.gprs.myapplication.YourLocationBroadcastReciever.NOTIFICATION_CHANNEL_ID;

public class home extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 111;
    private static final int REQUEST_INVITE = 10115;
    RelativeLayout mystatus,selfassess,dashmap,updates,case_report,helpline,donate,scan,medstore,epass,admission,quora;
    Toolbar toolbar;
    TextView confirm,death;
    BroadcastReceiver br;
    private FusedLocationProviderClient fusedLocationClient;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         br = new InternetBroadcastReciever();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(br, filter);
        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor=pref.edit();



        getdetails();

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startAlarm(false);
            startAlarm(true);
        }




        final SwipeRefreshLayout swipeRefreshLayout= findViewById(R.id.swipe);
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



        if(pref.getString("user","").equals("")){
            startActivity(new Intent(home.this,logouthome.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            finish();
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(home.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
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
            }, 20000);

        }

        if(!pref.getString("todaychatintro","").equals(currentDateTime)) {
          chatbotintro();
            editor.putString("todaychatintro",currentDateTime);
            editor.commit();
        }



        quora=findViewById(R.id.quora);
        donate=findViewById(R.id.donate);
        helpline=findViewById(R.id.helpline);
        mystatus=findViewById(R.id.mystatus);
        scan=findViewById(R.id.scan);
        dashmap=findViewById(R.id.dashmap);
        epass=findViewById(R.id.epass);

        selfassess=findViewById(R.id.selfassess);
        confirm=findViewById(R.id.confirm);
        death=findViewById(R.id.death);
        updates=findViewById(R.id.updates);
        medstore=findViewById(R.id.medstore);
        admission=findViewById(R.id.admission);


        TextView scrolltextview = findViewById(R.id.scrollingtextview);
        scrolltextview.setSelected(true);


            APIextract apIextract = new APIextract(this, confirm, death);



        case_report=findViewById(R.id.case_report);
        setToolBar();


        ImageView i1,i2,i3,i4,i5;
        i1=findViewById(R.id.implink1);
        i2=findViewById(R.id.implink2);
        i3=findViewById(R.id.implink3);
        i4=findViewById(R.id.implink4);
        i5=findViewById(R.id.implink5);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://www.mohfw.gov.in/");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://www.nhp.gov.in/disease/communicable-disease/novel-coronavirus-2019-ncov");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://www.mohfw.gov.in/");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://www.icmr.gov.in/");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://nhm.gov.in/");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        ImageView shelter;
        LinearLayout faq,faq1;
        shelter=findViewById(R.id.shelter);
        faq=findViewById(R.id.faq);
        faq1=findViewById(R.id.faq1);


        shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, pdfViewer.class);
                intent.putExtra("text","https://drive.google.com/file/d/1mWap_8QEc3HUAiIpPM65K2rZiPjudMO1/view");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://drive.google.com/file/d/1GPoaMCIwbUdd3XDCzHnY_HiP7p2dVJ4x/view?usp=sharing");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });

        faq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this,pdfViewer.class);
                intent.putExtra("text","https://drive.google.com/file/d/1A0mY4oMMoSMY5IeuhtKTWVvTkkdOy7lf/view?usp=sharing");
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
            }
        });
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
                if(!pref.getString("status","").equals("victim"))
                startActivity(new Intent(home.this,victimalert.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
                else {
                    Toast.makeText(home.this,"You are found victim \nYou can't use this festure!",Toast.LENGTH_LONG).show();
                }
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

        FirebaseDatabase.getInstance().getReference().child("Location").child(pref.getString("user","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    UserLocationHelper u = dataSnapshot.getValue(UserLocationHelper.class);

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(home.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(u.getLat(), u.getLon(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5



                        String city = addresses.get(0).getLocality();
                        String state=addresses.get(0).getAdminArea();

                        editor.putString("city",city);
                        editor.putString("state",state);
                        editor.commit();



                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!isMyServiceRunning(AlarmForegroundNotification.class)) {
            startService(AlarmForegroundNotification.class);
        }

        if(!isMyServiceRunning(HelpneededBroadcastReceiver.class)) {
            startService(HelpneededBroadcastReceiver.class);
        }

        sendBroadcast(new Intent(this, Restarter.class).setAction("Help"));


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
                           menu.getItem(1).setIcon(R.drawable.ic_notifications_red_24dp);
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
            mNotificationManager.cancel(33);
            Exit();
        }

        if(id==R.id.chatbot){
            startActivity(new Intent(home.this,Chatbot.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

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
            final String appPackageName = BuildConfig.APPLICATION_ID;
            final String appName = getString(R.string.app_name);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store/apps/details?id=" +
                    appPackageName;
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string
                    .share_with)));
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
                if(isMyServiceRunning(VictimAlertForegroundNotification.class))
                stopService(VictimAlertForegroundNotification.class);
                startActivity(new Intent(home.this,logouthome.class));
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
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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


        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());




        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(home.this, YourLocationBroadcastReciever.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);


        if(set){

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            1 * 1000, pendingIntent);

        }
        else
        if (manager!= null) {
            manager.cancel(pendingIntent);
        }

        myIntent = new Intent(home.this, MyNotificationBroadcastReceiver.class);
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
        myIntent = new Intent(home.this, HelpneededBroadcastReceiver.class);
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
                    if(!isMyServiceRunning(VictimAlertForegroundNotification.class)) {
                        startService(VictimAlertForegroundNotification.class);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(br);
        }
        catch (Exception e){

        }
    }

    public void chatbot(View view) {
        startActivity(new Intent(home.this,Chatbot.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }

    public void whatsapp(View view) {
        try {
            whatsapp(this, "919013151515");
        }
        catch (IllegalStateException e){
            Toast.makeText(this,"You have no whatsapp",Toast.LENGTH_LONG).show();
        }
    }

    public static void whatsapp(Activity activity, String phone) {
        String formattedNumber = (phone);
        try{
            Intent sendIntent =new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Press 1 for latest updates");
            sendIntent.putExtra("jid", formattedNumber +"@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"You don't have Whatsapp"+ e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void chatbotintro() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();


        LayoutInflater inflater=getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_chatbotintro, null, true);


        Button button = view.findViewById(R.id.btn_get_started);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               alert.hide();

           }
       });

        alert.setView(view);
        alert.show();

    }

    public void startService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(this, serviceClass);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(this, serviceClass);
        stopService(serviceIntent);

    }

    public void assign(View view) {
            startActivity(new Intent(home.this,assign_work.class));

    }

    public void labs(View view) {
        startActivity(new Intent(home.this,labsfortest.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }

    public void msme(View view) {
        startActivity(new Intent(home.this,MSME.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

    }

    public void hosnear(View view) {
        Intent intent=new Intent(this,Medicalshops.class);
        intent.putExtra("text","Hospitals");
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void onlinecourse(View view) {
        startActivity(new Intent(home.this,course.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());
    }

    public void sectorworkers(View view) {
        startActivity(new Intent(home.this,unorganizedsectors.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

    }

    public void publiccare(View view) {
        startActivity(new Intent(home.this,publichealthcare.class), ActivityOptions.makeSceneTransitionAnimation(home.this).toBundle());

    }
}
