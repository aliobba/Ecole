package com.example.meyss.monecole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.meyss.monecole.Activities.login;

public class SplashScreen extends AppCompatActivity {
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image = (ImageView) findViewById(R.id.hand);
        final Animation animRotate = AnimationUtils.loadAnimation(this,R.anim.rotate);
        animRotate.setDuration(4000);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        image.startAnimation(animRotate);
        thread.start();
    }
}
