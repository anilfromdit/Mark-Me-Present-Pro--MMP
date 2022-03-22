package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.afd.mmp_pro.MainActivity;
import com.afd.mmp_pro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapters.TeamsAdapter;
import Models.TeamsModel;

public class TeamsFragment extends Fragment {
    FirebaseAuth auth;
    ProgressDialog pd;
    private RecyclerView recyclerView;
    TeamsAdapter teamsAdapter;
    List<TeamsModel> mteam;
    String mySap;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_teams, container, false);
    auth = FirebaseAuth.getInstance();
    textView = view.findViewById(R.id.noTeam);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("AttendanceList").child(auth.getUid());
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reference2.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    mteam = new ArrayList<>();
    pd = new ProgressDialog(getContext());
    pd.setTitle("Please Wait");
    pd.setMessage("Loading Your Teams");
    pd.show();


    recyclerView = view.findViewById(R.id.teamsRcv);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    teamsAdapter = new TeamsAdapter(getContext(),mteam);
    recyclerView.setAdapter(teamsAdapter);
        DatabaseReference mySapQuery = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("Sap");
        mySapQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mySap = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("Teams");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(textView.getVisibility()==View.VISIBLE){
                        textView.setVisibility(View.GONE);
                    }
                    mteam.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String name = child.getKey();
                        TeamsModel teamsModel = new TeamsModel(name);
                        mteam.add(teamsModel);
                        teamsAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pd.dismiss();
    return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle("MMP-Pro");    }


}
