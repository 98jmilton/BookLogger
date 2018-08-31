package com.example.ezmilja.booklogger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SplashScreen extends AppCompatActivity {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    final static public DatabaseReference BookRef = database.getReference();
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    final static public StorageReference storageReference = storage.getReference();

    public static int p=0;
    public static int h=0;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private Context mContext;
    private Activity mActivity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(), "yourfont.ttf");
        TextView ericssonText = findViewById(R.id.EricssonText);
        ericssonText.setTypeface(myTypeFace1);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = SplashScreen.this;

        checkPermission();
        BookRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                h = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

        protected void checkPermission(){
            if(ContextCompat.checkSelfPermission(mActivity,Manifest.permission.CAMERA)
                    + ContextCompat.checkSelfPermission(
                    mActivity,Manifest.permission.READ_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(
                    mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){

                // Do something, when permissions not granted
                if(ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity,Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity,Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Camera, Read and Write External" +
                            " Storage permissions are required to do the task.");
                    builder.setTitle("Please grant the following permissions");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    mActivity,
                                    new String[]{
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    },
                                    MY_PERMISSIONS_REQUEST_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    // Directly request for required permissions, without explanation
                    ActivityCompat.requestPermissions(
                            mActivity,
                            new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            MY_PERMISSIONS_REQUEST_CODE
                    );
                }
            }else {
                // Do something, when permissions are already granted
                Toast.makeText(mContext,"Permissions already granted",Toast.LENGTH_SHORT).show();

                Thread myThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            Intent intent = new Intent(getApplicationContext(), ContentsActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                myThread.start();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                        ) {
                    // Permissions are granted
                    Toast.makeText(mContext,"Permissions granted.",Toast.LENGTH_SHORT).show();

                    Thread myThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(2000);
                                Intent intent = new Intent(getApplicationContext(), ContentsActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    myThread.start();
                } else {
                    // Permissions are denied
                    Toast.makeText(mContext,"Permissions denied.",Toast.LENGTH_SHORT).show();

                        Thread myThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
                                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                    startActivity(intent);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        myThread.start();
                    }
            }
        }
    }
}




