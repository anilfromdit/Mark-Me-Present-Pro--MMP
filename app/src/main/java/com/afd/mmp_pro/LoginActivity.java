package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button loginBtn;
    String DeviceId;
    TextView signup, newBindTV, forgotPass;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog pd;
    String Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(LoginActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Logging In...");
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        DeviceId=getDevice(LoginActivity.this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.signUpTxt);
        forgotPass = findViewById(R.id.forgotPass);
        newBindTV = findViewById(R.id.newBind);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Feature Not Activated Yet", Toast.LENGTH_SHORT).show();
            }
        });

        if(auth.getUid()!=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.addCategory( Intent.CATEGORY_HOME );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, " "+DeviceId, Toast.LENGTH_SHORT).show();

                Email = email.getText().toString().trim();
                Password = password.getText().toString();
                if(Email.length()<5){
                    email.setError("Email is Required");
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(email);
                    return;
                }
                if(Password.length()<4){
                    password.setError("Password is Required");
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(password);
                    return;
                }
                pd.show();
                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pd.setMessage("Verifying Device ID");
                            database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                    String di = map.get("DeviceId");
                                    if(di.equals(DeviceId)){
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        auth.signOut();
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this, "This Account is Registered with an other device ", Toast.LENGTH_SHORT).show();
                                        newBindTV.setVisibility(View.VISIBLE);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("HardwareIds")
    private String getDevice(Activity activity) {
        Toast.makeText(LoginActivity.this, Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID), Toast.LENGTH_SHORT).show();
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}