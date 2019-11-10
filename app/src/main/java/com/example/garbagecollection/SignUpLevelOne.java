package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.garbagecollection.Company.CompSignUp;
import com.example.garbagecollection.Employee.EmpSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpLevelOne extends AppCompatActivity {
    EditText email,password,confirmPassword;
    RadioGroup rg;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button verify,next;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mRef;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_level_one);
        email=findViewById(R.id.signUpemail);
        password=findViewById(R.id.signUpPassword);
        confirmPassword=findViewById(R.id.signUpConfirmPassword);
        rg=findViewById(R.id.radioGroup);
        verify=findViewById(R.id.verifyButton);
        next=findViewById(R.id.NextSignUp);
        next.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mRef=database.getReference().child("AccountType");
    }
    @Override
    protected void onStart(){
        super.onStart();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="";
                switch(rg.getCheckedRadioButtonId()){
                    case R.id.userSelect:
                        type="user";
                        break;
                    case R.id.employeeSelect:
                        type="employee";
                        break;
                    case R.id.companySelect:
                        type="company";
                        break;
                }
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !confirmPassword.getText().toString().isEmpty() && !type.isEmpty()) {
                    if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mUser=mAuth.getCurrentUser();
                                    AccountType accountType=new AccountType(mUser.getEmail(),mUser.getUid(),type);
                                    mRef.child(mUser.getUid()).setValue(accountType);
                                    mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {

                                                verify.setVisibility(View.GONE);
                                                next.setVisibility(View.VISIBLE);
                                            }
                                            else
                                                Toast.makeText(SignUpLevelOne.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    sendVerifyAlert();
//                                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString());



                                } else
                                    Toast.makeText(SignUpLevelOne.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        confirmPassword.setError("Password missmatch!!Please re-enter");

                    }
                } else {
                    Toast.makeText(SignUpLevelOne.this, "Please fill in all the details", Toast.LENGTH_LONG).show();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                mAuth.signOut();
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent;
                            mUser=mAuth.getCurrentUser();
                            if(mUser.isEmailVerified()) {
                                switch (type) {
                                    case "user":
                                        intent = new Intent(SignUpLevelOne.this, SignUp.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        break;
                                    case "employee":
                                        intent = new Intent(SignUpLevelOne.this, EmpSignUp.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        break;
                                    case "company":
                                        intent = new Intent(SignUpLevelOne.this, CompSignUp.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        break;
                                }
                                SignUpLevelOne.this.finish();
                                Toast.makeText(SignUpLevelOne.this, type, Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(SignUpLevelOne.this,"not verified",Toast.LENGTH_LONG).show();
                        }
                    }
                });



                }
            }
        );
    }
    public void verifyOnClick(View view){

        }

    public void sendVerifyAlert(){
//        final AlertDialog.Builder alert=new AlertDialog.Builder(SignUpLevelOne.this);
//        final View mView=getLayoutInflater().inflate(R.layout.sign_up_verify_alert,null);
//        final Button okButton=mView.findViewById(R.id.positiveButton);
//        alert.setView(mView);
//        final AlertDialog alertDialog=alert.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//
//
//            }
//        });
    }
    public void NextSignup(View view)
    {

    }
}
