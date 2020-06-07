package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopUpgrade extends AppCompatActivity {
    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_upgrade);
        Intent intent = getIntent();
        final int unemployedCount = intent.getIntExtra("unemployed", 0);
        final int victoryCount = intent.getIntExtra("victoryCount", 0);
        final int lossCount = intent.getIntExtra("lossCount", 0);
        final int successfulLift = intent.getIntExtra("successfulLift", 0);
        final int unsuccessfulLift = intent.getIntExtra("unsuccessfulLift", 0);
        final int growPressed = intent.getIntExtra("growPressed", 0);

        TextView textViewIdleAnts = findViewById(R.id.idle_ants);
        textViewIdleAnts.setText("Idle Ants: " + unemployedCount);
        TextView textViewVictory = findViewById(R.id.biteVictory);
        textViewVictory.setText("Territories Owned: " + victoryCount);
        TextView textViewLoss = findViewById(R.id.biteLoss);
        textViewLoss.setText("Bite Defeats: " + lossCount);
        TextView textViewSuccessful = findViewById(R.id.liftSuccessful);
        textViewSuccessful.setText("Successful Lifts: " + successfulLift);
        TextView textViewUnsuccessful = findViewById(R.id.liftUnsuccessful);
        textViewUnsuccessful.setText("Unsuccessful Lifts: " + unsuccessfulLift);
        TextView textViewGrowPressed = findViewById(R.id.growPressed);
        textViewGrowPressed.setText("Grow Pressed: " + growPressed);

        // upgrade button
        Button upgradeButton = findViewById(R.id.upgradeButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (backButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {

            }
        });

        // back button
        Button backButton = findViewById(R.id.upgradeOkayButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (backButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                Intent aftermath = new Intent();
                setResult(RESULT_OK, aftermath);
                finish();
            }
        });
    }
}
