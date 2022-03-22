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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class IdAttendanceActivity extends AppCompatActivity {
EditText idET,passET;
Button btn;
TextView counter,counterTag,prompt;
FirebaseAuth auth;
String teamName,teamKey,id,password,dateToStr;
RelativeLayout layout;
ProgressDialog pd;
int k=0;
    Workbook wb = new HSSFWorkbook();
    FileOutputStream outputStream = null;
    Cell cell = null;
    Sheet sheet = null;
    Row row;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_attendance);

        Bundle extras = getIntent().getExtras();
        teamName = extras.getString("name");
        teamKey = extras.getString("key");
        getSupportActionBar().setTitle(teamName);
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(IdAttendanceActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Initializing Attendance Mode...");
counter = findViewById(R.id.count);
idET = findViewById(R.id.mmp_id);
passET = findViewById(R.id.mmp_pass);
btn = findViewById(R.id.start_btn);
layout = findViewById(R.id.my_rel);
counterTag = findViewById(R.id.a_txt);
prompt = findViewById(R.id.download_atn_list);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idET.getText().toString();
                password = passET.getText().toString();
                if(id.length()<6){
                    YoYo.with(Techniques.RubberBand).duration(500).playOn(idET);
                    idET.setError("Id must have at least 6 chars");
                    return;
                }
                if (password.length()<6){
                    YoYo.with(Techniques.RubberBand).duration(500).playOn(passET);
                    passET.setError("Password must be at least 6 chars long");
                    return;
                }

                pd.show();
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss a");
                dateToStr = format.format(today);
                HashMap<Object,String> hashMap = new HashMap<>();
                hashMap.put("usd",auth.getUid());
                hashMap.put("password",password);
                hashMap.put("name",teamName+" "+dateToStr);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("ActiveIdPass").child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(IdAttendanceActivity.this, "Attendance Mode Initialized", Toast.LENGTH_SHORT).show();
                            AttendanceMode();
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(IdAttendanceActivity.this, "Task Failed:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }


                });

            }
        });
        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheet = wb.createSheet(dateToStr);
                row = sheet.createRow(k);

                cell = row.createCell(0);
                cell.setCellValue("SAP");

                cell = row.createCell(1);
                cell.setCellValue("Name");
                sheet.setColumnWidth(k,(10*200));
                sheet.setColumnWidth(k,(10*200));

                k++;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AttendanceList").child(auth.getUid()).child(teamName+" "+dateToStr);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot snap:snapshot.getChildren()){
                            row = sheet.createRow(k);
                            String Sap = snap.getValue(String.class);
                            String aName = snap.getKey();
                            cell = row.createCell(0);
                            cell.setCellValue(aName);
                            cell = row.createCell(1);
                            cell.setCellValue(Sap);
                            sheet.setColumnWidth(k,(10*200));
                            sheet.setColumnWidth(k,(10*200));

                            k++;
                        }

                        File file = new File(getExternalFilesDir(null),teamName+" "+dateToStr+".xls");
                        try{
                            outputStream = new FileOutputStream(file);
                            wb.write(outputStream);
                            Toast.makeText(IdAttendanceActivity.this, "Attendance List Download Complete", Toast.LENGTH_SHORT).show();
                            finish();
                        }catch (java.io.IOException e){
                            Toast.makeText(IdAttendanceActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            try{
                                outputStream.close();
                            }
                            catch (IOException x){
                                Toast.makeText(IdAttendanceActivity.this, x.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

     void AttendanceMode() {
         getSupportActionBar().hide();
         idET.setVisibility(View.GONE);
         passET.setVisibility(View.GONE);
         btn.setVisibility(View.GONE);
         layout.setBackgroundColor(getResources().getColor(R.color.black));
         counter.setVisibility(View.VISIBLE);
         counterTag.setVisibility(View.VISIBLE);
         prompt.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         database.getReference("AttendanceList").child(auth.getUid()).child(teamName+" "+dateToStr).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()) {
                     int countA = (int)snapshot.getChildrenCount();
                     counter.setText(String.valueOf(countA));
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(IdAttendanceActivity.this);

        builder.setTitle("Are you sure?");
        builder.setMessage("Confirm To Exit?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(id!=null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference("ActiveIdPass").child(id).removeValue();
                }
                IdAttendanceActivity.super.onBackPressed();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}