package com.example.garbagecollection.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garbagecollection.Garbage;
import com.example.garbagecollection.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserActivities extends Fragment {

RecyclerView garbageRecycleView;
List<Garbage>garbageList;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

public Toolbar mToolBar;
View garbageView;
FloatingActionButton addUser;
private DatabaseReference mRef;
FirebaseAuth mAuth;
FirebaseUser mUser;
FirebaseRecyclerOptions<Garbage> options;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        // Inflate the layout for this fragment
       // Garbage e=new Garbage("test","test","test",100);
        garbageView=inflater.inflate(R.layout.fragment_user_activities, container, false);
        garbageRecycleView=garbageView.findViewById(R.id.garbageHistoryRecycleView);
//        garbageList=new ArrayList<>();
          addUser=garbageView.findViewById(R.id.addUser);
        //recycle view
        garbageRecycleView.setHasFixedSize(true);
        garbageRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRef= FirebaseDatabase.getInstance().getReference().child("CitizenUsers");
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("UserActivity").orderByChild("email").equalTo(mUser.getEmail()).limitToFirst(10);
         options =new FirebaseRecyclerOptions.Builder<Garbage>().setQuery(query,Garbage.class).build();



            addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddUserActivityEntry.class);
                startActivity(intent);
            }
        });

         firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Garbage, GarbageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GarbageViewHolder garbageViewHolder, int i, @NonNull final Garbage garbage) {
                garbageViewHolder.setTitle(garbage.getTitle());
                garbageViewHolder.setColor(garbage.getStatus());
                garbageViewHolder.setThumbNail(garbage.getImgName(),getContext());
                garbageViewHolder.setOnClickListener(new GarbageViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(),GarbageItemView.class);
                        Bundle bundle=new Bundle();

                        bundle.putString("title",garbage.getTitle());
                        bundle.putString("email",garbage.getEmail());
                        bundle.putString("Desc",garbage.getDesc());
                        bundle.putString("imgName",garbage.getImgName());
                        bundle.putString("wasteType",garbage.getWasteType());
                        bundle.putString("latitude",Double.toString(garbage.getLatitude()));
                        bundle.putString("longitude",Double.toString(garbage.getLongitude()));
                        bundle.putString("status",garbage.getStatus());
                        bundle.putString("acceptedBy",garbage.getAcceptedEmail());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public GarbageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.garbage_list_layout,parent,false);

                return new GarbageViewHolder(view); }};
        garbageRecycleView.setAdapter(firebaseRecyclerAdapter);
        garbageRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return garbageView;
    }
    @Override
    public void onStart(){
        super.onStart();

        //Firebase recycle adapter

        firebaseRecyclerAdapter.startListening();
    }

    public static class GarbageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public GarbageViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view,getAdapterPosition());
                }
            });

        }
        private GarbageViewHolder.ClickListener mClickListener;
        public interface ClickListener{
            public void onItemClick(View view,int position);

        }
            public void setOnClickListener(GarbageViewHolder.ClickListener clickListener){
                mClickListener=clickListener;
        }
        public void setColor(String status)
        {
            ConstraintLayout layout=mView.findViewById(R.id.rLayout);
            layout.setBackgroundColor(Color.BLUE);
        }

        public void setTitle(String Title){
            TextView mTitle=mView.findViewById(R.id.title);
            mTitle.setText(Title);
        }

        public void setThumbNail(String imgName, Context context) {
            ImageView activityImg=mView.findViewById(R.id.img);
            Picasso.with(context).load(imgName).into(activityImg);
        }}
}
