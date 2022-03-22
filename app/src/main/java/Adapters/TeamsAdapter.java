package Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.afd.mmp_pro.IdAttendanceActivity;
import com.afd.mmp_pro.QrAttendanceActivity;
import com.afd.mmp_pro.R;
import com.afd.mmp_pro.TeamDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import Models.TeamsModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
private Context mcontext;
private  List <TeamsModel> mteam;
FirebaseAuth auth;
ProgressDialog pd;


    public TeamsAdapter(Context mcontext,List<TeamsModel> team){
        this.mcontext = mcontext;
        this.mteam = team;
    }
    @NonNull
    @Override
    public TeamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.teams_layout,parent,false);
        return new TeamsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsAdapter.ViewHolder holder, int position) {
final TeamsModel team = mteam.get(position);
pd = new ProgressDialog(mcontext.getApplicationContext());
auth = FirebaseAuth.getInstance();
String fullName = team.getName();
String [] name = fullName.split(",",2);
        holder.name.setText(name[0]);
        holder.linearLayout.setVisibility(View.GONE);
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(holder.linearLayout.getVisibility()==View.GONE){
            holder.linearLayout.setVisibility(View.VISIBLE);
        }
        else{
            holder.linearLayout.setVisibility(View.GONE);
        }
    }
});


        holder.generateQr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("Users").child(auth.getUid()).child("Teams").child(team.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if((auth.getUid()).equals(snapshot.getValue())){
                            Intent intent = new Intent(mcontext.getApplicationContext(), QrAttendanceActivity.class);
                            String [] nameK = fullName.split(",",2);
                            intent.putExtra("name",nameK[0]);
                            intent.putExtra("key",nameK[1]);
                            mcontext.startActivity(intent);
                        }
                        else {
                            Toast.makeText(mcontext.getApplicationContext(), "You're not an admin", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        holder.generateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("Users").child(auth.getUid()).child("Teams").child(team.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if((auth.getUid()).equals(snapshot.getValue())){
Intent intent = new Intent(mcontext.getApplicationContext(), IdAttendanceActivity.class);
                            String [] nameK = fullName.split(",",2);
                            intent.putExtra("name",nameK[0]);
                            intent.putExtra("key",nameK[1]);
mcontext.startActivity(intent);

                        }
                        else {
                            Toast.makeText(mcontext.getApplicationContext(), "You're not an admin", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] nameK = fullName.split(",",2);
                Intent intent = new Intent(mcontext.getApplicationContext(), TeamDetailActivity.class);
                intent.putExtra("name",nameK[0]);
                intent.putExtra("key",nameK[1]);
                mcontext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {

            return mteam.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{
CircleImageView profileImage;
TextView name;
LinearLayout linearLayout;
Button generateQr,generateId,more;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.icon);
        name = itemView.findViewById(R.id.teamName);
        linearLayout = itemView.findViewById(R.id.linLay);
        generateQr = itemView.findViewById(R.id.genQR);
        generateId = itemView.findViewById(R.id.genID);
        more = itemView.findViewById(R.id.more);



    }
    }
}
