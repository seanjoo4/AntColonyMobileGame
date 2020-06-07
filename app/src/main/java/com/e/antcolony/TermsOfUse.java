package com.e.antcolony;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * TermsOfUse: a class that handles the Terms of Use page of the app. Child of Settings.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 6, 2020
 */
public class TermsOfUse extends Activity {

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        // Back Button
        Button okay = findViewById(R.id.termsOfUseBackButton);
        okay.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (privacyBackButton) is clicked.
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
