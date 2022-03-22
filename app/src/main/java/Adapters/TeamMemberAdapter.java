package Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.afd.mmp_pro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import Models.TeamsModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.ViewHolder> {
    private Context mcontext;
    private List<TeamsModel> mMembers;
    FirebaseAuth auth;

    public TeamMemberAdapter(Context mcontext,List<TeamsModel> member){
        this.mcontext = mcontext;
        this.mMembers = member;
    }

    @NonNull
    @Override
    public TeamMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_members_layout,parent,false);
        return new TeamMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberAdapter.ViewHolder holder, int position) {
        final TeamsModel member = mMembers.get(position);
        auth = FirebaseAuth.getInstance();
        String sap = member.getName();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("fID").child(sap);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    String uid = snapshot.getValue(String.class);
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Name");
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.getValue(String.class);
                            holder.name.setText(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else{
                    mMembers.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), mMembers.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.sap.setText(sap);



    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
TextView name,sap;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.memberName);
            sap = itemView.findViewById(R.id.memberID);
        }
    }
}
