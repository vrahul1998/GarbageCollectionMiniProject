package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.garbagecollection.Company.CompanyHome;
import com.example.garbagecollection.Employee.EmployeeHome;
import com.example.garbagecollection.User.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstPage extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    String [] Permissions={Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    Button letsgo;
    int Permission_All=1;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        letsgo=findViewById(R.id.LetsGo);

        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseReference=database.getReference().child("AccountType");
        progressDialog=new ProgressDialog(FirstPage.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);




        if(!hasPermissions(this, Permissions)) {

            ActivityCompat.requestPermissions(this, Permissions, Permission_All);

        }

    }
    @Override
    public void onStart(){
        super.onStart();
        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(FirstPage.this, Permissions)) {

                    ActivityCompat.requestPermissions(FirstPage.this, Permissions, Permission_All);

                }
                else {
                    progressDialog.show();
                    if(mUser != null){

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String type =dataSnapshot.child(mUser.getUid()).child("type").getValue(String.class);
                                progressDialog.dismiss();
                                switch (type){
                                    case "user":
                                        intent=new Intent(FirstPage.this, Home.class);//MapTest.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        FirstPage.this.finish();
                                        break;
                                    case "employee":
                                        intent=new Intent(FirstPage.this, EmployeeHome.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        FirstPage.this.finish();
                                        break;
                                    case "company":
                                        intent=new Intent(FirstPage.this, CompanyHome.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        FirstPage.this.finish();
                                        break;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(FirstPage.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });





                    }else
                    {
                        intent=new Intent(FirstPage.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        FirstPage.this.finish();
                    }

                }
            }
        });

    }
    public static boolean hasPermissions(Context context, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
}
