package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import fragments.AccountFragment;
import fragments.AppSettingFragment;
import fragments.ReportsFragment;
import fragments.TeamsFragment;

public class MainActivity extends AppCompatActivity {
BottomNavigationView btmNav;
Button usingScan , usingId;
    String reportKey;
FloatingActionButton fab;
    FirebaseDatabase forReport = FirebaseDatabase.getInstance();
    HashMap<Object, String> reportMap ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth;
    String MyName , MySap,activeKey,Usd,key,name,dateTime;
    ProgressDialog pd ;
    String check;
    String[] arrOfStr;
    int n=0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_team_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.newTeam) {

            Intent intent = new Intent(MainActivity.this, NewTeam.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        btmNav = findViewById(R.id.btmNavBar);
        fab = findViewById(R.id.MarkAttendance);
        usingScan = findViewById(R.id.mmpScan);
        usingId = findViewById(R.id.mmpID);
        auth = FirebaseAuth.getInstance();

        btmNav.setBackground(null);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frgCnt, new TeamsFragment());
        fragmentTransaction.commit();
        btmNav.setSelectedItemId(R.id.myTeams);

        usingScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n=0;
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Align QR Code in Square Box");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(scan.class);
                intentIntegrator.initiateScan();
            }
        });
        usingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Intent intent = new Intent(MainActivity.this, mmpUsingIdActivity.class);
startActivity(intent);

            }
        });
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(usingScan.getVisibility()==View.GONE){
            usingScan.setVisibility(View.VISIBLE);
            usingId.setVisibility(View.VISIBLE);
        }
        else if (usingScan.getVisibility()==View.VISIBLE){
            usingScan.setVisibility(View.GONE);
            usingId.setVisibility(View.GONE);
        }
    }
});
        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.myAccount:
                        fragmentTransaction.replace(R.id.frgCnt, new AccountFragment());
                        break;
                    case R.id.myReports:
                        fragmentTransaction.replace(R.id.frgCnt, new ReportsFragment());
                        break;
                    case R.id.myTeams:
                        fragmentTransaction.replace(R.id.frgCnt, new TeamsFragment());
                        break;
                        case R.id.myApp:
                        fragmentTransaction.replace(R.id.frgCnt, new AppSettingFragment());
                        break;


                }
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
        pd = new ProgressDialog(MainActivity.this);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode , resultCode, data);

                if(intentResult.getContents()!=null){
                    try {
                        arrOfStr = intentResult.getContents().split("/", 8);
                        Usd = arrOfStr[1];
                        key = arrOfStr[2];
                        activeKey = arrOfStr[3];
                        name = arrOfStr[4];
                        dateTime = arrOfStr[5];
                        pd.show();
                        pd.setTitle(name);
                        pd.setMessage("Marking Your Present");


                            database.getReference("ActiveKey").child(Usd).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, String> map = (Map<String, String>) snapshot.getValue();

                                    if (arrOfStr[0].equals("anilfromdit")) {

                                        if (n == 0) {
                                            check = map.get("key");

                                            if (check.equals(activeKey)) {
                                                n++;
                                                try {
                                                    FirebaseDatabase forAtn = FirebaseDatabase.getInstance();
                                                    FirebaseDatabase forList = FirebaseDatabase.getInstance();
                                                    forList.getReference("List").child(name).child(activeKey).setValue(MySap);
                                                            forAtn.getReference("AttendanceList").child(Usd).child(name + " " + dateTime).child(MySap).setValue(MyName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            int newKey = Integer.parseInt(activeKey) + 1;
                                                            String newKeyFinal = Integer.toString(newKey);
                                                            FirebaseDatabase toUpdatekey = FirebaseDatabase.getInstance();
                                                            toUpdatekey.getReference("ActiveKey").child(Usd).child("key").setValue(newKeyFinal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                public void onSuccess(Void unused) {

                                                                    Toast.makeText(getApplicationContext(), MyName + " Marked Present", Toast.LENGTH_SHORT).show();
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                                                    builder.setTitle(name);
                                                                    builder.setMessage(" Marked Present In " + name);
                                                                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            pd.dismiss();
                                                                            dialog.dismiss();

                                                                        }
                                                                    });
                                                                    builder.show();
                                                                    pd.dismiss();
                                                                }
                                                            });


                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    pd.dismiss();
                                                }

                                            } else if(!check.equals(activeKey)) {

                                                DatabaseReference  prev = FirebaseDatabase.getInstance().getReference("List").child(name).child(activeKey);
                                                DatabaseReference next = FirebaseDatabase.getInstance().getReference("List").child(name).child(String.valueOf(Integer.parseInt(activeKey)+1));
                                                DatabaseReference forKey = FirebaseDatabase.getInstance().getReference("Reports");
                                                reportKey = forKey.push().getKey();

                                                Date today = new Date();
                                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                                                String dateToStr = format.format(today);
reportMap = new HashMap<>();

                                                reportMap.put("lecture", name);
                                                reportMap.put("key",reportKey);
                                                reportMap.put("sap", MySap);
                                                reportMap.put("date_time", dateToStr);
                                                reportMap.put("uid", auth.getUid());
                                                prev.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        System.out.println(snapshot);
                                                        if(snapshot.exists()){
                                                            reportMap.put("previous",snapshot.getValue(String.class));
                                                        }else{
                                                            reportMap.put("previous","Init");

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                next.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        System.out.println(snapshot);
                                                        if(snapshot.exists()){
                                                            reportMap.put("next",snapshot.getValue(String.class));

                                                        }else{
                                                            reportMap.put("next","End");

                                                        }


                                                        Toast.makeText(MainActivity.this, "Keys Does Not Match", Toast.LENGTH_SHORT).show();
                                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                                                        builder3.setTitle("Reporting");
                                                        builder3.setMessage("This Incident Will Be Reported");
                                                        builder3.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                forReport.getReference("Reports").child(Usd).child(reportKey).setValue(reportMap);
                                                                pd.dismiss();
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        builder3.show();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }


                                        }
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(MainActivity.this, "This QR code does not belong to this app", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                                        builder2.setTitle("Invalid QR Code");
                                        builder2.setMessage("This is an invalid qr code for attendance");
                                        builder2.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder2.show();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    }
                    catch (Exception e ){
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "This QR code does not belong to this app", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                        builder2.setTitle("Invalid QR Code");
                        builder2.setMessage("This is an invalid qr code for attendance");
                        builder2.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder2.show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Could Not Read Any QR CODE", Toast.LENGTH_SHORT).show();
                }
    }


    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("ActiveKey").child(auth.getUid()).child("key").setValue("0");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase db1 = FirebaseDatabase.getInstance();


        db1.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map <String,String> map = (Map<String, String>) snapshot.getValue();
                MyName = map.get("Name");
                MySap = map.get("Sap");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

