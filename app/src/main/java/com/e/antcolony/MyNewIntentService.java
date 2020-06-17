package com.e.antcolony;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationManagerCompat;

/**
 * MyNewIntentService: the class that creates new notifications intents.
 *
 * @author Aidan Andrucyk
 * @version June 12, 2020
 */
public class MyNewIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID = 2;

    /**
     * Constructor for intent service
     */
    public MyNewIntentService() {
        // super();
    }

    /**
     * Creates notification with data
     *
     * @param intent represents transferred data from main activity
     */
    @Override
    protected void onHandleWork(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        // app title as content title
        builder.setContentTitle(getResources().getString(R.string.app_name));
        // bite victory message as content text
        builder.setContentText(getResources().getString(R.string.bitevictorymessage));
        builder.setSmallIcon(R.drawable.ant_logo);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}

