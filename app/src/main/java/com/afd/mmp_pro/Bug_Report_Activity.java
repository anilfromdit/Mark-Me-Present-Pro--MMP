package com.afd.mmp_pro;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;

public class Bug_Report_Activity extends AppCompatActivity {
    EditText report;
    ProgressDialog pd;
    FirebaseAuth auth;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        pd = new ProgressDialog(Bug_Report_Activity.this);
        getSupportActionBar().setTitle("Bug Report");
        auth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.reportBtn);
        report = findViewById(R.id.reportET);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text;
                text = report.getText().toString();
                if(text.length()<4){
                    report.setError("Bug Report should contain at least 4 letter");
                    Toast.makeText(Bug_Report_Activity.this, "Report should contain atleast 4 letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();


                Task<Void> database = FirebaseDatabase.getInstance().getReference("BugReports").child(auth.getUid()).child(now.toString()).setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Bug_Report_Activity.this);
                        builder.setTitle("Thank You ");
                        builder.setMessage("Thank you for Reporting a Bug 🪲");
                        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        builder.show();

                    }
                });
            }
        });


    }

}