package com.example.newchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

     Button button;
     EditText email,password;
     TextView signup;
     FirebaseAuth auth;
     String emailpattern="[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
     android.app.ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        button=findViewById(R.id.loginbutton);
        email=findViewById(R.id.emailtextlogin);
        password=findViewById(R.id.passtextlogin);
        signup=findViewById(R.id.signinlog);

        signup.setOnClickListener(view -> {
            Intent intent=new Intent(login.this, com.example.newchat.signup.class);
            startActivity(intent);
            finish();

        });


        button.setOnClickListener(view -> {
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            if (TextUtils.isEmpty(Email)) {
                progressDialog.dismiss();
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(Password)) {
                progressDialog.dismiss();
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();

            } else if (!Email.matches(emailpattern)) {
                progressDialog.dismiss();
                email.setError("enter valid email");
            } else if (password.length() < 6) {
                progressDialog.dismiss();
                password.setError("password length is less than 6");

            } else {
                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.show();
                            try {
                                Intent intent = new Intent(login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });

    }
}