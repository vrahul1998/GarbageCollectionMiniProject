package com.example.garbagecollection.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagecollection.MainActivity;
import com.example.garbagecollection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;


public class UserProfile extends Fragment {
    View profileView;
    Button signout;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference fRef;
    TextView name,email,age,phone;
    ImageView profilepic;
    Bitmap my_image;
    CitizenUser profile;
    StorageReference profileImg;
    StorageReference pic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView=inflater.inflate(R.layout.fragment_user_profile, container, false);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        fDatabase=FirebaseDatabase.getInstance();
        fRef=fDatabase.getReference().child("CitizenUsers");
        Toast.makeText(getActivity(),mUser.getUid(),Toast.LENGTH_LONG).show();
        profileImg= FirebaseStorage.getInstance().getReferenceFromUrl("gs://garbagecollection-af8af.appspot.com/profilepic/");
          signout=profileView.findViewById(R.id.ProfileSignOut);
//        name=profileView.findViewById(R.id.Profilename);
//        age=profileView.findViewById(R.id.Profileage);
//        phone=profileView.findViewById(R.id.ProfileNumber);
//        email=profileView.findViewById(R.id.Profileemail);
//        name.setText("Name:"+profile.getName());
//        age.setText("Age:"+profile.getAge());
//        phone.setText("Phone:"+profile.getPhoneNo());
//        email.setText("Email:"+mUser.getEmail());
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent;
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return profileView;
    }
        @Override
    public void onStart(){
        super.onStart();

            fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile = dataSnapshot.child(mUser.getUid()).getValue(CitizenUser.class);
                if (profile != null) {
                    name = profileView.findViewById(R.id.Profilename);
                    age = profileView.findViewById(R.id.Profileage);
                    phone = profileView.findViewById(R.id.ProfileNumber);
                    email = profileView.findViewById(R.id.Profileemail);
                    profilepic=profileView.findViewById(R.id.ProfileImage);
                    name.setText("Name:" + profile.getName());
                    age.setText("Age:" + profile.getAge());
                    phone.setText("Phone:" + profile.getPhoneNo());
                    email.setText("Email:" + mUser.getEmail());
                    Toast.makeText(getContext(),profile.getImgsrc(),Toast.LENGTH_LONG).show();
                    Picasso.with(getContext()).load(profile.getImgsrc()).placeholder(R.drawable.default_avatar).into(profilepic);
//                    pic=profileImg.child(profile.getImgsrc());
//                    try {
//                        final File localFile = File.createTempFile("image","jpg");
//                        pic.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                                if (task.isSuccessful()) {
////                                    my_image= BitmapFactory.decodeFile(localFile.getAbsolutePath());
////                                    profilepic.setImageBitmap(my_image);
//                                    Picasso.with(getContext()).load(localFile).placeholder(R.drawable.default_avatar).into(profilepic);
//                                }
//                            }
//                        });
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}



}
