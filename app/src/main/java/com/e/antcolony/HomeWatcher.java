package com.e.antcolony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * This class is used in tandem to the MusicService class to support the music player by reacting to the use of the home button.
 *
 * @author Aidan Andrucyk
 * @version June 5, 2020
 */
public class HomeWatcher {

    //static final String TAG = "hg";
    private Context mContext;
    private OnHomePressedListener mListener;
    private IntentFilter mFilter;
    private InnerReceiver mReceiver;

    /**
     * This method will close the system dialogs.
     * @param context represents phone state.
     */
    public HomeWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * This function sets the listener for the home button and initializes a InnerReceiver.
     * @param listener represents the listener for the system nav bar home button.
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mReceiver = new InnerReceiver();
    }

    /**
     * This method checks if the music media receiver is not null to non-statically alter the context of the media.
     */
    public void startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter);
        }
    }

    /**
     * his method checks if the music media receiver is not null to call to stop.
     */
    public void stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    /**
     * This class primarily serves to receive input from the listeners.
     */
    class InnerReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        /**
         * @param context represents previous events affecting the media player.
         * @param intent  represents the carried-over data.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    // logs action and reason in Logcat
                    Log.e(TAG, "action:" + action + ",reason:" + reason);
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            mListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            mListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }

    /**
     * This method calls other methods, acting as a listener for when the home button is clicked on the system nav bar.
     */
    public interface OnHomePressedListener {
        void onHomePressed();
        void onHomeLongPressed();
    }
}
