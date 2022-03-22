package com.afd.mmp_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapters.TeamMemberAdapter;
import Models.TeamsModel;

public class TeamDetailActivity extends AppCompatActivity {
    EditText newMembers;
    TextView heading;
    Button addBtn,addListBtn;
    String []  listOfAttendees ;
    String teamName,teamKey,myUid,key;
    ProgressDialog pd ;
    FirebaseAuth auth;
    int current=0;

    private RecyclerView recyclerView;
    TeamMemberAdapter memberAdapter;
    List<TeamsModel> mTeamMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        Bundle extras = getIntent().getExtras();
        teamName = extras.getString("name");
        teamKey = extras.getString("key");
    Objects.requireNonNull(getSupportActionBar()).setTitle(teamName);
        newMembers = findViewById(R.id.attendeesTBA);
        addBtn = findViewById(R.id.add_btn);
        addListBtn = findViewById(R.id.add_list);
        heading = findViewById(R.id.heading);
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(TeamDetailActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Please wait while we are adding new members");
        myUid = auth.getUid();
        mTeamMember = new ArrayList<>();

        recyclerView = findViewById(R.id.teamMembers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        memberAdapter = new TeamMemberAdapter(getApplicationContext(),mTeamMember);
        recyclerView.setAdapter(memberAdapter);

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teams").child(teamKey);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mTeamMember.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String name = child.getKey();
                        TeamsModel teamsModel = new TeamsModel(name);
                        mTeamMember.add(teamsModel);
                        memberAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e ){
            Toast.makeText(TeamDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }









        addBtn.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("Users").child(Objects.requireNonNull(auth.getUid())).child("Teams").child(teamName+","+teamKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String owner = snapshot.getValue(String.class);
                    if(myUid.equals(owner)){
                        newMembers.setVisibility(View.VISIBLE);
                        addListBtn.setVisibility(View.VISIBLE);
                        addBtn.setVisibility(View.GONE);
                        heading.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);


                    }
                    else {
                        Toast.makeText(TeamDetailActivity.this, "You're not an admin", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        });

        addListBtn.setOnClickListener(v -> {
String members = newMembers.getText().toString();

            if (members.charAt(members.length()-1)==','){
                members = members.replace(members.substring(members.length()-1), "");
            }

            if(members.length()<1){
                newMembers.setError("Add at least one member");
                YoYo.with(Techniques.RubberBand).duration(700).playOn(newMembers);
                return;
            }
            pd.show();
            listOfAttendees = members.split(",",2000);

            current=0;
            addMember();
            Toast.makeText(TeamDetailActivity.this, "New Member(s) Added Successfully", Toast.LENGTH_SHORT).show();
            newMembers.setVisibility(View.GONE);
            addListBtn.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);


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
                    forUser.getReference("Users").child(key).child("Teams").child(teamName+","+teamKey).setValue(auth.getUid()).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            FirebaseDatabase forTeams = FirebaseDatabase.getInstance();
                            forTeams.getReference("Teams").child(teamKey).child(listOfAttendees[current].trim()).setValue(teamName+","+myUid).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()) {
                                    current++;
                                    if(current<(listOfAttendees.length)){
                                        addMember();
                                    }

                                }else{
                                    Toast.makeText(TeamDetailActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(TeamDetailActivity.this, "Error Occurred:"+ Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }

                            });
                        }else{
                            Toast.makeText(TeamDetailActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(TeamDetailActivity.this, "Error Occurred:"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(TeamDetailActivity.this, listOfAttendees[current]+" Does not exist", Toast.LENGTH_SHORT).show();

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
           newMembers.setVisibility(View.GONE);
            addListBtn.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}