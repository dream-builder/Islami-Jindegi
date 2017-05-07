package com.islamijindegi.islamijindegi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Shahed on 4/17/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        String msg      = remoteMessage.getData().get("body");
        String title    = remoteMessage.getData().get("title");
        String postId    = remoteMessage.getData().get("postid");
        //String clickAction =remoteMessage.getData().get("click_action");

        String clickAction = remoteMessage.getNotification().getClickAction();

        //Log.d("action",clickAction);

        if(clickAction.equals("NotificationAction")) {

            intent = new Intent(clickAction);
            intent.putExtra("msg", msg);
            intent.putExtra("title", title);
            intent.putExtra("postid", postId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }
        else
            intent= new Intent(this,MainActivity.class);



        //Log.d("Push notice",msg);

        showNotification(msg,intent);
    }



    private void showNotification(String message, Intent i ) {


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("IslamiJindegi")
                .setContentText(message)
                .setSmallIcon(R.drawable.app_logo)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }
}
