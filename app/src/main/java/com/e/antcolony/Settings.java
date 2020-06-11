package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import java.util.Locale;

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
    Button selectLanguage;
    Button credits;
    Button rateUs;
    Button share;
    Button visit;
    Button privacy;
    Button termsOfUse;
    Button okay;

    // Constants
    private static final String SHARED_PREF = "sharedPref";
    private static final String MUSIC_STATE = "musicState";
    private static final String SOUND_EFFECT_STATE = "soundEffectState";
    private static final String SETTINGS = "Settings";
    private static final String MY_LANG = "My_Lang";
    private static final float MUSIC_ON = .1f;
    private static final float MUSIC_OFF = 0f;
    private static final float SOUND_EFFECT_ON = 1f;


    // variables for SavedPreferences functionality
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
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

        // set actionbar
        //ActionBar actionBar = getSupportActionBar();
        // actionBar.setTitle(getResources().getString(R.string.app_name));

        musicSwitch = (Switch) findViewById(R.id.musicSwitch);
        soundEffectSwitch = (Switch) findViewById(R.id.soundEffectSwitch);

        // SharedPreferences default values
        prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        musicSwitch.setChecked(prefs.getBoolean(MUSIC_STATE, true));
        // Music Switch
        musicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSwitch.isChecked()) {
                    Toast.makeText(getBaseContext(), "Music On", Toast.LENGTH_SHORT).show();
                    MusicService.mPlayer.setVolume(MUSIC_ON, MUSIC_ON);
                    editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                    editor.putBoolean(MUSIC_STATE, true);
                    editor.commit();
                    musicSwitch.setChecked(true);
                }
                else {
                    Toast.makeText(getBaseContext(), "Music Off", Toast.LENGTH_SHORT).show();
                    MusicService.mPlayer.setVolume(MUSIC_OFF, MUSIC_OFF);
                    editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                    editor.putBoolean(MUSIC_STATE, false);
                    editor.commit();
                    musicSwitch.setChecked(false);
                }
            }
        });

        // Sound Effect Switch
        //prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        soundEffectSwitch.setChecked(prefs.getBoolean(SOUND_EFFECT_STATE, true));
        soundEffectSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundEffectSwitch.isChecked()) {
                    Toast.makeText(getBaseContext(), "Sound Effect On", Toast.LENGTH_SHORT).show();
                    MainActivity.workSound.setVolume(SOUND_EFFECT_ON, SOUND_EFFECT_ON);
                    MainActivity.growSound.setVolume(SOUND_EFFECT_ON, SOUND_EFFECT_ON);
                    MainActivity.liftSound.setVolume(SOUND_EFFECT_ON, SOUND_EFFECT_ON);
                    MainActivity.biteSound.setVolume(SOUND_EFFECT_ON, SOUND_EFFECT_ON);
                    MainActivity.nouSound.setVolume(SOUND_EFFECT_ON, SOUND_EFFECT_ON);
                    PopUpgrade.forTheQueenSound.setVolume(SOUND_EFFECT_ON, SOUND_EFFECT_ON);
                    editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                    editor.putBoolean(SOUND_EFFECT_STATE, true);
                    editor.commit();
                    soundEffectSwitch.setChecked(true);
                } else {
                    Toast.makeText(getBaseContext(), "Sound Effect Off", Toast.LENGTH_SHORT).show();
                    MainActivity.workSound.setVolume(MUSIC_OFF, MUSIC_OFF);
                    MainActivity.growSound.setVolume(MUSIC_OFF, MUSIC_OFF);
                    MainActivity.liftSound.setVolume(MUSIC_OFF, MUSIC_OFF);
                    MainActivity.biteSound.setVolume(MUSIC_OFF, MUSIC_OFF);
                    MainActivity.nouSound.setVolume(MUSIC_OFF, MUSIC_OFF);
                    PopUpgrade.forTheQueenSound.setVolume(MUSIC_OFF, MUSIC_OFF);
                    editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                    editor.putBoolean(SOUND_EFFECT_STATE, false);
                    editor.commit();
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

        // selectLanguage
        selectLanguage = (Button) findViewById(R.id.selectLanguage);
        selectLanguage.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(Settings.this, Languages.class));
                showChangeLanguageDialog();
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

        // Share Button
        share = findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Your body here";
                String sub = "Your Subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        // Visit Us Button
        visit = findViewById(R.id.visitButton);
        visit.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://seanjoo4.github.io/PlutosPlayground/"));
                startActivity(intent);
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

    /**
     * Alters the language of the application
     */
    private void showChangeLanguageDialog() {
        // array of languages to display in alert dialogue
        final String[] listItems = {"Français", "Español", "한국어", "中文", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);
        mBuilder.setTitle("Select Language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // select language
                switch (which) {
                    case 0:
                        // french
                        setLocale("fr");
                        recreate();
                        break;
                    case 1:
                        // spanish
                        setLocale("es");
                        recreate();
                        break;
                    case 2:
                        // korean
                        setLocale("ko");
                        recreate();
                        break;
                    case 3:
                        // chinese
                        setLocale("zh");
                        recreate();
                        break;
                    // english
                    default:
                        setLocale("en");
                        recreate();
                }
                // dismiss dialogInterface
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        // show alert dialog
        mDialog.show();
    }

    /**
     *
     * @param lang
     */
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(SETTINGS, MODE_PRIVATE).edit();
        editor.putString(MY_LANG, lang);
        editor.apply();
    }

    // load language

    /**
     *
     */
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
        String language = prefs.getString(MY_LANG, "");
        setLocale(language);
    }
}