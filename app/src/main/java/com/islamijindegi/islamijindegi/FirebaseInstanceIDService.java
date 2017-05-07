package com.islamijindegi.islamijindegi;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Shahed on 4/17/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    AppPreference appPreference;

    @Override
    public void onTokenRefresh() {

        appPreference = new AppPreference(this);
        //super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

        appPreference.set_pref(appPreference.FCMTOKEN,token);
        registerToken(token);
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
