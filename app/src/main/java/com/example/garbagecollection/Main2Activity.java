package com.example.garbagecollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Main2Activity extends AppCompatActivity {
    FirebaseStorage storage;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ImageView imageViewT;
    Bitmap bitmap;
    StorageReference mountainImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("UPLOAD","on create");
//        fAuth=FirebaseAuth.getInstance();
        Log.d("UPLOAD","auth instance");
//        fAuth.signInWithEmailAndPassword("test001@gmail.com","12345678").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                Log.d("UPLOAD","Email Sent for verification");
//                user=fAuth.getCurrentUser();
//                user.sendEmailVerification();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("UPLOAD",e.getMessage());
//
//            }
//        }).addOnCanceledListener(new OnCanceledListener() {
//            @Override
//            public void onCanceled() {
//                Log.d("UPLOAD","Cancelled");
//            }
//        });

        imageViewT = (ImageView) findViewById(R.id.imageViewT);
        // TODO Capture Image and Add to Image View to later receive the bitmap from it
        StorageReference storageRef = storage.getInstance().getReference();
        mountainImagesRef = storageRef.child("test/mountains.jpg");
        bitmap = ((BitmapDrawable) imageViewT.getDrawable()).getBitmap();
    }
    public void capture(View v){

    }

    @Override
    protected void onStart() {
        super.onStart();

        AsyncTask at = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.d("UPLOAD", "START");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                Log.d("UPLOAD", "DATA BYTES COMPUTED");

                UploadTask uploadTask = mountainImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(Main2Activity.this, "Failed to upload image", Toast.LENGTH_LONG).show();
                        Log.d("UPLOAD", "FAILED");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Main2Activity.this, "Success to upload image", Toast.LENGTH_LONG).show();
                        Log.d("UPLOAD", "SUCCESS");
                    }
                });
                return null;
            }
        };
        at.execute();
    }
}
