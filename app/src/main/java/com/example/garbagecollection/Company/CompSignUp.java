package com.example.garbagecollection.Company;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.garbagecollection.MainActivity;
import com.example.garbagecollection.R;
import com.example.garbagecollection.SignUp;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class CompSignUp extends AppCompatActivity {
    ImageView compImg;
    EditText compName,compPhone,compDesc;
    Button compRegister;

    public static final int CAMERA_REQUEST_CODE=1;
    String imageFileName;
    byte[] bytesData;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDbase;
    private DatabaseReference mRef;
    private StorageReference compPic;
    private StorageReference mStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_sign_up);
        compImg=findViewById(R.id.compImg);
        compName=findViewById(R.id.compName);
        compDesc=findViewById(R.id.compDesc);
        compPhone=findViewById(R.id.compPhone);
        compRegister=findViewById(R.id.compRegButton);

        mAuth = FirebaseAuth.getInstance();
        mDbase = FirebaseDatabase.getInstance();
        mRef=mDbase.getReference().child("Companies");
        mStore = FirebaseStorage.getInstance().getReference();
        mUser=mAuth.getCurrentUser();

        compImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
                }

            }

        });

        compRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!compName.getText().toString().isEmpty() && !compDesc.getText().toString().isEmpty() && !compPhone.getText().toString().isEmpty() && bytesData!=null){
                    mRef=mDbase.getReference().child("Companies");
                    try{
                        mStore=FirebaseStorage.getInstance().getReference();
                        compPic=mStore.child("CompanyImage").child(imageFileName);
                        UploadTask ut=compPic.putBytes(bytesData);
                        Task<Uri> uriTask=ut.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                return compPic.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri=task.getResult();
                                final Company company=new Company(compName.getText().toString(),compDesc.getText().toString(),mUser.getUid(),compPhone.getText().toString(),uri.toString());
                                mRef.child(mUser.getUid()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(CompSignUp.this, "successful", Toast.LENGTH_LONG).show();
                                            Intent intent=new Intent(CompSignUp.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            CompSignUp.this.finish();



                                        } else
                                            Toast.makeText(CompSignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }catch (Exception e) {
                        Toast.makeText(CompSignUp.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            compImg.setImageBitmap(imageBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            bytesData = baos.toByteArray();

            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            imageFileName = "JPEG"+timeStamp+".jpg";
        }}


}
