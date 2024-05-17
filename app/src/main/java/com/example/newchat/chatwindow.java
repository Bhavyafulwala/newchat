package com.example.newchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwindow extends AppCompatActivity {
    String receiverimg,receiveruid,receivername,senderuid;
    CircleImageView profileimg;
    TextView chattername;
    ImageButton sendbtn;
    EditText msgwrite;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String receiverIMG;
    String senderRoom,receiverRoom;
    RecyclerView msgrecyclerview;
    ArrayList<Msgmodelclass> messagesArrayList;
    messagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindow);
        firebaseAuth=FirebaseAuth.getInstance();

        receivername=getIntent().getStringExtra("namee");
        receiverimg=getIntent().getStringExtra("Receiverimg");
        receiveruid=getIntent().getStringExtra("userid");

        messagesArrayList=new ArrayList<>();

        profileimg=findViewById(R.id.profileimg);
        chattername=findViewById(R.id.chatwinNAme);
        msgrecyclerview=findViewById(R.id.mesgRecyclerview);



        Picasso.get().load(receiverimg).into(profileimg);
        chattername.setText(""+receivername);
        senderuid= firebaseAuth.getCurrentUser().getUid();
        senderRoom=senderuid+receiveruid;
        receiverRoom=receiveruid+senderuid;


        database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("user").child(Objects.requireNonNull(firebaseAuth.getUid()));
        DatabaseReference chatrefernce=database.getReference().child("user").child(senderRoom).child("messages");
        chatrefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                Log.e("errer1","working");
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Msgmodelclass messages=dataSnapshot.getValue(Msgmodelclass.class);
                    messagesArrayList.add(messages);

                }
                messagesAdapter.notifyDataSetChanged();
                Log.e("errer","working");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Assuming you already have the receiverRoom variable set up

// Add ValueEventListener for the receiver's room
        DatabaseReference receiverChatReference = FirebaseDatabase.getInstance().getReference().child("user").child(receiverRoom).child("messages");
        receiverChatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing messagesArrayList to avoid duplication
                messagesArrayList.clear();

                // Iterate through the dataSnapshot to retrieve messages
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Msgmodelclass message = dataSnapshot.getValue(Msgmodelclass.class);
                    messagesArrayList.add(message);
                }

                // Notify the adapter that the dataset has changed
                messagesAdapter.notifyDataSetChanged();

                // Optionally, scroll the RecyclerView to the bottom
                msgrecyclerview.scrollToPosition(messagesArrayList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg=snapshot.child("profilepic").getValue().toString();
                receiverIMG=receiverimg;
                Log.e("working2","wooo");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        senderuid= firebaseAuth.getUid();
        senderRoom=senderuid+receiveruid;
        receiverRoom=receiveruid+senderuid;
        sendbtn=findViewById(R.id.sendbtn);
        msgwrite=findViewById(R.id.msgpart);
        sendbtn.setOnClickListener(view -> {
            String message=msgwrite.getText().toString();
            if(message.isEmpty()){
                msgwrite.setError("Please write a message");
            }
            msgwrite.setText("");
            Date date=new Date();
            Msgmodelclass messages=new Msgmodelclass(message,senderuid,date.getTime());
            database=FirebaseDatabase.getInstance();
            database.getReference().child("chats").child("senderRoom").child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    database.getReference().child("chats").child("receiverRoom").child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            });

        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        msgrecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new messagesAdapter(chatwindow.this,messagesArrayList);
        msgrecyclerview.setAdapter(messagesAdapter);


    }
}