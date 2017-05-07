package com.islamijindegi.islamijindegi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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

/**
 * Created by Shahed on 3/10/2017.
 */

public class WebService extends AsyncTask<String, String,String>{

    Context context;
    private ProgressDialog Dialog;
    private AppPreference appPreference;
    private Integer loop = null;
    DatabaseHelper DB;

    public WebService(Context context) {
        this.context = context;
        Dialog = new ProgressDialog(context);
        DB = new DatabaseHelper(context);
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
        protected void onPreExecute() {
            super.onPreExecute();
            DB.openDB();

            String cp= appPreference.get_pref(appPreference.LOOP);
            String tp = appPreference.get_pref(appPreference.SYNCPAGE);

            if(Integer.parseInt(cp)> Integer.parseInt(tp))
                cp=tp;

            Dialog.setMessage("Getting data from web server.... pages " + cp +"/"+tp);
            Dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String tp="0",action=null;
            JSONObject jo;

            if(appPreference.get_pref(appPreference.WEBSYNCSTART).equals("1"))
            {


                try {
                    jo = new JSONObject(result);

                    tp = jo.getString("total_page");
                    action = jo.getString("action");

                    if(action.equals("sync") && Integer.parseInt(tp)>0){
                        appPreference.set_pref(appPreference.SYNCPAGE,tp);

                        //Start download all data
                        appPreference.set_pref(appPreference.WEBSYNCSTART,"2");

                        //Log.d("SYNC PAGE",appPreference.get_pref(appPreference.SYNCPAGE));
                    }
                    else{
                        Toast.makeText(context,"There is a problem in web server, please try later",Toast.LENGTH_LONG).show();
                    }


                    //Log.d("total page",tp);
                    //Log.d("Action page",action);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            if(appPreference.get_pref(appPreference.WEBSYNCSTART).equals("2")){

                loop = Integer.parseInt(appPreference.get_pref(appPreference.LOOP));

                try {
                    jo = new JSONObject(result);

                    String st=jo.getString("status");
                    JSONArray jA= jo.getJSONArray("posts");

                    if(st.equals("200")){
                        updateRecord(result);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                if(loop<= Integer.parseInt(appPreference.get_pref(appPreference.SYNCPAGE))+1) {
                    //Sync Loop{
                    //Log.d("Sync status",String.valueOf(loop));
                    new WebService(context).execute("http://islamijindegi.com/webservice/?action=install&pages=" + loop);
                }
                else{
                    appPreference.set_pref(appPreference.WEBSYNCSTART,"0");
                    appPreference.set_pref(appPreference.APPFIRSTRUN,"1");
                }

                loop++;
                appPreference.set_pref(appPreference.LOOP,String.valueOf(loop));

            }

            Dialog.dismiss();
            DB.closeDB();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Dialog.setProgress(Integer.parseInt(String.valueOf(values)));
        }


        public void updateRecord(String data){

            JSONObject parentObject = null;

            try {

                Dialog.setMessage("Updating database");
                Dialog.show();

                parentObject = new JSONObject(data);

                //Log.d("DatabaseStatus",parentObject.getString("status"));

                JSONArray parentArray = parentObject.getJSONArray("posts");

                for(int i=0; i<parentArray.length();i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    //post.put(DB.POSTLASTUPDATE,finalObject.getString(currentDateandTime));

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
                            "0"
                    );

                }
                Dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            finally {

            }

        }



}
