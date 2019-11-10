package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.garbagecollection.Employee.EmpActiveActivity;
import com.example.garbagecollection.Employee.EmployeeHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapGarbage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    View map;
    String id;
    Garbage garbage;
    Double latitude=0.0;
    Double longitude=0.0;
    Button cancel,accept;
    public static FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_garbage);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("UserActivity");
        databaseReference1=firebaseDatabase.getReference().child("Employees");
        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("id");
        latitude=bundle.getDouble("latitude");
        longitude=bundle.getDouble("longitude");
        Toast.makeText(MapGarbage.this,id,Toast.LENGTH_LONG).show();
        cancel=findViewById(R.id.cancel);
        accept=findViewById(R.id.accept);
        fragmentManager =getSupportFragmentManager();



    }
    @Override
    public void onStart(){
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                garbage=dataSnapshot.child(id).getValue(Garbage.class);
                latitude=garbage.getLatitude();
                longitude=garbage.getLongitude();
                Toast.makeText(MapGarbage.this,latitude+" "+longitude,Toast.LENGTH_LONG).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapGarbage.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent;
//                intent = new Intent(MapGarbage.this, EmployeeHome.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                garbage.setStatus("Accepted");
                garbage.setAcceptedEmail(mUser.getEmail());
                databaseReference.child(id).setValue(garbage).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        databaseReference1.child(mUser.getUid()).child("activity").setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                databaseReference1.child(mUser.getUid()).child("status").setValue("Active").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MapGarbage.this,"Activity started",Toast.LENGTH_LONG).show();
//
//                                    Intent intent;
//                                    intent = new Intent(MapGarbage.this, EmployeeHome.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
                                            finish();
                                        }
                                        else Toast.makeText(MapGarbage.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else
                                Toast.makeText(MapGarbage.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(MapGarbage.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                    }
                });



            }
        });
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}
