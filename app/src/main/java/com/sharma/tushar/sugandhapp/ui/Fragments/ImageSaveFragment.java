package com.sharma.tushar.sugandhapp.ui.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sharma.tushar.sugandhapp.MainActivity;
import com.sharma.tushar.sugandhapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.sharma.tushar.sugandhapp.ui.Fragments.CameraFragment.bitmap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSaveFragment extends Fragment {


    public ImageSaveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_save, container, false);

        ImageView imageView = view.findViewById(R.id.save_image);
        imageView.setImageBitmap(bitmap);

        ImageButton button = view.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeImage(bitmap);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ImageButton button1 = view.findViewById(R.id.cancel_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager transaction = getActivity().getSupportFragmentManager();
                transaction.beginTransaction()
                        .replace(R.id.container, new CameraFragment())
                        .commit();
            }
        });

        return view;
    }

    private void storeImage(Bitmap bitmap) {

        File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "Sugandh");

        if (!outFile.exists()) {
            if (!outFile.mkdirs()) {
                Log.e("Audio", "Unable to make dir");
            }
        }

        outFile = new File(outFile, "Sugandh" + ".jpg");

        try(FileOutputStream out = new FileOutputStream(outFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Toast.makeText(getContext(), "Saved successfully.", Toast.LENGTH_SHORT).show();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
