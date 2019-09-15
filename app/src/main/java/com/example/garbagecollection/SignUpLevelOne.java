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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpLevelOne extends AppCompatActivity {
    EditText email,password,confirmPassword;
    RadioGroup rg;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button verify,next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_level_one);
        email=findViewById(R.id.signUpemail);
        password=findViewById(R.id.signUpPassword);
        confirmPassword=findViewById(R.id.signUpConfirmPassword);
        verify=findViewById(R.id.verifyButton);
        next=findViewById(R.id.NextSignUp);
        next.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

    }
    public void verifyOnClick(View view){

            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !confirmPassword.getText().toString().isEmpty()) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mUser=mAuth.getCurrentUser();
                                    mUser.sendEmailVerification();
                                    sendVerifyAlert();
                                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString());
                                    verify.setVisibility(View.GONE);
                                    next.setVisibility(View.VISIBLE);

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

    public void sendVerifyAlert(){
        final AlertDialog.Builder alert=new AlertDialog.Builder(SignUpLevelOne.this);
        final View mView=getLayoutInflater().inflate(R.layout.sign_up_verify_alert,null);
        final Button okButton=mView.findViewById(R.id.positiveButton);
        alert.setView(mView);
        final AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });
    }
    public void NextSignup(View view)
    {
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString());
        mUser=mAuth.getCurrentUser();
        if(mUser.isEmailVerified()){
            Intent intent=new Intent(SignUpLevelOne.this,SignUp.class);
            startActivity(intent);
        }
        else{
            sendVerifyAlert();
        }
    }
}
