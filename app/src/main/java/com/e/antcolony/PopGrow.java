package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * PopGrow: this class is interactive with the MainActivity and is a shop. The popup displays how many idle ants are available,
 * cost to upgrade, and number of victories. This class is child of MainActivity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 5, 2020
 */

public class PopGrow extends AppCompatActivity {

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_grow);

        Intent intent = getIntent();
        final int unemployedCount = intent.getIntExtra("unemployed", 0);
        final int costToUpgrade = intent.getIntExtra("costToUpgrade", 0);
        final int victoryCount = intent.getIntExtra("victoryCount", 0);

        TextView textViewIdleAnts = findViewById(R.id.idle_ants);
        textViewIdleAnts.setText("Idle Ants: " + unemployedCount);
        TextView textViewCostToUpgrade = findViewById(R.id.cost_to_upgrade);
        textViewCostToUpgrade.setText("Cost to Upgrade: " + costToUpgrade);
        TextView textViewVictory = findViewById(R.id.biteVictory);
        textViewVictory.setText("Number of Victories: " + victoryCount);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (backButton) is clicked.
             *
             * @param v used when a view is clicked.
             */

            @Override
            public void onClick(View v) {
                int result = unemployedCount - costToUpgrade;
                Intent aftermath = new Intent();
                aftermath.putExtra("result", result);
                setResult(RESULT_OK, aftermath);
                finish();
            }
        });
    }
}