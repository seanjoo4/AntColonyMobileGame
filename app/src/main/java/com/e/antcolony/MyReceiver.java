package com.e.antcolony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // create new intent to send to MyNewIntentService.java
        context.startService(new Intent(context, MyNewIntentService.class));
    }
}
