package com.islamijindegi.islamijindegi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shahed on 3/1/2017.
 */

public class DownloadMaterial extends AsyncTask<String, Integer, String> {

    public DownloadResponse delegate = null;

    public Context context;
    private PowerManager.WakeLock mWakeLock;
    public ProgressDialog mProgressDialog;
    private String fileName="";
    private OutputStream output = null;
    ListView listView;

    public String getDownloadFileID() {
        return DownloadFileID;
    }

    public void setDownloadFileID(String downloadFileID) {
        DownloadFileID = downloadFileID;
    }

    private String DownloadFileID;

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public DownloadMaterial(Context context) {
        this.context = context;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Downloading, please wait ...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);


    }

    public String AppRoot(){

        File Root = Environment.getExternalStorageDirectory();
        //File dir = new File(Root.getAbsolutePath() + "/IJFiles");

        return Root.toString();
    }

    @Override
    protected String doInBackground(String... sUrl) {

        InputStream input = null;

        HttpURLConnection connection = null;

         try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                //String myPath= Environment.getExternalStorageDirectory()+"/test.pdf";

                //File myFile = new File();
                //input = new FileInputStream(myFile);

                // String path=":/storage/sdcard0/DCIM/Camera/1414240995236.jpg";//it contain your path of image..im using a temp string..
                fileName=sUrl[0].substring(sUrl[0].lastIndexOf("/")+1);
              //  Toast.makeText(context,filename,Toast.LENGTH_SHORT).show();


                input = connection.getInputStream();

                String state;
                state = Environment.getExternalStorageState();

                if (Environment.MEDIA_MOUNTED.equals(state)) {

                    File Root = Environment.getExternalStorageDirectory();
                    File dir = new File(Root.getAbsolutePath() + "/IJFiles");
                    output = new FileOutputStream(Root.getAbsolutePath() + "/IJFiles" + File.separator + fileName);

                    fileName="/IJFiles" + File.separator + fileName;
                }




                //File file = new File();


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        else {
            delegate.downloadCompleted(fileName);
        }
    }

}
