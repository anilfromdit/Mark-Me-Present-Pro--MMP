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

public class FeedbackActivity extends AppCompatActivity {
EditText feedback;
Button submit;
    FirebaseAuth auth;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new ProgressDialog(FeedbackActivity.this);

        getSupportActionBar().setTitle("Feedback");
        setContentView(R.layout.activity_feedback);
auth = FirebaseAuth.getInstance();
    feedback = findViewById(R.id.feedbackET);
    submit = findViewById(R.id.submit_feedback);
    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String text;
            text = feedback.getText().toString();
            if(text.length()<4){
                feedback.setError("Feedback should contain atleast 4 letter");
                Toast.makeText(FeedbackActivity.this, "Feedback should contain atleast 4 letter", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();

            Task<Void> database = FirebaseDatabase.getInstance().getReference("feedbacks").child(auth.getUid()).child(now.toString()).setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                    builder.setTitle("Thank You ");
                    builder.setMessage("Thank you for your feedback ♥");
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