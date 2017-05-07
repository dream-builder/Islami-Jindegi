package com.islamijindegi.islamijindegi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebViewBookArticle extends AppCompatActivity {

    TextView postTitleTextView,writerTextView;
    WebView postWebView;
    Bundle extras;
    DatabaseHelper databaseHelper;
    AlertDialog.Builder builder;
    Check check;
    String POSTID=null;
    AppPreference appPreference;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_book_article);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        databaseHelper = new DatabaseHelper(this);

        extras = getIntent().getExtras();
        int id = Integer.parseInt (extras.getString("postID"));
        POSTID = extras.getString("postID");

        init();

        if(appPreference.get_pref(appPreference.POSTTYPE).equals("articles"))
            getSupportActionBar().setTitle("প্রবন্ধ");

        if(appPreference.get_pref(appPreference.POSTTYPE).equals("specialamal"))
            getSupportActionBar().setTitle("বিশেষ আমল");

        getContent(id);
    }




    private void getContent(int id) {
        String c = null;
        databaseHelper.openDB();
        c = databaseHelper.getPostContent(id);
        databaseHelper.closeDB();

        final String postid= String.valueOf(id);


        if(c==null || c.equals("")){

            builder.setTitle(R.string.sync_title);
            builder.setMessage(R.string.download_content);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(check.checkInternet()){
                        WebSync webSync = new  WebSync(WebViewBookArticle.this);
                        webSync.execute("http://www.islamijindegi.com/webservice/?action=get_content&id="+postid);

                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }
        else{
            loadDataToView(c);
        }



    }


    public void loadDataToView(String data){
        String style = "<style>html{background-color:#e2efd9; font-size:13px; padding:15px;}</style>";
        data = Html.fromHtml(data).toString();
        postWebView.getSettings().setJavaScriptEnabled(true);
        postWebView.loadData(style+data, "text/html; charset=UTF-8", null);

    }

    private void init() {


        builder = new AlertDialog.Builder(this);
        check = new Check(this);
        postTitleTextView = (TextView) findViewById(R.id.postTitleTextView);
        writerTextView = (TextView) findViewById(R.id.writerTextView);
        postWebView = (WebView) findViewById(R.id.postWebView);

        appPreference = new AppPreference(this);

        postTitleTextView.setText(extras.getString("postTitle"));
        writerTextView.setText(extras.getString("postAuthor"));

    }

    private class WebSync extends AsyncTask<String, String,String> {
        StringBuffer buffer;
        Context context;
        private ProgressDialog Dialog;

        public WebSync(Context context) {
            this.context = context;
            Dialog= new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setMessage("Getting data from web server....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line);
                }



                //Log.d("cnt",buffer.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            databaseHelper.openDB();
            databaseHelper.updatePostContent(POSTID,s);
            loadDataToView(s);
            databaseHelper.closeDB();

            Dialog.dismiss();

        }
    }
}
