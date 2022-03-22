package com.afd.mmp_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class NotConnectedActivity extends AppCompatActivity {
Button retryButton;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_connected);
        retryButton = findViewById(R.id.retryButton);
        imageView = findViewById(R.id.dogPic);

        Animation myAnimation;
        myAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.not_connected_anim);
        imageView.startAnimation(myAnimation);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotConnectedActivity.this, "hn mallom hai ki amazon ki copy hai", Toast.LENGTH_SHORT).show();
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotConnectedActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}