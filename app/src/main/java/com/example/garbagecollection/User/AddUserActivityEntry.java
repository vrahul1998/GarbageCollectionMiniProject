package com.example.garbagecollection.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagecollection.Garbage;
import com.example.garbagecollection.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class AddUserActivityEntry extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private ImageView garbageRequest;
    String timeStamp;
    String imageFileName;
    TextView latitude;
    TextView longitude;
    Location location;
    Button addRequest;
    FirebaseDatabase fBase;
    DatabaseReference fRef;
    TextView note;
    byte[] bytesData;
    private static final int CAMERA_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    RadioGroup wasteType;
   FirebaseUser fUser;
   FirebaseAuth mAuth;
   StorageReference mStore;
   StorageReference gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_entry);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        fBase=FirebaseDatabase.getInstance();
        fRef=fBase.getReference().child("ActivityRequests");
        wasteType=findViewById(R.id.wasteType);
        garbageRequest = findViewById(R.id.garbageRequest);
        note=findViewById(R.id.note);
        mAuth=FirebaseAuth.getInstance();
        fUser=mAuth.getCurrentUser();
        addRequest=findViewById(R.id.addrequest);
        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadRequest();
            }
        });
        garbageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
                }

            }

        });
    }


    private void uploadRequest(){
        if(bytesData!=null && !longitude.getText().toString().isEmpty() && !latitude.getText().toString().isEmpty() && wasteType.getCheckedRadioButtonId()!=-1 ){
           final String type;
            if(wasteType.getCheckedRadioButtonId()==R.id.drywaste)
                type="dry waste";
            else
                type="wet waste";
            try{
                mStore = FirebaseStorage.getInstance().getReference();
                gb = mStore.child("requests").child(imageFileName);
                UploadTask ut = gb.putBytes(bytesData);
                fRef=fBase.getReference().child("UserActivity");
                Task<Uri> urlTask =ut.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                            Toast.makeText(AddUserActivityEntry.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        return gb.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri=task.getResult();
                        Garbage newGarbage=new Garbage(fUser.getEmail()+"_"+"start","JPEG"+timeStamp,location.getLatitude(),location.getLongitude(),uri.toString(),type,fUser.getEmail().toString(),"","Request Sent",note.getText().toString(),fUser.getDisplayName()+" "+type+" request");
                        fRef.child("JPEG"+timeStamp).setValue(newGarbage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AddUserActivityEntry.this,"Successful",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else
                                    Toast.makeText(AddUserActivityEntry.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
//                ut.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if(task.isSuccessful())
//                    {
//                        Toast.makeText(AddUserActivityEntry.this, "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
//
//                        Garbage newGarbage=new Garbage(latitude.toString(),longitude.toString(),gb.getDownloadUrl().toString(),type,fUser.getEmail().toString(),"Request Sent",note.getText().toString(),fUser.getDisplayName()+" "+type+" request");
//                        fRef.child("JPEG"+timeStamp).setValue(newGarbage).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    Toast.makeText(AddUserActivityEntry.this,"Successful",Toast.LENGTH_LONG).show();
//                                    finish();
//                                }
//                                else
//                                    Toast.makeText(AddUserActivityEntry.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                    }
//                });
//                ut.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(AddUserActivityEntry.this, "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
//
//                        Garbage newGarbage=new Garbage(latitude.toString(),longitude.toString(),imageFileName,type,fUser.getEmail().toString(),"Request Sent",note.getText().toString(),fUser.getDisplayName()+" "+type+" request");
//                        fRef.child("JPEG"+timeStamp).setValue(newGarbage).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(AddUserActivityEntry.this,"Successful",Toast.LENGTH_LONG).show();
//                                finish();
//                            }
//                            else
//                                Toast.makeText(AddUserActivityEntry.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                    }
//                });
//                ut.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AddUserActivityEntry.this, "Failed to upload image", Toast.LENGTH_LONG).show();
//                    }
//                });
            }catch (Exception e)
            {
                Toast.makeText(AddUserActivityEntry.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }


        }else
        {
            Toast.makeText(AddUserActivityEntry.this,"Please fill in all the required details",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            garbageRequest.setImageBitmap(imageBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            bytesData = baos.toByteArray();

            timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            imageFileName = "JPEG"+timeStamp+".jpg";
        }}
        public void getLocation(View view){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(AddUserActivityEntry.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?

                if (ActivityCompat.shouldShowRequestPermissionRationale(AddUserActivityEntry.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(this).setTitle("Location Permission").setMessage("you have to give location permission for the app to work").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(AddUserActivityEntry.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                                    dialogInterface.dismiss();
                        }
                    }).create().show();
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(AddUserActivityEntry.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    AddUserActivityEntry.this.location=location;
                                    longitude.setText(Double.toString(location.getLatitude()));
                                    latitude.setText(Double.toString(location.getLatitude()));
                                    // Logic to handle location object
                                }
                            }
                        });
            }
        }


}
