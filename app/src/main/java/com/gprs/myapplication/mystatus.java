package com.gprs.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class mystatus extends AppCompatActivity {

    RelativeLayout profile,viewmap,todo,notification;
    TextView confirm,death,mystatus,mystatus1,mystatus2,todotext;
    ImageView todoimage;
    int total=0,sick=0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    UserRegistrationHelper userRegistrationHelper;
    boolean flag=false;
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

        pref =getSharedPreferences("user", 0); //
        todoimage=findViewById(R.id.todoimage);
        todotext=findViewById(R.id.todotext);

        setview();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(mystatus.this,advice.class);
                startActivity(i);
            }
        }, 5000);

        check();



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
                startActivity(new Intent(mystatus.this,profile.class));
            }
        });
        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mystatus.this,MapsActivity.class));
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                startActivity(new Intent(mystatus.this,mystatus.class));
                swipeRefreshLayout.setRefreshing(false);
                finish();
            }
        });


        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    if(userRegistrationHelper.getRole().equals("Monitors")){
                        startActivity(new Intent(mystatus.this,assign_work.class));
                    }
                    else {
                        startActivity(new Intent(mystatus.this,your_work.class));
                    }
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mystatus.this,notification.class));
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

    void setview(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    userRegistrationHelper=dataSnapshot.getValue(UserRegistrationHelper.class);
                    flag=true;
                    if(userRegistrationHelper.getRole().equals("Monitors")){
                        todoimage.setImageDrawable(getDrawable(R.drawable.assign));
                        todotext.setText("Assign Work");
                    }
                    else {
                        todoimage.setImageDrawable(getDrawable(R.drawable.todo));
                        todotext.setText("Todo List");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
