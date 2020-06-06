package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Settings: this class is where all the settings are implemented for the app. This class is a child of MainActivity.
 *
 * @author Sean Joo
 * @version June 5, 2020
 */

public class Settings extends AppCompatActivity {

    Switch musicSwitch;
    Switch soundEffectSwitch;
    Button howToPlayButton;
    Button credits;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Music Switch
        musicSwitch = (Switch) findViewById(R.id.musicSwitch);
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * This function checks if the switch is checked or not.
             *
             * @param buttonView checks if the state has changed.
             * @param isChecked if it is checked, it returns true, if not, false.
             */

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(), "Music On", Toast.LENGTH_SHORT).show();
                    MusicService.mPlayer.setVolume(.1f, .1f);
                    musicSwitch.setChecked(true);
                } else {
                    Toast.makeText(getBaseContext(), "Music Off", Toast.LENGTH_SHORT).show();
                    MusicService.mPlayer.setVolume(0f, 0f);
                    musicSwitch.setChecked(false);
                }
            }
        });

        // Sound Effect Switch
        soundEffectSwitch = (Switch) findViewById(R.id.soundEffectSwitch);
        soundEffectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * This function checks if the switch is checked or not.
             *
             * @param buttonView checks if the state has changed.
             * @param isChecked if it is checked, it returns true, if not, false.
             */

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(), "Sound Effect On", Toast.LENGTH_SHORT).show();
                    MainActivity.workSound.setVolume(1f, 1f);
                    MainActivity.growSound.setVolume(1f, 1f);
                    MainActivity.liftSound.setVolume(1f, 1f);
                    MainActivity.biteSound.setVolume(1f, 1f);
                    soundEffectSwitch.setChecked(true);
                } else {
                    Toast.makeText(getBaseContext(), "Sound Effect Off", Toast.LENGTH_SHORT).show();
                    MainActivity.workSound.setVolume(0f, 0f);
                    MainActivity.growSound.setVolume(0f, 0f);
                    MainActivity.liftSound.setVolume(0f, 0f);
                    MainActivity.biteSound.setVolume(0f, 0f);
                    soundEffectSwitch.setChecked(false);
                }
            }
        });

        // How To Play Button
        howToPlayButton = (Button) findViewById(R.id.howToPlayButton);
        howToPlayButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, HowToPlay.class));
            }
        });

        // Credits Button
        credits = findViewById(R.id.creditsButton);
        credits.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Credits.class));
            }
        });

        Intent intent = getIntent();

        // Back Button
        Button okay = findViewById(R.id.okaySetting);
        okay.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}