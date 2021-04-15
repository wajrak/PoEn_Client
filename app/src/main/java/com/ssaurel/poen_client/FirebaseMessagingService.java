package com.ssaurel.poen_client;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ashish.kapoor on 04.09.2017.
 */

public class FirebaseMessagingService extends  com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG="FirebaseMessagingServic";

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String strTitle=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();
        Log.d(TAG,"onMessageReceived: Message Received: \n" +
        "Title: " + strTitle + "\n" +
        "Message: "+ message);

        sendNotification(strTitle,message);
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title,String messageBody) {


    }
}