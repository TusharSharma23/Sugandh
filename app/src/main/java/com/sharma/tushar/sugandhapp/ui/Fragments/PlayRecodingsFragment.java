package com.sharma.tushar.sugandhapp.ui.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.sharma.tushar.sugandhapp.MainActivity;
import com.sharma.tushar.sugandhapp.R;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayRecodingsFragment extends Fragment {


    public PlayRecodingsFragment() {
        // Required empty public constructor
    }

    VideoView videoPlayer;
    ImageView imageView;
    MediaPlayer audio;
    ImageButton button;

    int stopPosition = 0;
    int stpos = 0;

    boolean playing = true;

    boolean first = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_play_recodings, container, false);

        Bitmap image = getImage();
        audio = getAudioFile();
        String video = getVideoFile();

        boolean flag = false;

        if (image == null) {
            Toast.makeText(getContext(), "Click image first", Toast.LENGTH_SHORT).show();
            flag = true;
        }

        if (audio == null) {
            Toast.makeText(getContext(), "Record audio first", Toast.LENGTH_SHORT).show();
            flag = true;
        }

        if (video == null) {
            Toast.makeText(getContext(), "Record video first", Toast.LENGTH_SHORT).show();
            flag = true;
        }

        if (flag) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
            return view;
        }
        videoPlayer = view.findViewById(R.id.video_player);
        videoPlayer.setVideoPath(video);
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoPlayer.setMediaController(new MediaController(getContext()));
        videoPlayer.requestFocus();

        imageView = view.findViewById(R.id.image_player);
        imageView.setImageBitmap(image);


        button = view.findViewById(R.id.play_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first) {
                    audio.start();
                    videoPlayer.start();
                    first = false;
                    button.setImageResource(android.R.drawable.ic_media_pause);
                    return;
                }

                if(playing) {
                    stpos = audio.getCurrentPosition();
                    audio.pause();
                    stopPosition = videoPlayer.getCurrentPosition();
                    videoPlayer.pause();
                    button.setImageResource(android.R.drawable.ic_media_play);
                    playing = !playing;
                } else {
                    audio.seekTo(stpos);
                    audio.start();
                    videoPlayer.seekTo(stopPosition);
                    videoPlayer.start();
                    button.setImageResource(android.R.drawable.ic_media_pause);
                    playing = !playing;
                }
            }
        });

        return view;
    }

    private String getVideoFile() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "Sugandh/Sugandh" + ".mp4");
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    private MediaPlayer getAudioFile() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "Sugandh/Sugandh" + ".3gp");
        MediaPlayer mediaPlayer;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return mediaPlayer;
    }

    private Bitmap getImage() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "Sugandh/Sugandh" + ".jpg");
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!first && videoPlayer!=null && audio!=null && button!=null) {
            if (playing) {
                stpos = audio.getCurrentPosition();
                audio.pause();
                stopPosition = videoPlayer.getCurrentPosition();
                videoPlayer.pause();
                button.setImageResource(android.R.drawable.ic_media_play);
                playing = !playing;
            } else {
                audio.seekTo(stpos);
                audio.start();
                videoPlayer.seekTo(stopPosition);
                videoPlayer.start();
                button.setImageResource(android.R.drawable.ic_media_pause);
                playing = !playing;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!first && videoPlayer!=null && audio!=null && button!=null) {
            if (playing) {
                stpos = audio.getCurrentPosition();
                audio.pause();
                stopPosition = videoPlayer.getCurrentPosition();
                videoPlayer.pause();
                button.setImageResource(android.R.drawable.ic_media_play);
                playing = !playing;
            } else {
                audio.seekTo(stpos);
                audio.start();
                videoPlayer.seekTo(stopPosition);
                videoPlayer.start();
                button.setImageResource(android.R.drawable.ic_media_pause);
                playing = !playing;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoPlayer!=null && audio!=null) {
            videoPlayer.stopPlayback();
            audio.release();
        }
    }
}
