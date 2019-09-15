package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if(mUser != null){
            Intent in=new Intent(MainActivity.this,Home.class);
            startActivity(in);
        }
    }
    public void signin(View v) {
        username = findViewById(R.id.regUsername);

        password = findViewById(R.id.regPassword);
        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()&& mAuth.getCurrentUser().isEmailVerified()){
                    Toast.makeText(MainActivity.this,"success",Toast.LENGTH_LONG).show();
                    Intent home=new Intent(MainActivity.this,Home.class);
                    startActivity(home);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Invalid username/password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void signup(View v){
        Intent sig=new Intent(this,SignUpLevelOne.class);
        startActivity(sig);

    }
}
