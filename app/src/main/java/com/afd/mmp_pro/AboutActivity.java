package com.afd.mmp_pro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AboutActivity extends AppCompatActivity {
LinearLayout l,l1,l2,l3,l4,l5,l6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About Us");
l = findViewById(R.id.linlay);
l1 = findViewById(R.id.linlay1);
l2 = findViewById(R.id.linlay2);
l3 = findViewById(R.id.linlay3);
l4 = findViewById(R.id.linlay4);
l5 = findViewById(R.id.linlay5);
l6 = findViewById(R.id.linlay6);

l1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/officialvaibhavmaurya/"));
        startActivity(intent);
    }
});
l3.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://anilfromdit.github.io/"));
        startActivity(intent);
    }
});


l4.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/anilfromdit"));
        startActivity(intent);
    }
});

l5.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/in/anilfromdit/"));
        startActivity(intent);
    }
});

l6.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/anilfromdit/"));
        startActivity(intent);
    }
});
    }
}