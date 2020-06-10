package com.e.antcolony;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Languages: a class that handles the How To Play page of the app. Child of Settings.
 *
 * @author Aidan Andrucyk
 * @version June 10, 2020
 */
public class Languages extends AppCompatActivity {

    Button okay;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_languages);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        // set actionbar
        //ActionBar actionBar = getSupportActionBar();
        // actionBar.setTitle(getResources().getString(R.string.app_name));

        Button changeLang = findViewById(R.id.french);
        changeLang.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (howToPlayOkayButton) is clicked.
             *
             * @param v used when a view is clicked.
             */

            @Override
            public void onClick(View v) {
                // show alert dialogue
                showChangeLanguageDialog();
            }
        });

        // Back Button
        okay = findViewById(R.id.languagesOkayButton);
        okay.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (howToPlayOkayButton) is clicked.
             *
             * @param v used when a view is clicked.
             */

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // create a separate strings.xml for each language first
    private void showChangeLanguageDialog() {
        // array of languages to display in alert dialogue
        final String[] listItems = {"Français", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Languages.this);
        mBuilder.setTitle("Select Language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //  French
                    setLocale("fr");
                    recreate();
                }
                // should be else
                else if (which == 1) {
                    //  French
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

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}