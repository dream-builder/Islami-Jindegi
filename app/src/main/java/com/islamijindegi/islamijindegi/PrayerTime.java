package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.os.AsyncTask;

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

/**
 * Created by Shahed on 4/25/2017.
 */

public class PrayerTime extends AsyncTask<String,String,String> {
    AppPreference appPreference;
    Context context;

    public PrayerTimeResponse delegate=null;


    public PrayerTime(Context context) {
        this.context = context;
        appPreference = new AppPreference(context);
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

       // Log.d("Prayer time",s);
        JsonProcess(s);
    }


    private void JsonProcess(String s){

        JSONObject parentObject = null;

        try {
            parentObject = new JSONObject(s);

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            JSONObject timing = parentObject.getJSONObject("data").getJSONObject("timings");
            timing.put("date",date);
            appPreference.set_pref(appPreference.SALATTIMING,timing.toString());
            delegate.syncCompleted(timing.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
