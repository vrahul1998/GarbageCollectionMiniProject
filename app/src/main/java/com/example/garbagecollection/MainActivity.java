package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.garbagecollection.Company.CompanyHome;
import com.example.garbagecollection.Employee.EmployeeHome;
import com.example.garbagecollection.User.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText username;
    EditText password;

    DatabaseReference databaseReference;
    Intent intent;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mAuth= FirebaseAuth.getInstance();
//       mAuth.signOut();
//        mUser=mAuth.getCurrentUser();
//
//
//        if(mUser != null){
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//               @Override
//               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               String type =dataSnapshot.child(mUser.getUid()).child("type").getValue(String.class);
//                   switch (type){
//                       case "user":
//                           intent=new Intent(MainActivity.this,Home.class);//MapTest.class);
//                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                           startActivity(intent);
//                           MainActivity.this.finish();
//                           break;
//                       case "employee":
//                           intent=new Intent(MainActivity.this, EmployeeHome.class);
//                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                           startActivity(intent);
//                           MainActivity.this.finish();
//                           break;
//                       case "company":
//                           intent=new Intent(MainActivity.this, CompanyHome.class);
//                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                           startActivity(intent);
//                           MainActivity.this.finish();
//                           break;
//                   }
//               }
//
//               @Override
//               public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
//               }
//           });
//
//
//
//
//
//        }
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("AccountType");
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Getting your credentials");
       progressDialog.setCanceledOnTouchOutside(false);
        username = findViewById(R.id.regUsername);

        password = findViewById(R.id.regPassword);
    }

    public void signin(View v) {
        progressDialog.show();



        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()&& mAuth.getCurrentUser().isEmailVerified()){
                    mUser=mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this,"success",Toast.LENGTH_LONG).show();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            progressDialog.dismiss();
                            String type =dataSnapshot.child(mUser.getUid()).child("type").getValue(String.class);
                            switch (type){
                                case "user":
                                    intent=new Intent(MainActivity.this,Home.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                    break;
                                case "employee":
                                    intent=new Intent(MainActivity.this, EmployeeHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                    break;
                                case "company":
                                    intent=new Intent(MainActivity.this, CompanyHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                    break;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void signup(View v){
        Intent sig=new Intent(this,SignUpLevelOne.class);
        sig.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       //sig.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sig);
        MainActivity.this.finish();

    }
    @Override
    public void onBackPressed(){
        finish();
    }


}
