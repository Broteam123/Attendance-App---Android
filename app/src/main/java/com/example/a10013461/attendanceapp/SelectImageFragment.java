package com.example.a10013461.attendanceapp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectImageFragment extends Fragment {

    ImageView imageView;
    ClassElement classElement;
    ArrayList<String> names=new ArrayList<>();
    Button readTextButton;
    Button getImageButton;
    Frame frame;
    private Uri selectedImageUri;
    final int RequestPermissionCode=1;
    ReceiveImage receiveImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_select_image,container,false);

        classElement=getArguments().getParcelable("classList");
        imageView = fragmentView.findViewById(R.id.imageView_select_image_frag);

        if (getArguments().getString("img") != null)
            if(Uri.parse(getArguments().getString("img"))!=null)
                imageView.setImageURI(Uri.parse(getArguments().getString("img")));

        readTextButton = fragmentView.findViewById(R.id.readTextButton);
        getImageButton = fragmentView.findViewById(R.id.getImageButton);

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            requestRunTimePermission();
        }

        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity().getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        readTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageView.getDrawable()!=null){
                    TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
                    if (!textRecognizer.isOperational()) {
                        Log.w("AddPeopleActivity", "Detector dependencies are not yet available");
                    } else {
                        frame = new Frame.Builder()
                                .setBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap())
                                .build();

                        String imageText="";
                        StringBuilder stringBuilder = new StringBuilder();
                        SparseArray<TextBlock> items = textRecognizer.detect(frame);
                        for(int i=0;i<items.size();i++){
                            TextBlock textBlock = items.valueAt(i);
                            stringBuilder.append(textBlock.getValue());
                            stringBuilder.append("\n");
                            imageText = stringBuilder.toString();
                        }

                        String [] words = imageText.split("\n");
                        for(int i=0;i<words.length;i++){
                            names.add(words[i]);
                        }
                        classElement.setPeople(names);
                    }
                }else{
                    Toast.makeText(getActivity(),"No image found :(",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return fragmentView;
    }

    private void requestRunTimePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)){
            Toast.makeText(getActivity(),"CAMERA permission lets us access CAMERA app", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},RequestPermissionCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.getUri();
                receiveImage.receive(selectedImageUri);
                imageView.setImageURI(selectedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestPermissionCode:
            {
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(),"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(),"Permission Cancelled",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        receiveImage = (ReceiveImage) context;
    }

    public interface ReceiveImage{
        public void receive(Uri uri);
    }
}
