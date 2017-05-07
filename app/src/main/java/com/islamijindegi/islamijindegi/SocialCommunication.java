package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Shahed on 3/16/2017.
 */

public class SocialCommunication {

    Context context;

    public SocialCommunication(Context context) {
        this.context = context;
    }


    public void share(String subject,String msg){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,msg);

        context.startActivity(Intent.createChooser(intent,"Share using"));
    }
}
