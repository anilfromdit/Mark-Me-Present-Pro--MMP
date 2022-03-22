package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
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
import Adapters.ReportsAdapter;
import Models.ReportsModel;

public class ReportsFragment extends Fragment {
    FirebaseAuth auth;
    ProgressDialog pd;
    private RecyclerView recyclerView;
    ReportsAdapter reportsAdapter;
    List<ReportsModel> mreport;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.newTeam).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reports");

        auth = FirebaseAuth.getInstance();
        textView = view.findViewById(R.id.noReport);
        mreport = new ArrayList<>();
        pd = new ProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.reportRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportsAdapter = new ReportsAdapter(getContext(),mreport);
        recyclerView.setAdapter(reportsAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(textView.getVisibility()==View.VISIBLE){
                        textView.setVisibility(View.GONE);
                    }
                    mreport.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String key = child.getKey();
                        add(key);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    void add(String key){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Reports").child(auth.getUid()).child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReportsModel reModel= snapshot.getValue(ReportsModel.class);
                mreport.add(reModel);
                reportsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("MMP-Pro");    }
}