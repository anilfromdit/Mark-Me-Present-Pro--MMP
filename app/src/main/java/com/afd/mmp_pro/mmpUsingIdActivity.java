package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

public class mmpUsingIdActivity extends AppCompatActivity {
EditText mmpid,mmppass;
Button start;
ProgressDialog pd;
String mySap,myName,Id,Pass;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmp_using_id);
        mmpid = findViewById(R.id.mmp_id);
        mmppass = findViewById(R.id.mmp_pass);
        auth = FirebaseAuth.getInstance();
        start = findViewById(R.id.mmp_start_btn);
        pd = new ProgressDialog( mmpUsingIdActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Please wait while we are marking you present");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id = mmpid.getText().toString();
                Pass = mmppass.getText().toString();
                int res=1;
                if(Id.length()<4){
                    mmpid.setError("Id Length must be greater than 4 char");
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(mmpid);
                    res = 0;

                }
                if(Pass.length()<4){
                    mmppass.setError("Password Length must be greater than 4 char");
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(mmppass);
                    res = 0;
                }
                if(res != 1){
                    return;
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("ActiveIdPass").child(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println(snapshot);
                        if(snapshot.exists()){
                            Map<String , String> map = (Map<String, String>) snapshot.getValue();
                            String sPass=map.get("password");
                          if(Pass.equals(sPass)){
                                String usd = map.get("usd");
                                String teamName = map.get("name");
                                markMe(usd,teamName);
                            }
                            else{
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(mmpUsingIdActivity.this);
                                builder.setTitle("Invalid Credentials");
                                builder.setMessage("Oops,Invalid ID or Password +");
                                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else{

                            AlertDialog.Builder builder = new AlertDialog.Builder(mmpUsingIdActivity.this);
                            builder.setTitle("Invalid Credentials");
                            builder.setMessage("Oops,Invalid ID or Password");
                            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
    void markMe(String usd, String teamName){
        String [] name = teamName.split(" ",5);
       FirebaseDatabase mmpRef = FirebaseDatabase.getInstance();
        mmpRef.getReference("AttendanceList").child(usd).child(teamName).child(mySap).setValue(myName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mmpUsingIdActivity.this);
                    builder.setTitle("Success");
                    builder.setMessage(myName+", You're Marked Present in "+ name[0]);
                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(mmpUsingIdActivity.this);
                    builder.setTitle("Failed");
                    builder.setMessage("Oops, There was an error while marking you present");
                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String , String> map1 = (Map<String, String>) snapshot.getValue();
                mySap = map1.get("Sap");
                myName = map1.get("Name");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}