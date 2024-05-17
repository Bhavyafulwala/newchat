package com.example.newchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    TextView appname,companyname;
    ImageView myname;
    Animation topanim,bottomanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myname=findViewById(R.id.bhavyaview);
        companyname=findViewById(R.id.companytext);
        appname=findViewById(R.id.messengertext);

        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        companyname.setAnimation(topanim);
        myname.setAnimation(topanim);
        appname.setAnimation(bottomanim);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);
    }
}