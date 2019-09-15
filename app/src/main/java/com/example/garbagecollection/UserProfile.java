package com.example.garbagecollection;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserProfile extends Fragment {
    View profileView;
    Button signout;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView=inflater.inflate(R.layout.fragment_user_profile, container, false);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        signout=profileView.findViewById(R.id.ProfileSignOut);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent;
                intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return profileView;
    }




}
