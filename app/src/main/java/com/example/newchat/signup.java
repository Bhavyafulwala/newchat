package com.example.newchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {

    EditText Email,Username,Password,reenter;
    Button signup;
    TextView login;
    CircleImageView circleimage;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailpattern="[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://newchat-7c3b4-default-rtdb.firebaseio.com/");
        storage=FirebaseStorage.getInstance();
        Email=findViewById(R.id.emailsign);
        Username=findViewById(R.id.username);
        Password=findViewById(R.id.passsign);
        reenter=findViewById(R.id.reenter);
        login=findViewById(R.id.loginsign);
        signup=findViewById(R.id.signbutton);
        circleimage=findViewById(R.id.profilerg);

        login.setOnClickListener(view -> {
            Intent intent=new Intent(com.example.newchat.signup.this, com.example.newchat.login.class);
            startActivity(intent);
            finish();
        });
        signup.setOnClickListener(view -> {
            String name=Username.getText().toString();
            String emaill=Email.getText().toString();
            String passwordd=Password.getText().toString();
            String cpassword=reenter.getText().toString();
            String status="hey i am using it";

            if(TextUtils.isEmpty(name)||TextUtils.isEmpty(emaill)||TextUtils.isEmpty(passwordd)||TextUtils.isEmpty(cpassword)){
                progressDialog.dismiss();
                Toast.makeText(this, "PLease enter valid information", Toast.LENGTH_SHORT).show();
            } else if (!emaill.matches(emailpattern)) {
                progressDialog.dismiss();
                Email.setError("Invalid emaill");
            } else if (passwordd.length()<6) {
                progressDialog.dismiss();
                Password.setError("should be more than 5 char");
            } else if (!cpassword.matches(passwordd)) {
                progressDialog.dismiss();
                reenter.setError("password not matched!!");
            }
            else {
                auth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String id=task.getResult().getUser().getUid();
                            DatabaseReference reference=database.getReference().child("user").child(id);
                            StorageReference storageReference=storage.getReference().child("Upload").child(id);

                            if(imageURI!=null){
                                storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            Log.e("added","added success");
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageuri=uri.toString();
                                                    Users users=new Users(id,name,emaill,passwordd,imageuri, status);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.show();
                                                                Intent intent=new Intent(com.example.newchat.signup.this,MainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }else {
                                                                Toast.makeText(signup.this, "oops something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }else {
                                String status="hey i am using it";
                                imageuri="https://firebasestorage.googleapis.com/v0/b/newchat-7c3b4.appspot.com/o/profileimg.png?alt=media&token=d040a610-3ebd-41c5-9cf1-476c515d7080";
                                Users users=new Users(id,name,emaill,passwordd,imageuri, status);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.e("added","added success");
                                            Toast.makeText(signup.this, "added", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(com.example.newchat.signup.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(signup.this, "oops something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }else {
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

        circleimage.setOnClickListener(view -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select picture"),10);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageURI=data.getData();
                circleimage.setImageURI(imageURI);


            }
        }
    }
}