package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by Shahed on 3/11/2017.
 */

public class FileHandler {

    Context context;

    public FileHandler(Context context) {
        this.context = context;
    }


    public void openPDF(String pathUri){

        //Uri path = Uri.fromFile(pathUri);
        //Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setDataAndType(path, "application/pdf");
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //context.startActivity(intent);



        File f = new File(pathUri);
        Uri path = Uri.fromFile(f);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }


    public boolean fileExists(String path){
        File Root = Environment.getExternalStorageDirectory();
        File dir = new File(Root.getAbsolutePath() + "/IJFiles");
        String p = Root.getAbsolutePath() + path;

        File file = new File(p);

        //Log.d("File", String.valueOf(file.exists()));
        return file.exists();

    }



}
