package com.e.antcolony;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * This class allows for music to be played in the background for active points of the main activity's lifecycle.
 *
 * @author Aidan Andrucyk
 * @version June 5, 2020
 */
public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    public static MediaPlayer mPlayer;
    private int length = 0;

    /**
     * This class represents the music service.
     */
    public MusicService() {
    }

    /**
     * This function allows the main activity to bind to the service.
     */
    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    /**
     * @param arg0 represents
     * @return music binder
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    /**
     *  Establishes music media player its attributes
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // music is sourced from a royalty free music service, Bensound.com
        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.setOnErrorListener(this);

        // creates loop for music
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(.1f, .1f);
        }


        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            /**
             *
             * @param mp
             * @param what
             * @param extra
             * @return
             */
            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    // make responsive for the app lifecycles

    /**
     * This function starts the starting of music services.
     * @param intent represents carried data.
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPlayer != null) {
            mPlayer.start();
        }
        return START_NOT_STICKY;
    }

    /**
     * This function stops music from playing.
     */
    public void pauseMusic() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                length = mPlayer.getCurrentPosition();
            }
        }
    }

    /**
     * This function resumes music after it has been paused.
     */
    public void resumeMusic() {
        if (mPlayer != null) {
            if (!mPlayer.isPlaying()) {
                mPlayer.seekTo(length);
                mPlayer.start();
            }
        }
    }

    /**
     * This function initiates music to play.
     */
    public void startMusic() {
        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            // reduce volume
            mPlayer.setVolume(.1f, .1f);
            mPlayer.start();
        }

    }

    /**
     * This function stops music from playing.
     */
    public void stopMusic() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * This function stops music from playing when the application is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    /**
     * This function stops music from playing when error occurs and notifies end user.
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player has encountered an error", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }
}
