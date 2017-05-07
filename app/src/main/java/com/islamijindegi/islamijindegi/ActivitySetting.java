package com.islamijindegi.islamijindegi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ActivitySetting extends AppCompatActivity {

    Button dbDelete, dbSync;
    AlertDialog.Builder builder;
    AlertDialog alert;
    DatabaseHelper DB;
    private ProgressDialog Dialog;
    AppPreference appPreference;
    Check check;
    TextView tokenTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");

        init();
        buttonActions();

    }

    private void init() {

        builder = new AlertDialog.Builder(this);
        dbDelete = (Button)findViewById(R.id.clearButton);
        dbSync = (Button)findViewById(R.id.syncButton);

        tokenTV = (TextView) findViewById(R.id.getTokenTV);

        DB = new DatabaseHelper(this);
        Dialog = new ProgressDialog(this);

        appPreference = new AppPreference(this);
        check = new Check(this);
    }


    private void buttonActions(){


        tokenTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = appPreference.get_pref(appPreference.FCMTOKEN);
                String oldToken = appPreference.get_pref(appPreference.FCMOLDTOKEN);

                if(check.checkInternet())
                    registerToken(token);
            }
        });

        //Delete old database
        dbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle(R.string.clear_database);
                builder.setMessage(R.string.clear_database_msg);
                alert= builder.create();

                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alert.dismiss();
                        Dialog.setMessage("Deleting database ...");
                        Dialog.show();

                        DB.openDB();
                        DB.deleteAllPost();
                        DB.deleteCategory();
                        DB.close();

                        Dialog.dismiss();

                        appPreference.set_pref(appPreference.APPFIRSTRUN,"0");

                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alert.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        dbSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle(R.string.sync_database);
                builder.setMessage(R.string.sync_database_msg);
                alert= builder.create();

                builder.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(check.checkInternet()) {

                            appPreference.set_pref(appPreference.WEBSYNCSTART,"1");
                            appPreference.set_pref(appPreference.LOOP,"0");
                            appPreference.set_pref(appPreference.SYNCPAGE,"0");

                            WebService webservice = new WebService(ActivitySetting.this);
                            webservice.execute("http://www.islamijindegi.com/webservice/?action=sync");

                            DB.openDB();
                            DB.createCategory();
                            DB.close();

                        }

                        alert.dismiss();
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }


    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("OldToken",appPreference.get_pref(appPreference.FCMOLDTOKEN))
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
                .url("http://www.islamijindegi.com/token-register/")
                .post(body)
                .build();

        appPreference.set_pref(appPreference.FCMOLDTOKEN,token);

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
