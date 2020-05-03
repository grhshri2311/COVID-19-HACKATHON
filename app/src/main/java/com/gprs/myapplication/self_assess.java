package com.gprs.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class self_assess extends AppCompatActivity {

    static ListView message_view;
    private static Integer[] togle;
    static ArrayList<String> question1=new ArrayList<String>();
    static ArrayList<String> answer1=new ArrayList<String>();
    static ArrayList<String> answer=new ArrayList<>();
    static ArrayList<ArrayList<String>> menu1=new ArrayList<ArrayList<String>>();
    static CustomAdapter customAdapter;
    static ArrayList<String> question=new ArrayList<>();
    static ArrayList<ArrayList<String>> menu=new ArrayList<>();
    static int count=0,pos=0;
    static UserLocationHelper userLocationHelper;

    static DatabaseReference reference;

    FirebaseDatabase database;

    void init(){
        answer=new ArrayList<>();
        String[] ques = getResources().getStringArray(R.array.self_assessq);
        question= new ArrayList<>(Arrays.asList(ques));
        menu=new ArrayList<>();
        answer=new ArrayList<>();
        String[] menus = getResources().getStringArray(R.array.self_assessm0);
        message_view=findViewById(R.id.messages_view);
        menu.add(new ArrayList<>(Arrays.asList(menus)));
        menus = getResources().getStringArray(R.array.self_assessm1);
        menu.add(new ArrayList<>(Arrays.asList(menus)));
        menus = getResources().getStringArray(R.array.self_assessm2);
        menu.add(new ArrayList<>(Arrays.asList(menus)));
        menus = getResources().getStringArray(R.array.self_assessm3);
        menu.add(new ArrayList<>(Arrays.asList(menus)));
        menus = getResources().getStringArray(R.array.self_assessm4);
        menu.add(new ArrayList<>(Arrays.asList(menus)));

        question1=new ArrayList<>();
        answer1=new ArrayList<>();
        menu1=new ArrayList<>();
        togle=new Integer[20];
        count=0;pos=0;
        if(pos<question.size()) {
            question1.add(question.get(pos));
            menu1.add(menu.get(pos));
            answer1.add("");
            togle[count++] = -1;
            question1.add(question.get(++pos));
            menu1.add(menu.get(pos));
            answer1.add("");
            togle[count++] = 1;

        }




        customAdapter=new CustomAdapter(this,menu1,togle,question1,answer1);
        message_view.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();

    }

    public static void answered(String itemAtPosition, Activity context) {
        if(!itemAtPosition.equals("")) {
            question1.add("");
            menu1.add(null);
            answer.add(itemAtPosition);
            answer1.add(itemAtPosition);
            togle[count++] = 2;
            customAdapter.notifyDataSetChanged();
        }
        if(pos+1<question.size()) {
            answer1.add("");
            question1.add(question.get(++pos));
            menu1.add(menu.get(pos));
                togle[count++]=1;
            customAdapter.notifyDataSetChanged();
            message_view.setSelection(pos);
        }
        else {
            next(context);
        }
    }

    private static void next(Activity context) {


        SharedPreferences pref;
        SharedPreferences.Editor editor;

        pref = context.getSharedPreferences("user", 0); // 0 - for private mode

        if(!pref.getString("user","").equals("")){
            save(context);
            question1.clear();
            question.clear();
            answer1.clear();
            menu.clear();
            menu1.clear();


            menu1.add(null);
            answer1.add("");
            togle[0]=11;
            question1.add("Result");
            customAdapter.notifyDataSetChanged();



        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assess);

        init();


        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Assess");
        message_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           }
        });

    }


    @Override
    public void onBackPressed() {
        Exit1();
    }

    public void Exit1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to quit Self-Assess?");
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
     static void save(final Context context){
        final SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref =context.getSharedPreferences("user", 0); // 0 - for private mode
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        FirebaseDatabase.getInstance().getReference().child("Location").child(pref.getString("user","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userLocationHelper = dataSnapshot.getValue(UserLocationHelper.class);
                if(userLocationHelper!=null){
                    UserSelfAssessHelper userSelfAssessHelper=new UserSelfAssessHelper(userLocationHelper.getLat(),userLocationHelper.getLon(), self_assess.answer,0);
                    FirebaseDatabase.getInstance().getReference().child("Assess").child(pref.getString("user","")).setValue(userSelfAssessHelper);
                    answer.clear();
                    Toast.makeText(context,"Tested Successfully",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context,"Tested locally \nCheck your internet connection ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
/*

   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#fff"
            android:orientation="horizontal"
            android:layout_margin="10dp">

          <EditText
                android:id="@+id/editText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="2"
                android:ems="10"
                android:hint="Write a message"
                android:inputType="text"
                android:paddingHorizontal="10dp"
                android:text="" />


            <ImageButton
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:scaleType="fitCenter"
                android:padding="20dp"
                android:layout_marginHorizontal="10dp"
                android:background="@drawable/ic_send_black_24dp"/>
        </LinearLayout>

 */