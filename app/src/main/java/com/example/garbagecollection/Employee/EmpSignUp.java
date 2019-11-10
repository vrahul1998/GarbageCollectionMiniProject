package com.example.garbagecollection.Employee;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.garbagecollection.Company.Employee;
import com.example.garbagecollection.MainActivity;
import com.example.garbagecollection.R;
import com.example.garbagecollection.SignUp;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EmpSignUp extends AppCompatActivity {
    ImageView empImage;
    EditText empName,empPhone,empAge;
    Button empRegister;
    Spinner empSpinner;
    String empComp;
    public static final int CAMERA_REQUEST_CODE=1;
    String imageFileName;
    byte[] bytesData;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDbase;
    private DatabaseReference mRef;
    private StorageReference empPic;
    private StorageReference mStore;

    HashMap<String,String>companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_sign_up);

        empName=findViewById(R.id.empName);
        empAge=findViewById(R.id.empAge);
        empPhone=findViewById(R.id.empPhone);
        empImage=findViewById(R.id.empImage);
        empRegister=findViewById(R.id.empRegister);

        mAuth = FirebaseAuth.getInstance();
        mDbase = FirebaseDatabase.getInstance();
        mRef=mDbase.getReference().child("Companies");
        mStore = FirebaseStorage.getInstance().getReference();
        mUser=mAuth.getCurrentUser();



        empImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        });


        empRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyChoice=empSpinner.getSelectedItem().toString();
                if(companyChoice.equals("freelance"))
                {
                   empComp="freelance";
                }
                else
                    empComp=companies.get(companyChoice);
                if(!empName.getText().toString().isEmpty() && !empAge.getText().toString().isEmpty() && !empPhone.getText().toString().isEmpty() && bytesData!=null && !empComp.isEmpty()){
                    mRef=mDbase.getReference().child("Employees");
                    try{
                        mStore=FirebaseStorage.getInstance().getReference();
                        empPic=mStore.child("EmployeeImage").child(imageFileName);
                        UploadTask ut=empPic.putBytes(bytesData);
                        Task<Uri> uriTask=ut.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                return empPic.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri=task.getResult();
                                final Employee employee=new Employee(empName.getText().toString(),empAge.getText().toString(),uri.toString(),mUser.getEmail(),empComp,empSpinner.getSelectedItem().toString(),"Inactive","");
                                mRef.child(mUser.getUid()).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(EmpSignUp.this, "successful", Toast.LENGTH_LONG).show();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(employee.getName()).build();

                                            mUser.updateProfile(profileUpdates);
                                            Intent intent=new Intent(EmpSignUp.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            EmpSignUp.this.finish();

                                        } else
                                            Toast.makeText(EmpSignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }catch (Exception e) {
                        Toast.makeText(EmpSignUp.this, e.getMessage(),Toast.LENGTH_LONG).show();
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
            empImage.setImageBitmap(imageBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            bytesData = baos.toByteArray();

            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            imageFileName = "JPEG"+timeStamp+".jpg";
        }}
        @Override
    public void onStart(){
        super.onStart();
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Is better to use a List, because you don't know the size
                    // of the iterator returned by dataSnapshot.getChildren() to
                    // initialize the array
                    final List<String> comp = new ArrayList<String>();
                    companies=new HashMap<String, String>();
                    comp.add("freelance");
                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String company = areaSnapshot.child("name").getValue(String.class);
                        String id=areaSnapshot.child("cid").getValue(String.class);
                        companies.put(company,id);
                        comp.add(company);
                    }

                    empSpinner = (Spinner) findViewById(R.id.empSpinner);
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(EmpSignUp.this, android.R.layout.simple_spinner_item, comp);
                    companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    empSpinner.setAdapter(companyAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
}
