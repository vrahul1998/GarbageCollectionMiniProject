package com.example.garbagecollection.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbagecollection.R;
import com.squareup.picasso.Picasso;

public class GarbageItemView extends AppCompatActivity {
    TextView title,requestedBy,desc,status,wasteType,acceptedEmail;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_item_view);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        img=findViewById(R.id.vImage);
        title=findViewById(R.id.vTitle);
        requestedBy=findViewById(R.id.vRequestedBy);
        desc=findViewById(R.id.vDesc);
        status=findViewById(R.id.vStatus);
        wasteType=findViewById(R.id.vWasteType);
        acceptedEmail=findViewById(R.id.vAcceptedEmail);
        acceptedEmail.setText(bundle.get("acceptedBy").toString());
        title.setText(bundle.get("title").toString());
        requestedBy.setText(bundle.get("email").toString());
        status.setText(bundle.get("status").toString());
        wasteType.setText(bundle.get("wasteType").toString());
        Picasso.with(getBaseContext()).load(bundle.get("imgName").toString()).placeholder(R.drawable.default_avatar).into(img);
    }
}
