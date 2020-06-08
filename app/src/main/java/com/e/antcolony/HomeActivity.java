package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * HomeActivity: this class is when the user first opens up the app. It displays the name of the game and our company.
 *
 * @author Sean Joo
 * @version June 5, 2020
 */
public class HomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // change window colors
        // first checks if correct version
        if (android.os.Build.VERSION.SDK_INT >= 21 && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // change system nav bar color
            window.setNavigationBarColor(this.getResources().getColor(R.color.homeColor));
            // change status bar color
            window.setStatusBarColor(this.getResources().getColor(R.color.homeColor));
        }

        new Handler().postDelayed(new Runnable() {

            /**
             * This function creates the connection between HomeActivity() and MainActivity().
             */
            @Override
            public void run() {
                Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }

        }, SPLASH_TIME_OUT);

    }
}