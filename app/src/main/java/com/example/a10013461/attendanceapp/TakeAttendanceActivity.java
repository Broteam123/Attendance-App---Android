package com.example.a10013461.attendanceapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Manifest;

public class TakeAttendanceActivity extends AppCompatActivity {

    SurfaceView cameraView;
    CameraSource cameraSource;
    ListView listView;
    TextView textView;
    Button finishAttendanceButton;
    ArrayList<String> classList;
    ArrayList<Boolean> presentList=new ArrayList<Boolean>();
    ArrayAdapter<String> arrayAdapter;
    int numPresent;
    final int RequestCameraPermissionID = 1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case RequestCameraPermissionID:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(TakeAttendanceActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        cameraView = (SurfaceView) findViewById(R.id.TakeAttendanceCameraView);
        classList = getIntent().getStringArrayListExtra("classList");
        listView = (ListView) findViewById(R.id.namesToCheckIn);
        textView = (TextView) findViewById(R.id.textView2);
        finishAttendanceButton = (Button) findViewById(R.id.endAttendanceButton);
        numPresent = 0;
        arrayAdapter = new ArrayAdapter<String>(TakeAttendanceActivity.this,android.R.layout.simple_list_item_1,classList);
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);

        for(int i=0;i<classList.size();i++){
            presentList.add(false);
        }

        finishAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEYYY,numPresent);
                intent.putExtra(MainActivity.KEYYYY,getIntent().getIntExtra("pos",0));
                if(numPresent==0){
                    intent.putExtra(MainActivity.ANOTHERKEY,1);
                }else if(numPresent>0&&numPresent<classList.size()){
                    intent.putExtra(MainActivity.ANOTHERKEY,2);
                }else if(numPresent==classList.size()){
                    intent.putExtra(MainActivity.ANOTHERKEY,3);
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("TakeAttendanceActivity", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(TakeAttendanceActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(TakeAttendanceActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size()!=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                for(int i=0;i<classList.size();i++){
                                    if(stringBuilder.toString().toUpperCase().contains(classList.get(i).toUpperCase())){
                                        if(!presentList.get(i)) {
                                            listView.getChildAt(i - listView.getFirstVisiblePosition()).setBackgroundColor(Color.GREEN);
                                            presentList.set(i, true);
                                            numPresent++;
                                        }
                                    }
                                }
                            }
                        });

                    }
                }
            });
        }
    }
}
