package com.example.newchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainuserrecyclerview;
    userAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView logoutview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("user");
        usersArrayList=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logoutview=findViewById(R.id.logout);
        logoutview.setOnClickListener(view -> {
            Dialog dialog=new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialoguefoelogout);
            Button yes,no;
            no=dialog.findViewById(R.id.nobtn);
            yes=dialog.findViewById(R.id.yesbtn);
            yes.setOnClickListener(view1 -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            });
            no.setOnClickListener(view12 -> {
                dialog.dismiss();
            });
            dialog.show();
        });

        mainuserrecyclerview=findViewById(R.id.mainuserrecyclerview);
        mainuserrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter=new userAdapter(MainActivity.this,usersArrayList);
        mainuserrecyclerview.setAdapter(adapter);
        if(auth.getCurrentUser()==null){
            Intent intent=new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        }
    }
}