package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDbase;
    private DatabaseReference mRef;
    private ImageView profilePic;
    private EditText age,dob,name,phone;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStore;
    String imageFileName;
    byte[] bytesData;
    ImageButton verify;
    private StorageReference profileStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=findViewById(R.id.Profilename);
        phone=findViewById(R.id.phoneNo);
        age=findViewById(R.id.Profileage);
        dob=findViewById(R.id.dob);
        mAuth = FirebaseAuth.getInstance();
        mDbase = FirebaseDatabase.getInstance();
        mRef=mDbase.getReference().child("CitizenUser");
        mStore = FirebaseStorage.getInstance().getReference();
        mUser=mAuth.getCurrentUser();
        profilePic = findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
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
            profilePic.setImageBitmap(imageBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bytesData = baos.toByteArray();

            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            imageFileName = "JPEG"+timeStamp+".jpg";
        }}



    public void registerUser(View v){
        if(!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty() && !dob.getText().toString().isEmpty() && !phone.getText().toString().isEmpty() && bytesData !=null)
        {
            mRef=mDbase.getReference().child("CitizenUsers");
            CitizenUser cUser=new CitizenUser(name.getText().toString(),age.getText().toString(),imageFileName,phone.getText().toString(),dob.getText().toString());
           try {

               mRef.child(mUser.getUid()).setValue(cUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(SignUp.this, "successful", Toast.LENGTH_LONG).show();
                       } else
                           Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
               });
               mStore = FirebaseStorage.getInstance().getReference();
               profileStore = mStore.child("profilepic").child(imageFileName);
               UploadTask ut = profileStore.putBytes(bytesData);
               ut.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Toast.makeText(SignUp.this, "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                   }
               });
               ut.addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(SignUp.this, "Failed to upload image", Toast.LENGTH_LONG).show();
                   }
               });
           }catch (Exception e){
               Log.d("INFO",e.getMessage());
           }
           }
    }
}