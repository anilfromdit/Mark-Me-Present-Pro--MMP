package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ThemesActivity extends AppCompatActivity {
RelativeLayout light,dark,system;
ImageView lightTheme, darkTheme,systemTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        getSupportActionBar().setTitle("Themes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        light = findViewById(R.id.lightThemeLL);
        dark = findViewById(R.id.darkThemeLL);
        system = findViewById(R.id.systemThemeLL);
        lightTheme = findViewById(R.id.lightTheme);
        darkTheme = findViewById(R.id.darkTheme);
        systemTheme = findViewById(R.id.systemTheme);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefTheme",MODE_PRIVATE);
        String s1 = sharedPreferences.getString("Theme", "0");

        if(s1.equals("Light")){
            lightTheme.setImageResource(R.drawable.checked);
            darkTheme.setImageResource(R.drawable.unchecked);
            systemTheme.setImageResource(R.drawable.unchecked);
        }
        else if (s1.equals("Dark")){
            lightTheme.setImageResource(R.drawable.unchecked);
            darkTheme.setImageResource(R.drawable.checked);
            systemTheme.setImageResource(R.drawable.unchecked);
        }
        else{
            lightTheme.setImageResource(R.drawable.unchecked);
            darkTheme.setImageResource(R.drawable.unchecked);
            systemTheme.setImageResource(R.drawable.checked);
        }


        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightTheme.setImageResource(R.drawable.checked);
                darkTheme.setImageResource(R.drawable.unchecked);
                systemTheme.setImageResource(R.drawable.unchecked);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("Theme", "Light");
                myEdit.apply();

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



            }
        });

        dark.setOnClickListener(v -> {
            lightTheme.setImageResource(R.drawable.unchecked);
            darkTheme.setImageResource(R.drawable.checked);
            systemTheme.setImageResource(R.drawable.unchecked);
            SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPrefTheme",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences1.edit();
            myEdit.putString("Theme", "Dark");
            myEdit.apply();


            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        });

        system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightTheme.setImageResource(R.drawable.unchecked);
                darkTheme.setImageResource(R.drawable.unchecked);
                systemTheme.setImageResource(R.drawable.checked);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefTheme",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("Theme", "System");
                myEdit.apply();


                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}