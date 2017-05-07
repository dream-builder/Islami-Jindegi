package com.islamijindegi.islamijindegi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationAction extends AppCompatActivity {

    TextView nTitleTextView,pageTitleTv;
    Bundle extras;
    Button updateButton;
    DatabaseHelper DB;
    Check check;

    String id ;
    String title ;
    String msg;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_action);

        getSupportActionBar().hide();

        init();

        loadValue();
        update();
    }

    public void alertMsg(String title,String msg){

        builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }


        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadValue() {

        extras = getIntent().getExtras();
        id = extras.getString("postid");
        title = extras.getString("title");
        msg = extras.getString("body");

        pageTitleTv.setText(title);
        nTitleTextView.setText(msg);

        if(!id.equals("0"))
            updateButton.setVisibility(View.VISIBLE);
        else
            updateButton.setVisibility(View.GONE);

    }

    private void init() {
        check = new Check(this);
        DB= new DatabaseHelper(this);
        updateButton = (Button)findViewById(R.id.updateBtn);
        nTitleTextView = (TextView) findViewById(R.id.nTiteTextView);
        pageTitleTv = (TextView) findViewById(R.id.pageTitle);
    }


    private void update(){

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.checkInternet())
                        new updatePost().execute("http://islamijindegi.com/webservice/?action=get_post&id="+id);

            }
        });
    }



    private class updatePost extends AsyncTask<String,String,String>{

        private ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DB.openDB();
            Dialog = new ProgressDialog(NotificationAction.this);

            Dialog.setMessage("Getting data from web server");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line="";
                while ((line=reader.readLine()) !=null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(reader !=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s!=null){
                updateRecord(s);
            }

            DB.closeDB();
            Dialog.dismiss();

            alertMsg("Update","Update success.");
        }


        public void updateRecord(String data){

           // Log.d("data",data);

            JSONObject parentObject = null;

            try {
                parentObject = new JSONObject(data);
                JSONArray parentArray = parentObject.getJSONArray("posts");

                for(int i=0; i<parentArray.length();i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());

                    if(!DB.postExists(finalObject.getString(DB.POSTID))) {

                        DB.insertPost1(
                                finalObject.getString(DB.POSTID),
                                finalObject.getString(DB.POSTTITLE),
                                "",
                                finalObject.getString(DB.POSTCATEGORY),
                                finalObject.getString(DB.POSTAUTHOR),
                                finalObject.getString(DB.POSTTYPE),
                                finalObject.getString(DB.POSTURL),
                                finalObject.getString(DB.POSTMETAEXTFILE),
                                finalObject.getString(DB.POSTMETAUPLOADFILE),
                                "",
                                finalObject.getString(DB.POSTMETAVIDEOLINK),
                                currentDateandTime,
                                "1" /*NEW POST */
                        );
                    }
                    else{
                        DB.updatePost(
                                finalObject.getString(DB.POSTID),
                                finalObject.getString(DB.POSTTITLE),
                                "",
                                finalObject.getString(DB.POSTCATEGORY),
                                finalObject.getString(DB.POSTAUTHOR),
                                finalObject.getString(DB.POSTTYPE),
                                finalObject.getString(DB.POSTURL),
                                finalObject.getString(DB.POSTMETAEXTFILE),
                                finalObject.getString(DB.POSTMETAUPLOADFILE),
                                "",
                                finalObject.getString(DB.POSTMETAVIDEOLINK),
                                currentDateandTime,
                                "1" /*NEW POST */
                        );
                        //Log.d("Duplicate","sd");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {

            }

        }
    }


}
