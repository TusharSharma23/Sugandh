package com.sharma.tushar.sugandhapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    public static final String GALLERY_DIR = "sugandh";
    public static final int TYPE_IMAGE = 3;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;

    public static final String INTENT_EXTRA_KEY = "key";

    public static boolean first = true;

    ImageButton mVideoBtn;
    ImageButton mAudioBtn;
    ImageButton mCameraBtn;

    Button mShowRecordings;

    boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoBtn = findViewById(R.id.record_video);
        mAudioBtn = findViewById(R.id.record_audio);
        mCameraBtn = findViewById(R.id.click_picture);
        mShowRecordings = findViewById(R.id.show_recordings);

        mVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra(INTENT_EXTRA_KEY, TYPE_VIDEO);
                startActivity(intent);
            }
        });

        mAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra(INTENT_EXTRA_KEY, TYPE_AUDIO);
                startActivity(intent);
            }
        });

        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra(INTENT_EXTRA_KEY, TYPE_IMAGE);
                startActivity(intent);
            }
        });

        mShowRecordings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Detail.class);
                startActivity(intent);
            }
        });

        ActionBar topBar = getSupportActionBar();
        if (topBar!=null) {
            topBar.hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
            return;
        }

        backPressed = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (first && !checkPermissions()) {
            requestPermission();
            first = false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, CAMERA, READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission && cameraPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(MainActivity.this, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
