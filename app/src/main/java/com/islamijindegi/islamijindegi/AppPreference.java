package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Shahed on 3/8/2017.
 */

public class AppPreference {
    public static final String POSTTYPE="post_type";
    public static final String POSTCATEGORY="post_category";
    public static final String POSTAUTHOR ="post_author";
    public static final String APPFIRSTRUN="app_first_run";
    public static final String SYNCPAGE="sync_pages";
    public static final String LOOP="sync_loop";
    public static final String FORGIRLS="for_girls";
    public static final String LOADCATEGORYTOFRAGMENT="0";
    public static final String FCMTOKEN="fcm_token";
    public static final String FCMOLDTOKEN="fcm_old_token";
    public static final String SALATTIMING="salah_timing";
    public static final String CONTACTSYNC="contact_sync";
    public static final String WEBSYNCSTART="sync_sync";





    Context context;

    private SharedPreferences pref;

    public AppPreference(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("APP_PREF",context.MODE_PRIVATE);
    }


    public void  set_pref(String key, String val){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,val);
        editor.commit();
    }

    public String get_pref(String key){
        return pref.getString(key,"0");
    }


}
