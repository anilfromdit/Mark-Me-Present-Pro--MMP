package com.afd.mmp_pro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OnOldVersionActivity extends AppCompatActivity {

    Button download;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_old_version);
        download = findViewById(R.id.downloadButton);
        imageView = findViewById(R.id.updateImage);
        anim();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://drive.google.com/drive/folders/1Nd0BWlw3e2F5bB64rqKSve_1G3bIJA25?usp=sharing"));
                startActivity(intent);
            }
        });


    }

    void anim(){
        Handler h = new Handler();
h.postDelayed(new Runnable() {
    @Override
    public void run() {
                YoYo.with(Techniques.RotateOut).duration(2600).playOn(imageView);
        anim();
    }
},2400);

    }
}