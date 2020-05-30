package com.gprs.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notification extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<String> arrayList=new ArrayList<>();
    ListView notices;
    ArrayAdapter adapter;
    TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        notice=findViewById(R.id.notice);
        notices=findViewById(R.id.notices);
        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        if(pref.getString("user","").equals("")){
            startActivity(new Intent(notification.this,logouthome.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }

        adapter = new ArrayAdapter<String>(notification.this,R.layout.notice,R.id.notice1,arrayList);
        if(pref.getString("user","").equals("")){
            startActivity(new Intent(notification.this,login.class));
            finish();
        }

        notices.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    String str = "0";
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        notice.setVisibility(View.INVISIBLE);
                        for(DataSnapshot dat:data.getChildren())
                        arrayList.add(dat.getValue(String.class)+'\n'+dat.getKey());
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user","")).removeValue();
    }
}
