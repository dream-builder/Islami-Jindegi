package com.islamijindegi.islamijindegi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalatTiming extends Fragment implements PrayerTimeResponse {

    PrayerTime prayerTime;
    AppPreference appPreference;
    JSONObject JO;
    View view;
    Check check;

    TextView fazar,duhur,asar,magrib,isha,sunrise,sunset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_salat_timing,container,false);
        this.view=view;

        init();

        return view;
    }

    private void init() {

        fazar = (TextView) view.findViewById(R.id.fazarTV);
        duhur = (TextView) view.findViewById(R.id.duhurTV);
        asar = (TextView) view.findViewById(R.id.asarTV);
        magrib = (TextView) view.findViewById(R.id.magribTV);
        isha = (TextView) view.findViewById(R.id.ishaTV);
        sunrise = (TextView) view.findViewById(R.id.sunriseTV);
        sunset = (TextView) view.findViewById(R.id.sunsetTV);

        check = new Check(getContext());
        appPreference = new AppPreference(getContext());


        load_timing();

        //syncTiming();
    }


    private void syncTiming(){

        prayerTime = new PrayerTime(getContext());
        prayerTime.delegate = this;

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Salat timing");
        builder.setMessage("Salat time is no updated. Please update now.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(check.checkInternet()) {
                    prayerTime.execute("http://api.aladhan.com/timingsByCity?city=Dhaka&country=BD&method=2&school=1");
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }


    private  void load_timing(){

        String timing = appPreference.get_pref(appPreference.SALATTIMING);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());



        if(!timing.equals("0")){

            try {
                JO = new JSONObject(timing);

                if(JO.getString("date").equals(date)){
                    loadData(JO.getString("Fajr"),JO.getString("Dhuhr"),JO.getString("Asr"),JO.getString("Maghrib"),JO.getString("Isha"),JO.getString("Sunrise"),JO.getString("Sunset"));
                }
                else
                    syncTiming();

            } catch (JSONException e) {
                e.printStackTrace();

                syncTiming();
            }

        }
        else
            syncTiming();

    }


    private void loadData(String...params){

        fazar.setText("ভোর " + convertToBn(params[0]));
        duhur.setText("দুপুর "+ convertToBn(params[1]));
        asar.setText("বিকাল " + convertToBn(params[2]));
        magrib.setText("সন্ধ্যা  "+ convertToBn(params[3]));
        isha.setText("রাত "+ convertToBn(params[4]));
        sunrise.setText("সকাল " + convertToBn(params[5]));
        sunset.setText("সন্ধা "+ convertToBn(params[6]));



    }

    @Override
    public void syncCompleted(String output) {

        String timing = appPreference.get_pref(appPreference.SALATTIMING);
        try {
            JO = new JSONObject(timing);
                loadData(JO.getString("Fajr"),JO.getString("Dhuhr"),JO.getString("Asr"),JO.getString("Maghrib"),JO.getString("Isha"),JO.getString("Sunrise"),JO.getString("Sunset"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String convertToBn(String s){

        String ar[] = s.split("");

        Integer PM=Integer.parseInt(ar[1]+ar[2]);

        if(PM>12)
        {
            PM -=12;
            String PM1[]=String.valueOf(PM).split("");

            if(PM1[0].equals(""))
                PM1[0]="০";

            ar[1]=PM1[0];
            ar[2]=PM1[1];

            //Log.d("PM Value",PM1[0]+PM1.length);
        }

        for(Integer i=0; i<ar.length;i++){

            switch (ar[i]){
                case "0":
                    ar[i]="০";
                    break;

                case "1":
                    ar[i]="১";
                    break;
                case "2":
                    ar[i]="২";
                    break;
                case "3":
                    ar[i]="৩";
                    break;
                case "4":
                    ar[i]="৪";
                    break;
                case "5":
                    ar[i]="৫";
                    break;
                case "6":
                    ar[i]="৬";
                    break;
                case "7":
                    ar[i]="৭";
                    break;
                case "8":
                    ar[i]="৮";
                    break;
                case "9":
                    ar[i]="৯";
                    break;
            }

        }


        String finalString = "";

        for (Integer i =0;i<ar.length;i++)
            finalString += ar[i];

        //Log.d("time",finalString);
        return finalString;
    }
}
