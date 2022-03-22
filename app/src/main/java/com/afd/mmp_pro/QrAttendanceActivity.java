package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
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
public class QrAttendanceActivity extends AppCompatActivity {
ProgressDialog pd ;
    Workbook wb = new HSSFWorkbook();
    FileOutputStream outputStream = null;
                Cell cell = null;
                Sheet sheet = null;
                Row row;
    ImageView qrcode;
    String dateToStr;
    FirebaseAuth auth;
    String tareek;
    FirebaseDatabase database;
    String name,key,activeKey;
    int k = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_attendance);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        key = extras.getString("key");
        getSupportActionBar().setTitle(name);
        qrcode = findViewById(R.id.qrCode);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        dateToStr = format.format(today);
        int myKey = (int)((Math.random()*1000)+1);
        activeKey = String.valueOf(myKey);
        database.getReference("ActiveKey").child(auth.getUid()).child("key").setValue((String)activeKey);


        FirebaseDatabase forClearing = FirebaseDatabase.getInstance();
        forClearing.getReference("List").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    forClearing.getReference("List").child(name).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

takeAtn();


qrcode.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss a");
        tareek = format.format(today);
        sheet = wb.createSheet(tareek);
        row = sheet.createRow(k);

        cell = row.createCell(0);
        cell.setCellValue("SAP");

        cell = row.createCell(1);
        cell.setCellValue("Name");
                sheet.setColumnWidth(k,(10*900));
                sheet.setColumnWidth(k+1,(10*900));

                k++;
 DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AttendanceList").child(auth.getUid()).child(name+" "+dateToStr);
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

             k++;
         }

//         String root = Environment.getExternalStorageDirectory().toString();
         String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
         File myDir = new File(root + "/MMP-Pro");
         if (!myDir.exists()) {
             myDir.mkdirs();
         }
                File file = new File(myDir,name+" "+tareek+".xls");
                try{
                    outputStream = new FileOutputStream(file);
                    wb.write(outputStream);
                    Toast.makeText(QrAttendanceActivity.this, "Attendance List Download Complete", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (java.io.IOException e){
                    Toast.makeText(QrAttendanceActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    try{
                        outputStream.close();
                    }
                    catch (IOException x){
                        Toast.makeText(QrAttendanceActivity.this, x.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void takeAtn(){
        MultiFormatWriter writer = new MultiFormatWriter();
        try{
            BitMatrix matrix = writer.encode("anilfromdit/"+auth.getUid()+"/"+key+"/"+activeKey+"/"+name+"/"+dateToStr+"/"+Math.random()*10, BarcodeFormat.QR_CODE,256,256);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrcode.setImageBitmap(bitmap);
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        catch (WriterException e){
            Toast.makeText(QrAttendanceActivity.this, "An Error Occurred\nAttendance Mode Finished", Toast.LENGTH_SHORT).show();
            finish();
        }


        database.getReference("ActiveKey").child(auth.getUid()).child("key").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String keyI = snapshot.getValue(String.class);
                if(Integer.parseInt(activeKey)+1==Integer.parseInt(keyI)){
                    Toast.makeText(QrAttendanceActivity.this, "Attendance Marked", Toast.LENGTH_SHORT).show();
                    activeKey = keyI;
                    takeAtn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QrAttendanceActivity.this);

        builder.setTitle("Are you sure?");
        builder.setMessage("Confirm To Exit?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QrAttendanceActivity.super.onBackPressed();
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