package com.example.garbagecollection.Employee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagecollection.Company.Employee;
import com.example.garbagecollection.FirstPage;
import com.example.garbagecollection.Garbage;
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
import com.squareup.picasso.Picasso;


public class EmpActiveActivity extends Fragment {
    View empActiveView;
     String active;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase fDatabase;
    DatabaseReference fRef;
    DatabaseReference fRef2;
    TextView requestedBy,acceptedBy,desc,wasteType,title;
    ImageView picture;
    Button updateActiveActivity,cancelActiveActivity;

    Garbage garbage;
    ConstraintLayout activeActivityLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        empActiveView=inflater.inflate(R.layout.fragment_emp_active,container,false);
        fAuth= FirebaseAuth.getInstance();
        activeActivityLayout=empActiveView.findViewById(R.id.activeActivityLayout);
        requestedBy = empActiveView.findViewById(R.id.aRequestedBy);
        acceptedBy = empActiveView.findViewById(R.id.aAcceptedBy);
        desc = empActiveView.findViewById(R.id.aDesc);
        wasteType = empActiveView.findViewById(R.id.aWasteType);
        title = empActiveView.findViewById(R.id.aTitle);
        picture = empActiveView.findViewById(R.id.aImage);
        cancelActiveActivity=empActiveView.findViewById(R.id.cancelActiveActivity);
        updateActiveActivity=empActiveView.findViewById(R.id.updateActiveActivity);
        activeActivityLayout.setVisibility(View.INVISIBLE);
        fUser=fAuth.getCurrentUser();
        fDatabase=FirebaseDatabase.getInstance();
        fRef=fDatabase.getReference().child("Employees");
        fRef2=fDatabase.getReference().child("UserActivity");
        return empActiveView;
    }

    @Override
    public void onStart(){
        super.onStart();
        activeActivityLayout.setVisibility(View.INVISIBLE);
        final Employee[] emp = {new Employee()};
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 emp[0] =dataSnapshot.child(fUser.getUid()).getValue(Employee.class);
//                activeActivityLayout.setVisibility(View.VISIBLE);
                fRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        garbage=null;

                        if (!emp[0].getActivity().equals("noActiveActivity")) {
                            garbage = dataSnapshot.child(emp[0].getActivity()).getValue(Garbage.class);

                            activeActivityLayout.setVisibility(View.VISIBLE);
                            active=garbage.getGid();
                            requestedBy.setText(garbage.getEmail());
                            acceptedBy.setText(garbage.getAcceptedEmail());
                            desc.setText(garbage.getDesc());
                            wasteType.setText(garbage.getWasteType());
                            title.setText(garbage.getTitle());
                            Picasso.with(getContext()).load(garbage.getImgName()).placeholder(R.drawable.default_avatar).into(picture);
                        }
                        else
                        {
                            activeActivityLayout.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getContext(),active,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getContext(),"actvity status:"+active,Toast.LENGTH_LONG).show();


            updateActiveActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    garbage.setState(garbage.getEmail()+"_"+"completed");
                    garbage.setStatus("completed");
                    ProgressDialog progressDialog;
                    progressDialog=new ProgressDialog(getContext());
                    progressDialog.setMessage(garbage.getGid()+"-"+garbage.getEmail());
                    progressDialog.show();
                    fRef2.child(garbage.getGid()).setValue(garbage).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                emp[0].setActivity("noActiveActivity");
                                emp[0].setStatus("Inactive");
                                fRef.child(fUser.getUid()).child("status").setValue("Inactive").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            fRef.child(fUser.getUid()).child("activity").setValue("noActiveActivity").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful())
                                                   {
                                                       activeActivityLayout.setVisibility(View.INVISIBLE);
                                                       FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                       ft.detach(EmpActiveActivity.this).attach(EmpActiveActivity.this).commit();
                                                   }
                                                }
                                            });

                                        }
                                        else
                                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

                }
            });
            cancelActiveActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    emp[0].setActivity("noActiveActivity");
                    emp[0].setStatus("Inactive");
                    garbage.setStatus("Request Sent");
                    garbage.setAcceptedEmail("");
                    ProgressDialog progressDialog;
                    progressDialog=new ProgressDialog(getContext());
                    progressDialog.setMessage(garbage.getGid()+"-"+garbage.getEmail());
                    progressDialog.show();
                    Toast.makeText(getContext(),active,Toast.LENGTH_LONG).show();
                    fRef2.child(garbage.getGid()).setValue(garbage).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                fRef.child(fUser.getUid()).setValue(emp[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getContext(), "cancelled", Toast.LENGTH_LONG).show();
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(EmpActiveActivity.this).attach(EmpActiveActivity.this).commit();
                                        }
                                        else
                                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

                }
            });

    }
}
