package com.example.garbagecollection.Employee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagecollection.Company.CompanyEmployees;
import com.example.garbagecollection.Company.Employee;
import com.example.garbagecollection.MapGarbage;
import com.example.garbagecollection.R;
import com.example.garbagecollection.Garbage;
import com.example.garbagecollection.User.UserActivities;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class EmpAvailableActivities extends Fragment {
    RecyclerView empAvailableActivitiesRecycleView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    View empAvailableActivitiesView;

    DatabaseReference fRef;
    DatabaseReference uRef;
    DatabaseReference uRef2;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseRecyclerOptions<Garbage> options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fAuth=FirebaseAuth.getInstance();
        fUser=fAuth.getCurrentUser();

        empAvailableActivitiesView=inflater.inflate(R.layout.fragment_emp_available_activities,container,false);
        empAvailableActivitiesRecycleView=empAvailableActivitiesView.findViewById(R.id.empAvaliableActivitiesRecycleView);

        empAvailableActivitiesRecycleView.setHasFixedSize(true);
        empAvailableActivitiesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        uRef=FirebaseDatabase.getInstance().getReference().child("UserActivity");
        fRef= FirebaseDatabase.getInstance().getReference().child("UserActivity");
        uRef2=FirebaseDatabase.getInstance().getReference().child("Employees");
        Query query=FirebaseDatabase.getInstance().getReference().child("UserActivity").orderByChild("status").equalTo("Request Sent");
        options=new FirebaseRecyclerOptions.Builder<Garbage>().setQuery(query,Garbage.class).build();

        //TODO: Adapter incomplete
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Garbage,EmpAvaliableActivitiesViewHolder>(options) {
            @NonNull
            @Override
            public EmpAvaliableActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.garbage_list_layout,parent,false);

                return new EmpAvaliableActivitiesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EmpAvaliableActivitiesViewHolder empAvaliableActivitiesViewHolder, int i, @NonNull final Garbage garbage) {
                empAvaliableActivitiesViewHolder.setTitle(garbage.getTitle());
                empAvaliableActivitiesViewHolder.setRequestedBy(garbage.getEmail());
                empAvaliableActivitiesViewHolder.setThumbNail(garbage.getImgName(),getContext());
                empAvaliableActivitiesViewHolder.setOnClickListener(new EmpAvaliableActivitiesViewHolder.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(getContext(), MapGarbage.class);
                        Bundle data=new Bundle();
                        data.putString("id",garbage.getGid());
                        data.putDouble("latitude",garbage.getLatitude());
                        data.putDouble("longitude",garbage.getLongitude());
                        intent.putExtras(data);
                        startActivity(intent);

//                       uRef.child(garbage.getGid()).child("status").setValue("Accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
//                           @Override
//                           public void onComplete(@NonNull Task<Void> task) {
//                               if(task.isSuccessful()){
//                                   Toast.makeText(getContext(),"status updated",Toast.LENGTH_LONG).show();
//                               }
//                               else
//                                   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                           }
//                       });
//                       uRef.child(garbage.getGid()).child("acceptedEmail").setValue(fUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                           @Override
//                           public void onComplete(@NonNull Task<Void> task) {
//                               if(task.isSuccessful()){
//                                   Toast.makeText(getContext(),"email updated",Toast.LENGTH_LONG).show();
//                               }
//                               else
//                                   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                           }
//                       });
//                       uRef2.child(fUser.getUid()).child("activity").setValue(garbage.getGid()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                           @Override
//                           public void onComplete(@NonNull Task<Void> task) {
//                               if(task.isSuccessful()){
//                                   Toast.makeText(getContext(),"Activity selected",Toast.LENGTH_LONG).show();
//                               }
//                               else
//                                   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                           }
//                       });
//                       uRef2.child(fUser.getUid()).child("status").setValue("Active");
                    }
                });
            }
        };

        empAvailableActivitiesRecycleView.setAdapter(firebaseRecyclerAdapter);

        // Inflate the layout for this fragment
        return empAvailableActivitiesView;
    }
    @Override
    public void onStart(){
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }


    private static class EmpAvaliableActivitiesViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public EmpAvaliableActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
        public void setThumbNail(String imgName, Context context) {
            ImageView img=mView.findViewById(R.id.img);
            Picasso.with(context).load(imgName).into(img);
        }
        public void setTitle(String name){
            TextView mTitle=mView.findViewById(R.id.title);
            mTitle.setText(name);

        }
        public void setRequestedBy(String userEmail){
            TextView email=mView.findViewById(R.id.location);
            email.setText(userEmail);
        }
        private EmpAvaliableActivitiesViewHolder.ClickListener mClickListener;



        public interface ClickListener{
            public void onItemClick(View view,int position);
        }

        public void setOnClickListener(EmpAvaliableActivitiesViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }
    }
}
