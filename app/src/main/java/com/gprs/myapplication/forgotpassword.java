package com.gprs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class forgotpassword extends AppCompatActivity {
    TextInputLayout pass;
    TextInputLayout cpass;
    TextInputLayout mobile;
    Button  reset,login;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        mobile=findViewById(R.id.email123);
        pass=findViewById(R.id.password123);
        cpass=findViewById(R.id.mobile123);
        login=findViewById(R.id.login123);
        reset=findViewById(R.id.go123);

        progressDialog=new ProgressDialog(forgotpassword.this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("connecting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgotpassword.this,login.class));
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(mobile,pass,cpass)){
                    progressDialog.show();
                    final FirebaseDatabase database=FirebaseDatabase.getInstance();
                    final DatabaseReference reference=database.getReference("Users").child(mobile.getEditText().getText().toString());

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserRegistrationHelper helper = dataSnapshot.getValue(UserRegistrationHelper.class);
                            if (helper != null) {
                                helper.setPass(pass.getEditText().getText().toString());
                                reference.setValue(helper);
                                progressDialog.hide();
                                startActivity(new Intent(forgotpassword.this,login.class));
                                finish();

                            }
                            else {
                                progressDialog.hide();
                                mobile.setError("Mobile number doesn't exists");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            progressDialog.hide();
                            Toast.makeText(forgotpassword.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


    }

    public static boolean validate(TextInputLayout mobile, TextInputLayout pass, final TextInputLayout cpass){
        String emailPatter="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPatter= "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        if(mobile.getEditText().getText().toString().isEmpty()) {
            mobile.setError("\nMobile cannot be empty\n");return false;
        }
        else{
            mobile.setError("");
            mobile.setErrorEnabled(false);
        }


        if(pass.getEditText().getText().toString().isEmpty()) {
            pass.setError("\nPassword cannot be empty\n");return false;
        }
        else  if(!pass.getEditText().getText().toString().matches(passwordPatter)){
            pass.setError("\nPassword Must contain atleast\nOne Uppercase ,\nOne Lowercase ,\nOne Number ,\nOne Special character and \nBetween 8 to 16 letter length\n");
            return false;
        }
        else{
            pass.setError("");
            pass.setErrorEnabled(false);
        }
        if(!pass.getEditText().getText().toString().equals(cpass.getEditText().getText().toString())) {
            cpass.setError("\nPassword Mismatched\n");return false;
        }
        else{
            cpass.setError("");
            cpass.setErrorEnabled(false);
        }


        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,login.class));
        finish();
    }

}
