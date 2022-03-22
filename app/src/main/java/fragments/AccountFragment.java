package fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.afd.mmp_pro.MainActivity;
import com.afd.mmp_pro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;
import java.util.Objects;

public class AccountFragment extends Fragment {

    TextView name,email,sap,rollNo,course,gender;
    Button editProfile;
    FirebaseAuth auth;

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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Accounts");

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        auth = FirebaseAuth.getInstance();
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        sap = view.findViewById(R.id.sap);
        rollNo = view.findViewById(R.id.rollno);
        course = view.findViewById(R.id.course);
        gender = view.findViewById(R.id.gender);
        editProfile = view.findViewById(R.id.editProfileButton);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
if(snapshot != null){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    name.setText(map.get("Name"));
                    email.setText(map.get("Email"));
                    course.setText(map.get("Course"));
                    sap.setText(map.get("Sap"));
                    gender.setText(map.get("Gender"));
                    rollNo.setText(map.get("Rollno"));
                }
else{
    Toast.makeText(getActivity(), "Some Error Occurred ", Toast.LENGTH_SHORT).show();
}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle("MMP-Pro");    }
}