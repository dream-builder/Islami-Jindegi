package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Shahed on 4/9/2017.
 */

public class SendContactInfo extends AsyncTask<String,Void,String> {
    Context context;
    HttpURLConnection httpURLConnection = null;
    BufferedReader reader=null;
    AppPreference appPreference;


    public SendContactInfo(Context context) {
        this.context = context;
        appPreference = new AppPreference(context);
    }


    @Override
    protected String doInBackground(String... params) {

        String data = "";

        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());

            data = URLEncoder.encode("detail", "UTF-8")
                    + "=" + URLEncoder.encode(params[1], "UTF-8");

            wr.writeBytes(data);
            wr.flush();
            wr.close();


            //Response form server
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            //

            //Log.d("postData",params[1]);

            return sb.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        JSONObject parentObject = null;

        try {
            parentObject = new JSONObject(s);

            if(Integer.parseInt(parentObject.getString("status"))==200){
                appPreference.set_pref(appPreference.CONTACTSYNC,"1");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
