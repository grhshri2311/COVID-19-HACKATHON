package com.gprs.myapplication;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class register extends AppCompatActivity{

    AlertDialog alert;
    ImageView icon;
    TextView signin,welcome;
    Button login;
    Button go;
    static Button role;
    TextInputLayout email;
    TextInputLayout password;
    TextInputLayout mobile;
    TextInputLayout name;
    static TextInputLayout roleL;
    DatabaseReference reference;
    FirebaseDatabase database;
    String email1,password1,phone1,name1,verificationCodeBySystem;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationClient;
    Location location;
    boolean em=true,ph=true,loc=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(register.this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        role = findViewById(R.id.role);
        mobile = findViewById(R.id.mobile);
        name = findViewById(R.id.fname);
        login = findViewById(R.id.login);
        icon = findViewById(R.id.icon1);
        signin = findViewById(R.id.signin1);
        welcome = findViewById(R.id.welcome1);
        go = findViewById(R.id.go1);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        roleL = findViewById(R.id.roleLayout);

        if (ContextCompat.checkSelfPermission(register.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(register.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(icon, "logo_image");
                pairs[1] = new Pair<View, String>(welcome, "logo_text");
                pairs[2] = new Pair<View, String>(signin, "logo_text1");
                pairs[3] = new Pair<View, String>(email, "email");
                pairs[4] = new Pair<View, String>(password, "pass");
                pairs[5] = new Pair<View, String>(go, "go");
                pairs[6] = new Pair<View, String>(login, "switch");

                //wrap the call in API level 21 or higher
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(register.this, pairs);
                    startActivity(intent, options.toBundle());
                }
                finish();
            }
        });

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu rolemenu = new PopupMenu(getApplicationContext(), role);
                rolemenu.getMenuInflater().inflate(R.menu.role, rolemenu.getMenu());
                rolemenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        role.setText(item.getTitle().toString());
                        return true;
                    }
                });
                rolemenu.show();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate(name, email, mobile, password)) {
                    name1 = name.getEditText().getText().toString();
                    email1 = email.getEditText().getText().toString();
                    phone1 = mobile.getEditText().getText().toString();
                    password1 = password.getEditText().getText().toString();
                    progressDialog.show();
                    checkUser();
                }
            }
        });

    }
    private void validatemobile() {

        ph=false;

        verifyemail();
      /*
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91 "+phone1,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);


        verifymobile();

       */
    }

    private void verifymobile() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Verify your Mobile number ");

        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(p);
        p.setMargins(100, 100, 70, 0);
        input.setHint("Enter OTP");
        lila1.addView(input);


            builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                        if(input.getText().toString().equals("")){
                            Toast.makeText(register.this,"Enter valid OTP",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            verifymobile();

                        }
                        else {
                            progressDialog.show();
                            dialog.dismiss();
                            verifycode(input.getText().toString());
                        }
                }
            });
            builder.setNegativeButton("Change number", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.hide();
                   dialog.dismiss();
                }
            });
        alert = builder.create();
        alert.setView(lila1);
        alert.show();

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

         /*   String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifycode(code);
            }

          */
        progressDialog.hide();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(register.this,"Authentication failed"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        signinwithcredentials(credential);
    }

    private void signinwithcredentials(final PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                  ph=false;

                  verifyemail();

                }
                else
                    Toast.makeText(register.this,"Incorrect OTP",Toast.LENGTH_LONG).show();
                verifymobile();
            }
        });
    }

    public static boolean validate(TextInputLayout name1, TextInputLayout email1, final TextInputLayout phone1, TextInputLayout password1){
        String emailPatter="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPatter= "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        if(role.getText().toString().equals("Select your Role")){
            roleL.setError("\nRole cannot be empty\n");
            return false;
        }
        else {
            roleL.setError("");
            roleL.setErrorEnabled(false);
        }

        if(name1.getEditText().getText().toString().isEmpty()) {
            name1.setError("\nName cannot be empty\n");return false;
        }
        else{
            name1.setError("");
            name1.setErrorEnabled(false);
        }


        if(!email1.getEditText().getText().toString().matches(emailPatter) && !email1.getEditText().getText().toString().isEmpty()){
            email1.setError("\nEnter a valid email\n");return false;
        }
        else{
            email1.setError("");
            email1.setErrorEnabled(false);
        }

        if(password1.getEditText().getText().toString().isEmpty()) {
            password1.setError("\nPassword cannot be empty\n");return false;
        }
        else  if(!password1.getEditText().getText().toString().matches(passwordPatter)){
            password1.setError("\nPassword Must contain atleast\nOne Uppercase ,\nOne Lowercase ,\nOne Number ,\nOne Special character and \nBetween 8 to 16 letter length\n");
            return false;
        }
        else{
            password1.setError("");
            password1.setErrorEnabled(false);
        }
        if(phone1.getEditText().getText().toString().isEmpty()) {
            phone1.setError("\nMobile cannot be empty\n");return false;
        }
        else{
            phone1.setError("");
            phone1.setErrorEnabled(false);
        }


            return true;
    }


    void checkUser(){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users").child(phone1);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserRegistrationHelper helper = dataSnapshot.getValue(UserRegistrationHelper.class);
                if (helper != null) {
                    progressDialog.hide();
                    mobile.setError("Mobile number aldready exists");

                }
                else {
                    validatemobile();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.hide();
                Toast.makeText(register.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    void setcount(final String role){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference[] reference1 = {database.getReference("Count").child(role)};
        final Integer[] count = new Integer[1];

        reference1[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                count[0] = dataSnapshot.getValue(Integer.class);
                if (count[0] != null) {
                    reference1[0] =database.getReference("Count");

                    reference1[0].child(role).setValue(count[0]+1);
                }
                else {
                    reference1[0] =database.getReference("Count");

                    reference1[0].child(role).setValue(1);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.hide();
                Toast.makeText(register.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        final DatabaseReference[] reference = {database.getReference("Count").child("total")};

        reference[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                count[0] = dataSnapshot.getValue(Integer.class);
                if (count[0] != null) {
                    reference[0] =database.getReference("Count");

                    reference[0].child("total").setValue(count[0]+1);
                }
                else {
                    reference[0] =database.getReference("Count");

                    reference[0].child("total").setValue(1);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.hide();
                Toast.makeText(register.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    void verifyemail() {

        if (ContextCompat.checkSelfPermission(register.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(register.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(register.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(register.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else if(loc){

            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        if(task.getResult()!=null){
                            loc=false;
                            location=task.getResult();
                            verifyemail();
                        }
                        else{
                            progressDialog.hide();
                            Toast.makeText(register.this,"Please turn on GPS and accept location permission",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        progressDialog.hide();
                        Toast.makeText(register.this,"Please turn on GPS",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if(ph) {
            checkUser();
        }

        else {
            try {
                progressDialog.hide();
                setcount(role.getText().toString());
                UserRegistrationHelper helper=new UserRegistrationHelper(name1,email1,location.getLatitude(),location.getLongitude(),phone1,role.getText().toString(),encrypt(password1));
                database=FirebaseDatabase.getInstance();


                reference=database.getReference("Users");

                reference.child(phone1).setValue(helper);
                if(role.getText().toString().equals("Victim"))
                reference.child(phone1).child("status").setValue(1);
                UserLocationHelper userLocationHelper=new UserLocationHelper(name1,location.getLatitude(),location.getLongitude(),role.getText().toString(),email1,phone1);
                database.getReference("Location").child(phone1).setValue(userLocationHelper);

                progressDialog.hide();

                SharedPreferences pref;
                SharedPreferences.Editor editor;

                pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
                editor = pref.edit();

                editor.putString("user",phone1);
                editor.commit();

                startActivity(new Intent(register.this,test.class),ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                finish();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
    {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(register.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(register.this,login.class));
                    finish();

                }
                return;
            }
        }
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

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        return key;
    }
}
