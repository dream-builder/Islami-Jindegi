package com.islamijindegi.islamijindegi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Shahed on 3/9/2017.
 */

public class AppPermisssion extends Activity {

    Context context;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=1;
    public static final int PERMISSIONS_REQUEST_INTERNET=1;
    public static final int PERMISSIONS_REQUEST_NETWORKSTATE=1;
    public static final int PERMISSIONS_REQUEST_WAKE_LOCK=1;
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=1;
    public static final int PERMISSIONS_REQUEST_VIBRATE=1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACT=1;

    public AppPermisssion(Context context) {
        this.context = context;
    }


    public void checkExternalStoragePermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){

            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    public void checkReadContactPermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACT);

        }
    }

    public void checkInternetPermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.INTERNET )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.INTERNET},
                    PERMISSIONS_REQUEST_INTERNET);

        }
    }

    public void checkNetworkStatePermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_NETWORK_STATE )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSIONS_REQUEST_NETWORKSTATE);

        }
    }

    public void checkWakeLockPermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.WAKE_LOCK )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WAKE_LOCK},
                    PERMISSIONS_REQUEST_WAKE_LOCK);

        }
    }

    public void checkReadExternalStoragePermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }
    }

    public void checkVibratePermission(){

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.VIBRATE )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.VIBRATE},
                    PERMISSIONS_REQUEST_VIBRATE);

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
