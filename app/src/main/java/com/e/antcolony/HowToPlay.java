package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * HowToPlay: a class that handles the How To Play page of the app. Child of Settings.
 *
 * @author Sean Joo
 * @version June 5, 2020
 */

public class HowToPlay extends AppCompatActivity {

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        //Back Button
        Button okay = findViewById(R.id.howToPlayOkayButton);

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