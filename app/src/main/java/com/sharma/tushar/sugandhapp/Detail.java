package com.sharma.tushar.sugandhapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sharma.tushar.sugandhapp.ui.Fragments.AudioFragment;
import com.sharma.tushar.sugandhapp.ui.Fragments.CameraFragment;
import com.sharma.tushar.sugandhapp.ui.Fragments.PlayRecodingsFragment;
import com.sharma.tushar.sugandhapp.ui.Fragments.VideoFragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Detail extends AppCompatActivity {

    private static int type;
    Intent starterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        type = -1;
        starterIntent = getIntent();
        if (starterIntent.hasExtra(MainActivity.INTENT_EXTRA_KEY)) {
            type = starterIntent.getIntExtra(MainActivity.INTENT_EXTRA_KEY, -1);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }
        inflateFragment(type);
    }

    void inflateFragment(int type) {
        boolean flag = false;
        FragmentManager transaction = getSupportFragmentManager();
        switch (type) {
            case MainActivity.TYPE_VIDEO:
                if (checkCameraPermissions()) {
                    if (checkAudioPermissions()) {
                        if (checkStoragePermissions()) {
                            transaction.beginTransaction()
                                    .replace(R.id.container, new VideoFragment())
                                    .commit();
                        } else {
                            requestStoragePermission();
                            return;
                        }
                    } else {
                        requestAudioPermission();
                        return;
                    }
                } else {
                    requestCameraPermission();
                    return;
                }
                break;
            case MainActivity.TYPE_AUDIO:
                if (checkAudioPermissions()) {
                    if (checkStoragePermissions()) {
                        transaction.beginTransaction()
                                .replace(R.id.container, new AudioFragment())
                                .commit();
                    }else {
                        requestStoragePermission();
                        return;
                    }
                } else {
                    requestAudioPermission();
                    return;
                }
                break;
            case MainActivity.TYPE_IMAGE:
                if (checkCameraPermissions()) {
                    if (checkStoragePermissions()) {
                        transaction.beginTransaction()
                                .replace(R.id.container, new CameraFragment())
                                .commit();
                    } else {
                        requestStoragePermission();
                        return;
                    }
                } else {
                    requestCameraPermission();
                    return;
                }
                break;
            default:
                if (checkStoragePermissions()) {
                    transaction.beginTransaction()
                            .replace(R.id.container, new PlayRecodingsFragment())
                            .commit();
                } else {
                    requestStoragePermission();
                    return;
                }
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{CAMERA}, 1);
    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{RECORD_AUDIO}, 2);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3:
                if (grantResults.length> 0) {
                    boolean StoragePermission1 = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean StoragePermission2 = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (!StoragePermission1 && !StoragePermission2) {
                        Toast.makeText(Detail.this, "Storage restricted.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                        startActivity(starterIntent);
                    }
                }
                break;
            case 2:
                if (grantResults.length> 0) {
                    boolean RecordPermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (!RecordPermission) {
                        Toast.makeText(Detail.this, "Audio restricted.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                        startActivity(starterIntent);
                    }
                }
                break;
            case 1:
                if (grantResults.length> 0) {
                    boolean cameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (!cameraPermission) {
                        Toast.makeText(Detail.this, "Camera restricted.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                        startActivity(starterIntent);
                    }
                }
                break;
        }
    }

    private boolean checkAudioPermissions() {
        return ContextCompat.checkSelfPermission(Detail.this, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCameraPermissions() {
        return ContextCompat.checkSelfPermission(Detail.this, CAMERA) == PackageManager.PERMISSION_GRANTED ;
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(Detail.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Detail.this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
