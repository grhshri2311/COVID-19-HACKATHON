package com.gprs.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class mystatus extends AppCompatActivity {

    RelativeLayout profile,viewmap,todo,notification;
    TextView confirm,death,mystatus,mystatus1,mystatus2;
    int total=0,sick=0;

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

        check();

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


    }

    void check() {
        final SharedPreferences pref;
        SharedPreferences.Editor editor;

        pref =getSharedPreferences("user", 0); //
        FirebaseDatabase.getInstance().getReference().child("Location").child(pref.getString("user","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserLocationHelper userLocationHelper1 = dataSnapshot.getValue(UserLocationHelper.class);
                if(userLocationHelper1!=null){

                    FirebaseDatabase.getInstance().getReference().child("Assess")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        final UserSelfAssessHelper userLocationHelper = snapshot.getValue(UserSelfAssessHelper.class);
                                            float[] results = new float[1];
                                            Location.distanceBetween(userLocationHelper.getLat(), userLocationHelper.getLon(),
                                                    userLocationHelper1.getLat(), userLocationHelper.getLon(), results);
                                            if (results[0] < 1000) {
                                                total = total + 1;
                                                if (userLocationHelper.getStatus() > 0) {
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
