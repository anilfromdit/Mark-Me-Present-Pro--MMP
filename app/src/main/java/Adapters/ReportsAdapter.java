package Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import Models.ReportsModel;
import Models.TeamsModel;

public class ReportsAdapter  extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private Context mcontext;
    private List<ReportsModel> mreports;
    FirebaseAuth auth;
    String bSap,bName,aSap,aName,pSap,pName;

    public ReportsAdapter(Context mcontext,List<ReportsModel> report){
        this.mcontext = mcontext;
        this.mreports = report;
    }

    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_layout,parent,false);
        return new ReportsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();
        final ReportsModel model = mreports.get(position);
        bSap= model.getPrevious();
        bName = bSap;
        aSap = model.getNext();
        aName=aSap;
        pSap = model.getSap();
        pName = pSap;
        holder.className.setText(model.getLecture());
        holder.date_time.setText(model.getDate_time());
        holder.prev.setText(bName);
        holder.next.setText(aName);
        holder.proxy.setText(pName);

    if (!model.getPrevious().equals("Init")) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("fID").child(model.getPrevious());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String bUid = snapshot.getValue(String.class);
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(bUid);
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                            bSap = map.get("Sap");
                            holder.prev.setText(map.get("Name"));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    if (!model.getNext().equals("End")) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("fID").child(model.getNext());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String aUid = snapshot.getValue(String.class);
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(aUid);
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                            aSap = map.get("Sap");
                            holder.next.setText(map.get("Name"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(model.getUid());
    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Map<String, String> map = (Map<String, String>) snapshot.getValue();
            pSap = map.get("Sap");
            holder.proxy.setText(map.get("Name"));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });



        holder.before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext.getApplicationContext(), bSap, Toast.LENGTH_SHORT).show();
            }
        });

        holder.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext.getApplicationContext(), bSap, Toast.LENGTH_SHORT).show();
            }
        });


        holder.after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext.getApplicationContext(), aSap, Toast.LENGTH_SHORT).show();
            }
        });
        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext.getApplicationContext(), aSap, Toast.LENGTH_SHORT).show();
            }
        });

        holder.proxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext.getApplicationContext(), pSap, Toast.LENGTH_SHORT).show();
            }
        });
        holder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext.getApplicationContext(), pSap, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mreports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
TextView className,prev,next,proxy,date_time;
ImageView before,after,present;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            prev = itemView.findViewById(R.id.prevAttendee);
            next = itemView.findViewById(R.id.nextAttendee);
            proxy = itemView.findViewById(R.id.proxyAttendee);
            date_time = itemView.findViewById(R.id.date_time);
            before = itemView.findViewById(R.id.imageView);
            after = itemView.findViewById(R.id.imageView2);
            present = itemView.findViewById(R.id.imageView4);


        }
    }

}
