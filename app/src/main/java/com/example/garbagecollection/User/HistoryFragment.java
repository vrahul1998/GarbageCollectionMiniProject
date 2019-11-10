package com.example.garbagecollection.User;

import android.content.Context;
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

import com.example.garbagecollection.Garbage;
import com.example.garbagecollection.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class HistoryFragment extends Fragment {
    //Recycle View variables
    RecyclerView historyRecycleView;
    View HistoryView;

    //Firebase variables ui
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseRecyclerOptions options;

    //Firebase database
    private DatabaseReference mRef;

    //Firebase Auth
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //Firebase auth
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        //recycle view inflator
        HistoryView=inflater.inflate(R.layout.fragment_history_activity,container,false);
        historyRecycleView=HistoryView.findViewById(R.id.garbageHistoryRecycleView);

        historyRecycleView.setHasFixedSize(true);
        historyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Firebase database
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("UserActivity").orderByChild("email").equalTo(mUser.getEmail());
        FirebaseRecyclerOptions<Garbage> options =new FirebaseRecyclerOptions.Builder<Garbage>().setQuery(query,Garbage.class).build();

        //firebase adapter
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Garbage,HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder viewHolder, int i, @NonNull final Garbage garbage) {
                viewHolder.setTitle(garbage.getTitle());
                viewHolder.setThumbNail(garbage.getImgName(),getContext());
            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.garbage_list_layout,parent,false);

                return new HistoryViewHolder(view);

            }
        };
        historyRecycleView.setAdapter(firebaseRecyclerAdapter);

        // Inflate the layout for this fragment
        return HistoryView;
    }
    @Override
    public void onStart(){
        super.onStart();

        //Firebase recycle adapter

        firebaseRecyclerAdapter.startListening();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView=itemView;
        }

        public void setTitle(String title) {
            TextView mTitle=mItemView.findViewById(R.id.title);
            mTitle.setText(title);
        }

        public void setThumbNail(String imgName, Context context) {
            ImageView activityImg=mItemView.findViewById(R.id.img);
            Picasso.with(context).load(imgName).into(activityImg);
        }
    }
}
