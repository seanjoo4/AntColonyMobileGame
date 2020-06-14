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
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

/**
 * Settings: this class is where all the settings are implemented for the app. This class is a child of MainActivity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 10, 2020
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

    // variables for SavedPreferences functionality
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Constants
    public static String SOUND_EFFECT_STATE = "soundEffectState";
    public static String MUSIC_STATE = "musicState";
    public static String SHARED_PREF = "sharedPref";
    public static String SETTINGS = "Settings";
    public static String MY_LANG = "My_Lang";
    public static String SHARE_USING = "Share Using";
    public static String EMPTY_STRING = "";
    public static float MUSIC_ON = .1f;
    public static float SOUND_OFF = 0f;
    public static int CURRENT_ANDROID_VERSION = 21;

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
        if (android.os.Build.VERSION.SDK_INT >= CURRENT_ANDROID_VERSION && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                } else {
                    Toast.makeText(getBaseContext(), "Music Off", Toast.LENGTH_SHORT).show();
                    MusicService.mPlayer.setVolume(SOUND_OFF, SOUND_OFF);
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
                    Toast.makeText(getBaseContext(), SOUND_EFFECT_STATE, Toast.LENGTH_SHORT).show();
                    MainActivity.workSound.setVolume(1f, 1f);
                    MainActivity.growSound.setVolume(1f, 1f);
                    MainActivity.liftSound.setVolume(1f, 1f);
                    MainActivity.biteSound.setVolume(1f, 1f);
                    MainActivity.nouSound.setVolume(1f, 1f);
                    MainActivity.forTheQueenSound.setVolume(1f, 1f);
                    editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                    editor.putBoolean(SOUND_EFFECT_STATE, true);
                    editor.commit();
                    soundEffectSwitch.setChecked(true);
                } else {
                    Toast.makeText(getBaseContext(), "Sound Effect Off", Toast.LENGTH_SHORT).show();
                    MainActivity.workSound.setVolume(SOUND_OFF, SOUND_OFF);
                    MainActivity.growSound.setVolume(SOUND_OFF, SOUND_OFF);
                    MainActivity.liftSound.setVolume(SOUND_OFF, SOUND_OFF);
                    MainActivity.biteSound.setVolume(SOUND_OFF, SOUND_OFF);
                    MainActivity.nouSound.setVolume(SOUND_OFF, SOUND_OFF);
                    MainActivity.forTheQueenSound.setVolume(SOUND_OFF, SOUND_OFF);
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

        // select language button
        selectLanguage = (Button) findViewById(R.id.selectLanguage);
        selectLanguage.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                // create a pop up for user to select from dialog (combo box type feature)
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
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                myIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(myIntent, SHARE_USING));
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

    // Outside of onCreate!


    /**
     * This method shows the interface for selecting which language the user would prefer.
     */
    // create pop-up asking user which language they would like to select
    private void showChangeLanguageDialog() {

        // array of languages to display in alert dialogue
        final String[] listItems = {"Français", "Español", "한국어", "中文", "English"};

        // create new alert dialogue
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);

        // allow user to select preferred language and exit upon selection
        mBuilder.setTitle(getResources().getString(R.string.select_lang));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            /**
             * This is a method which allow
             *
             * @param dialog represents the interface.
             * @param which  corresponds to which location in the listItems array the respective language corresponds to.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // select language
                switch (which) {
                    case 0:
                        // french
                        setLocale("fr");
                        break;
                    case 1:
                        // spanish
                        setLocale("es");
                        break;
                    case 2:
                        // korean
                        setLocale("ko");
                        break;
                    case 3:
                        // chinese
                        setLocale("zh");
                        break;
                    // english
                    default:
                        setLocale("en");
                }

                // dismiss the interface
                dialog.dismiss();

                // recreates the application so that text are localized according the language
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        AlertDialog mDialog = mBuilder.create();
        // show alert dialog
        mDialog.show();
    }

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
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
        String language = prefs.getString(MY_LANG, EMPTY_STRING);
        setLocale(language);
    }
}