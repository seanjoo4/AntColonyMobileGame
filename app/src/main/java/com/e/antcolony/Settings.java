package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Settings: this class is where all the settings are implemented for the app. This class is a child of MainActivity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 8, 2020
 */
public class Settings extends AppCompatActivity {

    Switch musicSwitch;
    Switch soundEffectSwitch;
    Button howToPlayButton;
    Button credits;
    Button rateUs;
    Button privacy;
    Button termsOfUse;
    Button okay;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // change window color from black to maroon
        // first checks if correct version
        if (android.os.Build.VERSION.SDK_INT >= 21 && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // change status bar color
            window.setStatusBarColor(this.getResources().getColor(R.color.liftLoss));
            // change system nav bar color
            getWindow().setNavigationBarColor(getResources().getColor(R.color.liftLoss));
        }

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

        // Rate Us Button
        rateUs = findViewById(R.id.rateUsButton);
        rateUs.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        // Privacy Button
        privacy = findViewById(R.id.privacyButton);
        privacy.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Privacy.class));
            }
        });

        // Terms of Use Button
        termsOfUse = findViewById(R.id.termsOfUseButton);
        termsOfUse.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, TermsOfUse.class));
            }
        });

        Intent intent = getIntent();

        // Back Button
        okay = findViewById(R.id.okaySetting);
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