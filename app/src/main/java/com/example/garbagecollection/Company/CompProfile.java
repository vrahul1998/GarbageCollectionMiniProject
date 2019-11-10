package com.example.garbagecollection.Company;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbagecollection.MainActivity;
import com.example.garbagecollection.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class CompProfile extends Fragment {
    View compProfile;
    Button signout;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase fDatabase;
    DatabaseReference fRef;
    TextView desc,name,phone,email;
    ImageView picture;
    Company company;
    StorageReference compImg;
    StorageReference pic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        compProfile=inflater.inflate(R.layout.fragment_comp_profile,container,false);
        fAuth= FirebaseAuth.getInstance();
        fUser=fAuth.getCurrentUser();
        fDatabase=FirebaseDatabase.getInstance();
        fRef=fDatabase.getReference().child("Companies");
        signout=compProfile.findViewById(R.id.compSignOut);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                Intent intent;
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return compProfile;
    }

    @Override
    public void onStart(){
        super.onStart();
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                company=dataSnapshot.child(fUser.getUid()).getValue(Company.class);
                if(company!=null)
                {
                    name=compProfile.findViewById(R.id.compName);
                    email=compProfile.findViewById(R.id.compEmail);
                    desc=compProfile.findViewById(R.id.compDesc);
                    phone=compProfile.findViewById(R.id.compPhone);
                    picture=compProfile.findViewById(R.id.compImage);
                    name.setText(company.getName());
                    phone.setText(company.getPhone());
                    desc.setText(company.getDesc());
                    email.setText(fUser.getEmail());
                    Picasso.with(getContext()).load(company.getImage()).placeholder(R.drawable.default_avatar).into(picture);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
