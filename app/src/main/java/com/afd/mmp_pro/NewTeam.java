package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;

public class NewTeam extends AppCompatActivity {
EditText teamName,teamAttendees;
Button createBtn;
FirebaseAuth auth;
ProgressDialog pd;
    String [] listOfAttendees;
    String name , attendees,myUid,key,teamKey;
    int current=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        pd = new ProgressDialog(NewTeam.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Creating Your Team..");
        auth = FirebaseAuth.getInstance();
        teamName = findViewById(R.id.classEt);
        teamAttendees = findViewById(R.id.attendeesEt);
        createBtn = findViewById(R.id.createTeam);



        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = teamName.getText().toString();
                attendees = teamAttendees.getText().toString();
                int i = 1;
                if(name.length()<4)
                {
                 teamName.setError("Team name must be at least 4 char long ");
                 i=0;
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(teamName);
                }
                if (attendees.charAt(attendees.length()-1)==','){
                    attendees = attendees.replace(attendees.substring(attendees.length()-1), "");
                }

                if(attendees.length()<1){
                    teamAttendees.setError("Team must have at least one attendee");
                    i=0;
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(teamAttendees);
                }

                if(i==0)
                    return;
                pd.show();
                listOfAttendees = attendees.split(",",2000);
                myUid = auth.getUid();



pd.show();
                DatabaseReference forKey = FirebaseDatabase.getInstance().getReference("Teams");
                teamKey = forKey.push().getKey();

                FirebaseDatabase self = FirebaseDatabase.getInstance();
                self.getReference("Users").child(myUid).child("Teams").child(name+","+teamKey).setValue(myUid).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
if(task.isSuccessful()){
    current=0;
    addMember();
}
else{
    pd.dismiss();
    Toast.makeText(NewTeam.this, "Task Failed, Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
}
                    }
                });
            }
        });
    }

    private void addMember() {

    FirebaseDatabase forFid = FirebaseDatabase.getInstance();
        forFid.getReference("fID").child(listOfAttendees[current].trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    key = snapshot.getValue(String.class);
                    FirebaseDatabase forUser = FirebaseDatabase.getInstance();
                    forUser.getReference("Users").child(key).child("Teams").child(name+","+teamKey).setValue(auth.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           FirebaseDatabase forTeams = FirebaseDatabase.getInstance();
                           forTeams.getReference("Teams").child(teamKey).child(listOfAttendees[current].trim()).setValue(name+","+myUid).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()) {
                                       current++;
                                       if(current<(listOfAttendees.length)){
                                           addMember();
                                       }

                                   }else{
                                       Toast.makeText(NewTeam.this, "Task Failed", Toast.LENGTH_SHORT).show();
                                       Toast.makeText(NewTeam.this, "Error Occurred:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                       pd.dismiss();
                                       return;
                                   }

                               }
                           });
                       }else{
                           Toast.makeText(NewTeam.this, "Task Failed", Toast.LENGTH_SHORT).show();
                           Toast.makeText(NewTeam.this, "Error Occurred:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           pd.dismiss();
                           return;
                       }
                        }
                    });
                }
                else{
                    Toast.makeText(NewTeam.this, listOfAttendees[current]+" Does not exist", Toast.LENGTH_SHORT).show();
                    current++;
                    if(current<(listOfAttendees.length)){
                        addMember();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
if(current==(listOfAttendees.length-1)){
pd.dismiss();
    Toast.makeText(NewTeam.this, "Team Created Successfully", Toast.LENGTH_SHORT).show();
finish();
}
    }
}