package com.rejointech.keybook.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rejointech.keybook.R;

public class Guide extends AppCompatActivity {
    VideoView vidviewa, vidviewb, vidviewc;
    StorageReference storageRef;
    private MediaController mediaController;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide2);
        vidviewa = findViewById(R.id.vidviewa);
        mediaController = new MediaController(this);
        storageRef = FirebaseStorage.getInstance().getReference();
        vidviewa.setMediaController(mediaController);
        vidviewa.start();
        vidviewb = findViewById(R.id.vidviewb);
        vidviewb.setMediaController(mediaController);
        vidviewb.start();
        vidviewc = findViewById(R.id.vidviewc);
        vidviewc.setMediaController(mediaController);
        vidviewc.start();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_guide);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_guide:
                        return true;
                    case R.id.nav_dashboard:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_design:
                        startActivity(new Intent(getApplicationContext(), Design.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        StorageReference dateRef = storageRef.child("5_6055476215991501084.mp4");
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                vidviewa.setVideoURI(downloadUrl);
                //do something with downloadurl
            }
        });


    }


}