package com.example.djikon.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.djikon.ChatViewerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        Log.i("TAG", "onMessageReceived: message send FirebaseMessaging");
        Log.i("TAG", "onMessageReceived: "+remoteMessage.getData());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("TAG", "onMessageReceived: message received FirebaseMessaging");
        if(firebaseUser != null  && sented.equals(firebaseUser.getUid())){
            sendNotification(remoteMessage);
            Log.i("TAG", "onMessageReceived: message received FirebaseMessaging");
            Log.i("TAG", "onMessageReceived: "+remoteMessage.getData());
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        Log.i("TAG", "sendNotification: "+remoteMessage.getData());

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        int J = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, ChatViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,J,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(Integer.parseInt(icon))
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSound)
                        .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if(J > 0){
            i = J;
        }

        noti.notify(i,builder.build());
    }
}
