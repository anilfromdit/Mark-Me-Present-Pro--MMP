package fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.afd.mmp_pro.AboutActivity;
import com.afd.mmp_pro.Bug_Report_Activity;
import com.afd.mmp_pro.FeedbackActivity;
import com.afd.mmp_pro.LoginActivity;
import com.afd.mmp_pro.R;
import com.afd.mmp_pro.ThemesActivity;
import com.google.firebase.auth.FirebaseAuth;


public class AppSettingFragment extends Fragment {
    TextView logout,themes,aboutUs,feedback,bug;
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
         View view=  inflater.inflate(R.layout.fragment_app_setting, container, false);
logout = view.findViewById(R.id.logout);
themes = view.findViewById(R.id.themes);
aboutUs = view.findViewById(R.id.aboutUs);
feedback = view.findViewById(R.id.feedback);
bug = view.findViewById(R.id.reportBug);
auth = FirebaseAuth.getInstance();
bug.setOnClickListener(v -> {
    Intent intent = new Intent(getActivity(), Bug_Report_Activity.class);
    startActivity(intent);
});
themes.setOnClickListener(v -> {
    Intent intent = new Intent(getActivity(), ThemesActivity.class);
    startActivity(intent);
});

aboutUs.setOnClickListener(v -> {
    Intent intent = new Intent(getActivity(), AboutActivity.class);
    startActivity(intent);
});

logout.setOnClickListener(v -> {



    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    builder.setTitle("Confirm");
    builder.setMessage("Hit confirm to logout");
    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            getActivity().finish();
        }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });
    builder.show();

});

feedback.setOnClickListener(v -> {
Intent intent = new Intent(getActivity(), FeedbackActivity.class);
startActivity(intent);
});
    return view;

    }

}