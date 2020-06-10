package com.e.antcolony;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * Languages: a class that handles the How To Play page of the app. Child of Settings.
 *
 * @author Aidan Andrucyk
 * @version June 10, 2020
 */
public class Languages extends Activity {

    Button okay;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);


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
}