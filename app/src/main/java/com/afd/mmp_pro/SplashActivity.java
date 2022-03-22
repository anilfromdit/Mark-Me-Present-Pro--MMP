package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {
TextView tv;
CircleImageView imageView;
ImageView top,bottom;
final String version = "1.1.4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefTheme",MODE_PRIVATE);
        String s1 = sharedPreferences.getString("Theme", "0");

        if(s1.equals("Light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (s1.equals("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        tv = findViewById(R.id.mySlogan);
        top= findViewById(R.id.topwave);
        bottom= findViewById(R.id.bottomwave);
        imageView = findViewById(R.id.image);

                YoYo.with(Techniques.FadeInUp).duration(4000).playOn(tv);
        YoYo.with(Techniques.SlideInDown).duration(2000).playOn(top);
        YoYo.with(Techniques.SlideInUp).duration(2000).playOn(bottom);
        new Thread(new Runnable() {
            public void run() {

                try {
                    Animation zoom;
                    zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
                    imageView.startAnimation(zoom);
                    Thread.sleep(2050);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isInternetAvailable()){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("version");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String latestVersion = snapshot.getValue(String.class);

                            if(latestVersion.equals(version)){
                                Intent intent = new Intent( SplashActivity.this, LoginActivity.class);
                                intent.addCategory( Intent.CATEGORY_HOME );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
}
                       else{
                                Intent intent2 = new Intent( SplashActivity.this, OnOldVersionActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent2);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Intent noNet = new Intent (SplashActivity.this, NotConnectedActivity.class);
                    startActivity(noNet);
                    finish();
                }
                finish();

            }
        }).start();

    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }


}