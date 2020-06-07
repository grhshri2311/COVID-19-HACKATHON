package com.gprs.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class helpline extends AppCompatActivity {


    TextView help1, help2, help3, help4, help5, help6, help7, help;
    Spinner help8;
    HashMap<String, String> district;
    ArrayList<String> spin;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(helpline.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }

        help = findViewById(R.id.help);
        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);
        help5 = findViewById(R.id.help5);
        help6 = findViewById(R.id.help6);
        help7 = findViewById(R.id.help7);
        help8 = findViewById(R.id.help8);

        district = new HashMap<>();


        spin = new ArrayList<>();
        spin.add("Select District");
        district.put("Ambala", "9729179275");
        district.put("Bhiwani", "7027847102");
        district.put("Charkhi Dadri", "01250222555");
        district.put("Faridabad", "01292221000");
        district.put("Fatehabad", "01667230018");
        district.put("Gurugram", "9953618102");
        district.put("Hisar", "7027830252");
        district.put("Jhajjar", "01251253118");
        district.put("Jind", "01681245206");
        district.put("Kaithal", "9896317010");
        district.put("Karnal", "9817701572");
        district.put("Kurukshetra", "7027854102");
        district.put("Mahendergarh", "9306754767");
        district.put("Mewat/Nuh", "7027855102");
        district.put("Palwal", "01275240022");
        district.put("Panchkula", "01722573901");
        district.put("Panipat", "01802640255");
        district.put("Rewari", "01274225145");
        district.put("Rohtak", "7027816559");
        district.put("Sirsa", "01666248882");
        district.put("Sonipat", "01302231932");
        district.put("Yamunanagar", "7027972089");


        ArrayList temp = new ArrayList<String>(district.keySet());
        Collections.sort(temp);
        spin.addAll(temp);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(helpline.this, R.layout.simple_spinner_item, R.id.txt_bundle, spin);


        help8.setAdapter(dataAdapter);
        help8.setGravity(11);

        int initialposition = help8.getSelectedItemPosition();
        help8.setSelection(initialposition, false);
        help8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + district.get(parent.getItemAtPosition(position))));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8558893911"));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();

            }
        });
        help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "11-23978046"));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();

            }
        });

        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18001802128"));
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "108"));
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        help4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "100"));
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        help5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1091"));
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        help6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1098"));
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        help7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "101"));
                Toast.makeText(helpline.this, "Connecting to Helpline ...", Toast.LENGTH_LONG).show();
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(helpline.this,"Permission granted",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(helpline.this,helpline.class));
                    finish();
                } else {
                    Toast.makeText(helpline.this,"Permission denied",Toast.LENGTH_LONG).show();
                   finish();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
