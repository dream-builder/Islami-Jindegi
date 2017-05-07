package com.islamijindegi.islamijindegi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Shahed on 4/2/2017.
 */

public class CopyFile {

    public static final String OPEN ="1";
    public static final String DONOTHING ="0";

    public CopyFile(Context context) {
        this.context = context;
    }

    Context context;

    public void copyFileToInternalStorage(String fileName, String action){
        InputStream reader = null;
        OutputStream myOutput = null;

        ProgressDialog dialog = new ProgressDialog(context);
        String filePath=Environment.getExternalStorageDirectory() + "/IJFiles/"+fileName;
        //Log.d("path", filePath);
        File path = new File(filePath);

        try {
            dialog.setMessage("Loading");
            dialog.show();

            reader = context.getAssets().open(fileName);

            //Create file

            myOutput = new FileOutputStream(path.toString());
            // do reading, usually loop until end of file reading

            byte[] buffer = new byte[1024];
            int length;

            while ((length = reader.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            //myInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

            if(action.equals("1")){

                File file = new File(filePath);
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            }

            dialog.dismiss();
        }

    }
}
