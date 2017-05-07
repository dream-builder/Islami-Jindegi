package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Shahed on 3/4/2017.
 */

public class Check {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=1;
    private static boolean WRITE_PERMISSION =false;

    private  Context context;

    public Check(Context context) {
        this.context=context;
    }


    public boolean checkInternet(){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
            return true;
        else {

            Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);

            Toast.makeText(context,"No internet",Toast.LENGTH_LONG).show();
            return false;

        }
    }


    // Check internal storage and folder
    // If the folder IJFiles is not exists, Make dir IJFiles
    public boolean ExternalStorage(){

        String state;
        state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){

            File Root = Environment.getExternalStorageDirectory();
            File dir = new File(Root.getAbsolutePath()+ "/IJFiles");

            if(!dir.exists()) {
                dir.mkdir();
                Toast.makeText(context,"Directory created",Toast.LENGTH_LONG).show();
            }

            return  true;
        }

        else
            return false;

    }

}
