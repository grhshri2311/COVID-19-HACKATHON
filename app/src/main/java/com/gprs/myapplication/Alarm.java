package com.gprs.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Alarm extends AppCompatActivity {


    FloatingActionButton floatingActionButton;
    ArrayList<String>name,desc,time;
    String extStorageDirectory = Environment.getExternalStorageDirectory()
            .toString();

    alarmAdapter alarmAdapter;
    File f=new File(extStorageDirectory + "/COVI19RELIEF/alarm/obj.dat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm2);

        if(!f.exists()){
            try {

                File folder1 = new File(extStorageDirectory, "COVI19RELIEF");// Name of the folder you want to keep your file in the local storage.
                folder1.mkdir(); //creating the folder
                File folder2 = new File(folder1, "alarm");// Name of the folder you want to keep your file in the local storage.
                folder2.mkdir(); //creating the folder
                File file = new File(folder2, "obj.dat");
                file.createNewFile(); // creating the file inside the folder
            }
            catch (Exception  e){

            }
        }


        name=new ArrayList<>();
        desc=new ArrayList<>();
        time=new ArrayList<>();

            loadMap();
        ListView listView =findViewById(R.id.alarmlist);

            alarmAdapter=new alarmAdapter(name,desc,time,this);
            listView.setAdapter(alarmAdapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    delete(position);
                    return true;
                }


            });

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addalarm();
            }
        });

    }


    void delete(final int pos){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Alarm");
        builder.setMessage("Do you want to delete alarm : "+name.get(pos)+" ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name.remove(pos);
                desc.remove(pos);
                time.remove(pos);
                saveMap();
                alarmAdapter.notifyDataSetChanged();
                    Toast.makeText(Alarm.this,"Alarm deleted",Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();



    }
    public String AlarmTime(TimePicker timePicker){

        Integer alarmHours = timePicker.getCurrentHour();
        Integer alarmMinutes = timePicker.getCurrentMinute();
        String stringAlarmMinutes;

        if (alarmMinutes<10){
            stringAlarmMinutes = "0";
            stringAlarmMinutes = stringAlarmMinutes.concat(alarmMinutes.toString());
        }else{
            stringAlarmMinutes = alarmMinutes.toString();
        }
        String stringAlarmTime;


        if(alarmHours>12){
            alarmHours = alarmHours-12;
            String stringAlarmHours="";
            if (alarmHours<10){
                 stringAlarmHours= "0";
                stringAlarmHours = stringAlarmHours.concat(alarmHours.toString());
            }
            else
                stringAlarmHours=alarmHours.toString();
            stringAlarmTime = stringAlarmHours.concat(":").concat(stringAlarmMinutes).concat(" PM");
        }else{
            String stringAlarmHours="";
            if (alarmHours<10){
                stringAlarmHours= "0";
                stringAlarmHours = stringAlarmHours.concat(alarmHours.toString());
            }
            else
                stringAlarmHours=alarmHours.toString();
            stringAlarmTime = stringAlarmHours.concat(":").concat(stringAlarmMinutes).concat(" AM");
        }
        return stringAlarmTime;
    }

    private void saveMap(){
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(f);
            ObjectOutputStream  objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(name);
            objectOutputStream.writeObject(desc);
            objectOutputStream.writeObject(time);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadMap(){
        try {
            FileInputStream fileInputStream=new FileInputStream(f);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            name=(ArrayList<String>)objectInputStream.readObject();
            desc=(ArrayList<String>)objectInputStream.readObject();
            time=(ArrayList<String>)objectInputStream.readObject();
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

    private void addalarm() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Set Alarm");


        LayoutInflater inflater=getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_item, null, true);


        final EditText name1,desc1;
        final TimePicker timePicker;
        name1=view.findViewById(R.id.alarmname);
        desc1=view.findViewById(R.id.alarmdesc);
        timePicker=view.findViewById(R.id.timePicker);

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!name1.getText().toString().isEmpty() && !desc1.getText().toString().isEmpty()){
                   name.add(name1.getText().toString());
                   desc.add(desc1.getText().toString());
                   time.add(AlarmTime(timePicker));
                    saveMap();
                    alarmAdapter.notifyDataSetChanged();
                    Toast.makeText(Alarm.this,"Alarm added",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(Alarm.this,"Please fill all the fields",Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setView(view);
        alert.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveMap();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMap();
    }
}