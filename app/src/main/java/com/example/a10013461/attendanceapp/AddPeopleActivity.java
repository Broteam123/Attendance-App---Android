package com.example.a10013461.attendanceapp;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.theartofdev.edmodo.cropper.CropImage;


public class AddPeopleActivity extends AppCompatActivity implements SelectImageFragment.ReceiveImage{

    ImageView imageView;
    TextView textView;
    Button readTextButton;
    Button getImageButton;
    Uri theImage;
    final int RequestPermissionCode=1;
    Frame frame;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG","HI2");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.action_selectImage);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle data = new Bundle();
                data.putParcelable("classList", getIntent().getParcelableExtra("className"));
                if(theImage!=null)
                    data.putString("img",theImage.toString());
                switch(item.getItemId()){
                    case R.id.action_readText:
                        ReadTextFragment readTextFragment = new ReadTextFragment();
                        readTextFragment.setArguments(data);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,readTextFragment).commit();
                        break;
                    case R.id.action_selectImage:
                        SelectImageFragment selectImageFragment = new SelectImageFragment();
                        selectImageFragment.setArguments(data);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectImageFragment).commit();
                        break;
                    case R.id.action_goback:
                        finish();
                        break;
                }

                return true;
            }
        });

       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SelectImageFragment()).commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestPermissionCode:
            {
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Permission Cancelled",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void receive(Uri uri) {
        theImage = uri;
    }
}
