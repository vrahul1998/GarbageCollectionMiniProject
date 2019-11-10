package com.example.garbagecollection.Employee;

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

import com.example.garbagecollection.Company.Company;
import com.example.garbagecollection.Company.Employee;
import com.example.garbagecollection.MainActivity;
import com.example.garbagecollection.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class EmpProfile extends Fragment {

    View empProfile;
    Button signout;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase fDatabase;
    DatabaseReference fRef;
    TextView name,email,company,age;
    ImageView picture;
    Employee employee;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        empProfile=inflater.inflate(R.layout.fragment_emp_profile,container,false);
        fAuth= FirebaseAuth.getInstance();
        fUser=fAuth.getCurrentUser();
        fDatabase=FirebaseDatabase.getInstance();
        fRef=fDatabase.getReference().child("Employees");
        signout=empProfile.findViewById(R.id.empSignOut);
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
        return empProfile;
    }
    @Override
    public void onStart(){
        super.onStart();
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employee=dataSnapshot.child(fUser.getUid()).getValue(Employee.class);
                if(employee!=null)
                {
                    name=empProfile.findViewById(R.id.empName);
                    email=empProfile.findViewById(R.id.empEmail);
                    age=empProfile.findViewById(R.id.empAge);
                    company=empProfile.findViewById(R.id.empCompany);
                    picture=empProfile.findViewById(R.id.empImage);
                    name.setText(employee.getName());
                    email.setText(employee.getEmail());
                    age.setText(employee.getAge());
                    company.setText(employee.getCompany());
                    Picasso.with(getContext()).load(employee.getImage()).placeholder(R.drawable.default_avatar).into(picture);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
