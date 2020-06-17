package com.e.antcolony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * MyReceiver: the class that receives notifications broadcasts.
 *
 * @author Aidan Andrucyk
 * @version June 12, 2020
 */
public class MyReceiver extends BroadcastReceiver {

    /**
     * Receiving data
     */
    public MyReceiver() {
    }

    /**
     *
     * @param context the receiver is running.
     * @param intent the Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // create new intent to send to MyNewIntentService.java
        context.startService(new Intent(context, MyNewIntentService.class));
    }
}
