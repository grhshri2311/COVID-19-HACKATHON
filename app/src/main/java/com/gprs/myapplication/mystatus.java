package com.gprs.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class mystatus extends AppCompatActivity {

    RelativeLayout profile,viewmap,todo,notification;
    TextView confirm,death,mystatus,mystatus1,mystatus2,todotext;
    ImageView todoimage,profileimg;
    int total=0,sick=0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    UserRegistrationHelper userRegistrationHelper;
    boolean flag=false;
    ViewPager viewPager;
    Adapterviewer adapter;
    List<Modelviewer> models;
    LinearLayout l1,l2;

    CountDownTimer c1,c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystatus);
        profile=findViewById(R.id.dashprofile);
        notification=findViewById(R.id.notification);
        todo=findViewById(R.id.todo);
        viewmap=findViewById(R.id.viewmap);
        mystatus=findViewById(R.id.mystatus);
        mystatus1=findViewById(R.id.mystatus1);
        mystatus2=findViewById(R.id.mystatus2);
        profileimg=findViewById(R.id.airplane);
        pref =getSharedPreferences("user", 0); //
        editor=pref.edit();

        todoimage=findViewById(R.id.todoimage);
        todotext=findViewById(R.id.todotext);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDateTime = dateFormat.format(new Date()); // Find todays date


        if(!PreferenceManager.getDefaultSharedPreferences(this).getString("today1","").equals(currentDateTime)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceManager.getDefaultSharedPreferences(mystatus.this).edit().putString("today1",currentDateTime).apply();
                    Intent i = new Intent(mystatus.this, advice.class);
                    startActivity(i);
                }
            }, 10000);
        }

        check();

        models = new ArrayList<>();
        models.add(new Modelviewer("https://ohsonline.com/Issues/2016/04/-/media/OHS/OHS/Images/2016/03/shutterstock_217127524.jpg", "Step 1", ""));
        models.add(new Modelviewer("https://img.lovepik.com/element/40162/9102.png_300.png", "Step 2", ""));
        models.add(new Modelviewer("https://lh3.googleusercontent.com/proxy/TMP_gvA43b55TUAUgJfuSy9ADD7opGCpzs7HeoLp6QrdolfMmy3BfN1g4_2Vy5h4j-7tvx5Viw80tawRZUlTwclaF2uPTZI", "Step 3", ""));
        models.add(new Modelviewer("https://previews.123rf.com/images/vostal/vostal1701/vostal170100066/70729733-hand-drawing-of-a-red-and-white-ambulance.jpg", "Step 4", ""));


        adapter = new Adapterviewer(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(10, 0, 50, 0);

        l1=findViewById(R.id.linearLayout);
        l2=findViewById(R.id.linearLayout0);


        c1=new CountDownTimer(9000, 3000)
        {
            public void onTick(long l) {
                l2.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%models.size());
            }
            public void onFinish()
            {
                l2.setVisibility(View.INVISIBLE);
                c2.start();
            }
        };

        c2=new CountDownTimer(5000, 1000)
        {
            public void onTick(long l) {
                l1.setVisibility(View.VISIBLE);
            }
            public void onFinish()
            {
                l1.setVisibility(View.INVISIBLE);
                c1.start();
            }
        };

        c2.start();

        FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Long count1=dataSnapshot.getChildrenCount();
                    if(count1>=1) {
                        notification.setBackground(getDrawable(R.drawable.customborder));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        confirm=findViewById(R.id.confirm);
        death=findViewById(R.id.death);

        APIextract apIextract=new APIextract(this,confirm,death);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mystatus.this,profile.class);
                Pair[]pairs=new Pair[1];
                pairs[0]=new Pair<View, String>(profileimg,"profile");


                //wrap the call in API level 21 or higher
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(mystatus.this,pairs);
                    startActivity(intent,options.toBundle());
                }

            }
        });
        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mystatus.this,MapsActivity.class));
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout= findViewById(R.id.swipe1);
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


        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mystatus.this,your_work.class));

            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mystatus.this,notification.class));
                notification.setBackground(null);
            }
        });
    }

    void check() {

        FirebaseDatabase.getInstance().getReference().child("Location").child(pref.getString("user","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserLocationHelper userLocationHelper1 = dataSnapshot.getValue(UserLocationHelper.class);
                if(userLocationHelper1!=null){

                    FirebaseDatabase.getInstance().getReference().child("Assess")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if(snapshot.getKey().equals(pref.getString("user",""))){
                                            continue;
                                        }
                                        final UserSelfAssessHelper userLocationHelper = snapshot.getValue(UserSelfAssessHelper.class);
                                            float[] results = new float[1];

                                            Location.distanceBetween(userLocationHelper.getLat(), userLocationHelper.getLon(),
                                                    userLocationHelper1.getLat(), userLocationHelper.getLon(), results);
                                            if (results[0] < 1000) {
                                                total = total + 1;
                                                if (userLocationHelper.getStatus() >=3) {
                                                    sick = sick + 1;
                                                }
                                            }


                                    }
                                    if(sick>0){
                                        mystatus.setText("You have to be Safe !\n");
                                        mystatus.setTextColor(getResources().getColor(R.color.RED));
                                        mystatus1.setText("Total assessed : "+total);
                                        mystatus2.setText("Found Sick : "+sick+"\nWithin 1 KM radius");

                                    }
                                    else {
                                        mystatus.setText("You are Safe !\n");
                                        mystatus.setTextColor(getResources().getColor(R.color.GREEN));
                                        mystatus1.setText("Total assessed : "+total);
                                        mystatus2.setText("Found Sick : "+sick+"\nWithin 1 KM radius");
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                }
                else {
                          }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
