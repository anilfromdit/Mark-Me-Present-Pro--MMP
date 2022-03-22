package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database,database1,database2,database3;
    ProgressDialog pd;
    EditText name,email,course,year,sap,rollno,password;
    TextView login;
    Spinner gender;
    Button signup;
    String Name,Email,Gender,Course,Year,Sap,Rollno,Password;
    String DeviceId;
    int[] response = {1};
    int a = 1;
    String [] list = {"Choose Your Gender","Male","Female","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
          auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        database2 = FirebaseDatabase.getInstance();
        database3 = FirebaseDatabase.getInstance();
        pd = new ProgressDialog(SignUpActivity.this);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        course = findViewById(R.id.course);
        year = findViewById(R.id.year);
        sap = findViewById(R.id.sap);
        rollno = findViewById(R.id.rollno);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        gender = findViewById(R.id.gender);
        signup = findViewById(R.id.signup);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        DeviceId=getDevice(SignUpActivity.this);
        Query deviceIdQuery=database.getReference().child("Users").orderByChild("DeviceId").equalTo(DeviceId);
        deviceIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0){
                    pd.dismiss();
                    response[0]=2;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        signup.setOnClickListener(v -> {
            if(response[0]==2){
                Toast.makeText(SignUpActivity.this, "Device Already Register", Toast.LENGTH_SHORT).show();
            }else{
response[0]=1;
                Query SapQuery=database.getReference().child("Users").orderByChild("Sap").equalTo(sap.getText().toString());

                SapQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.getChildrenCount()>0) {
                            pd.dismiss();
                            Toast.makeText(SignUpActivity.this, "SAP ID Already Registered", Toast.LENGTH_SHORT).show();
                            response[0] = 0;

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                ProgressDialog TempDialog;
                CountDownTimer mCountDownTimer;
                int i=0;

                TempDialog = new ProgressDialog(SignUpActivity.this);
                TempDialog.setMessage("Please wait...");
                TempDialog.setCancelable(false);
                TempDialog.setProgress(i);
                TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

                TempDialog.show();
                mCountDownTimer = new CountDownTimer(2000, 1000)
                {
                    public void onTick(long millisUntilFinished)
                    {
                        TempDialog.setMessage("Please wait..");
                    }

                    public void onFinish()
                    {
                        TempDialog.dismiss();
                        if(response[0]==1) {
                            work();
                        }
                    }
                }.start();

            }
        });


    }
    public void work(){
response[0]=1;
        pd.setTitle("Please Wait");
        pd.setMessage("Creating Your Account");
        pd.show();
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Gender =  gender.getSelectedItem().toString();
        Course = course.getText().toString().trim();
        Year = year.getText().toString().trim();
        Sap = sap.getText().toString().trim();
        Rollno = rollno.getText().toString().trim();
        Password = password.getText().toString();
        if(Name.length()==0){
            name.setError("Please Enter Your Name");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(name);
            response[0] =0;
        }
        if(Email.length()==0){
            email.setError("Please Enter Your Email");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(email);
            response[0] =0;
        }

        if(Gender.equals("Choose Your Gender")){
            ((TextView)gender.getSelectedView()).setError("Choose Your Gender.");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(gender);
            response[0] =0;
        }
        if(Course.length()==0){
            course.setError("Please Enter Your Course");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(course);
            response[0] =0;
        }
        if(Year.length()==0 || Integer.parseInt(Year)>5 || Integer.parseInt(Year)<1){
            year.setError("Please Enter Valid Academic Year");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(year);
            response[0] =0;
        }
        if(Sap.length()==0 ){
            sap.setError("Please Enter Valid SAP ID");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(sap);
            response[0] =0;
        }
        if(Rollno.length()==0 || Double.parseDouble(Rollno)<200000000){
            Rollno="NA";
        }
        if(Password.length()==0){
            password.setError("Please Choose Your Password");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(password);
            response[0] =0;
        }

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(Password);

        if(!m.matches()){
            password.setError("Password Must Contain at least (8 Character):\n1. one lower case alphabet\n2. one upper case Alphabet\n3. One Special Character [@#$%^&+=] ");
            YoYo.with(Techniques.RubberBand).duration(700).playOn(password);
            response[0]=0;
        }



        if(response[0] == 1) {
            auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        HashMap<String , Object> hashMap1 = new HashMap<>();

                        String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        hashMap1.put("Name", Name);
                        hashMap1.put("Email", Email);
                        hashMap1.put("Gender", Gender);
                        hashMap1.put("Course", Course);
                        hashMap1.put("Year", Year);
                        hashMap1.put("Sap", Sap);
                        hashMap1.put("Rollno", Rollno);
                        hashMap1.put("Uid", id);
                        hashMap1.put("DeviceId", DeviceId);


                        database1.getReference("Users").child(id).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                    HashMap<String , Object> hashMap2 = new HashMap<>();
                                    hashMap2.put("SAP", Sap);
                                    hashMap2.put("Email", Email);
                                    hashMap2.put("DeviceId", DeviceId);
                                    hashMap2.put("Date",currentDate);
                                    database2.getReference("Binding").child(id).setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            database3.getReference("fID").child(Sap).setValue(auth.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                    pd.dismiss();
                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    }
                                                }
                                            });

                                            }
                                        }
                                    });
                                }
         }
                        });
                    }
                    else{
                        pd.dismiss();
                        Toast.makeText(SignUpActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{

            pd.dismiss();
        }
    }

    private String getDevice(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}