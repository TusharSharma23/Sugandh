package com.sharma.tushar.sugandhapp.ui.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sharma.tushar.sugandhapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends Fragment {


    public AudioFragment() {
        // Required empty public constructor
    }

    MediaRecorder mediaRecorder;
    boolean recorderState = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        if (!checkPermissions()) {
            requestPermission();
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);

        File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "Sugandh");

        if (!outFile.exists()) {
            if (!outFile.mkdirs()) {
                Log.e("Audio", "Unable to make dir");
            }
        }

        outFile = new File(outFile, "Sugandh" + ".3gp");

        mediaRecorder.setOutputFile(outFile.getAbsolutePath());

        ImageButton button = view.findViewById(R.id.start_recording);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    if (recorderState) {
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            recorderState = !recorderState;
                            Toast.makeText(getContext(), "Recording started.", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mediaRecorder.stop();
                        Toast.makeText(getContext(), "Recording saved in Music.", Toast.LENGTH_SHORT).show();
                        recorderState = !recorderState;
                    }
                } else {
                    requestPermission();
                    Toast.makeText(getContext(), "Provide audio permission.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, 1);
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

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

}
