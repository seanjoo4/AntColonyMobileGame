package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * PopUpgrade: this class is employed to show the user statistics and colony upgrade requirements upon pressing the arrow button from the parent activity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 8, 2020
 */
public class PopUpgrade extends AppCompatActivity {
    // top colony attributes
    //String currentTier = "Tribal Village";
    //int gloryScore = 0;
    // number of territories and grows required to upgrade tier
    int growsToUpgrade = 2;
    int tempAnt = 0;
    int territoriesCount = 1;
    int totalGrows = 2;

    // button music when upgrade tier is pressed
    public static MediaPlayer forTheQueenSound;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_upgrade);

        // change window colors
        // first checks if correct version
        if (android.os.Build.VERSION.SDK_INT >= 21 && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // change system nav bar color
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorDark));
            // change status bar color
            window.setStatusBarColor(this.getResources().getColor(R.color.colorDark));
        }


        Intent intent = getIntent();

        // SEAN can you add a bunch of things i don't know how to do this part
        final int totalAntsCount = intent.getIntExtra("totalAnts", 0);
        final int unemployedCount = intent.getIntExtra("unemployed", 0);
        final int strengthCount = intent.getIntExtra("strengthCount", 0);
        final int victoryCount = intent.getIntExtra("victoryCount", 0);
        final int lossCount = intent.getIntExtra("lossCount", 0);
        final int successfulLift = intent.getIntExtra("successfulLift", 0);
        final int unsuccessfulLift = intent.getIntExtra("unsuccessfulLift", 0);
        final int growPressed = intent.getIntExtra("growPressed", 0);
        final int antTitle = intent.getIntExtra("antTitle", 0);
        final int gloryScoreTitle = intent.getIntExtra("gloryScore", 0);
        final int totalTerritories = intent.getIntExtra("totalTerritories", 0);
        final int totalGrows = intent.getIntExtra("totalGrows", 0);

        // update glory score
        MainActivity.gloryScore = victoryCount * 30 + successfulLift + growPressed * 5;

        // top colony aspects
        final TextView textGloryScore = findViewById(R.id.gloryScore);
        final TextView textCurrentTier = findViewById(R.id.currentTier);

        // setting text for top colony aspects
        textGloryScore.setText(getResources().getText(R.string.colony_glory_score) + " " + MainActivity.gloryScore);
        textCurrentTier.setText(getResources().getText(R.string.tier) + " " + MainActivity.tier);

        // upgrade requirements
        final TextView textTerritoryRequirement = findViewById(R.id.territoryRequirementText);
        final TextView textNumberOfGrowsRequirement = findViewById(R.id.numberOfGrowsRequirementText);

        // setting text for upgrade requirements
        textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + totalTerritories);
        textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + totalGrows);

        // colony stats
        TextView textViewTotalAnts = findViewById(R.id.totalAntsText);
        TextView textViewIdleAnts = findViewById(R.id.idleAntsText);
        final TextView textColonyStrength = findViewById(R.id.colonyStrengthText);
        TextView textViewVictory = findViewById(R.id.biteVictory);
        TextView textViewLoss = findViewById(R.id.biteLoss);
        TextView textViewSuccessful = findViewById(R.id.liftSuccessful);
        TextView textViewUnsuccessful = findViewById(R.id.liftUnsuccessful);
        TextView textViewGrowPressed = findViewById(R.id.growPressed);

        // setting text for colony stats
        textViewTotalAnts.setText(getResources().getText(R.string.total_ants) + " " + totalAntsCount);
        textViewIdleAnts.setText(getResources().getText(R.string.idle_ants) + " " + unemployedCount);
        textColonyStrength.setText(getResources().getText(R.string.colony_strength) + " " + strengthCount);
        textViewVictory.setText(getResources().getText(R.string.territories_owned) + " " + victoryCount);
        textViewLoss.setText(getResources().getText(R.string.bite_defeats) + " " + lossCount);
        textViewSuccessful.setText(getResources().getText(R.string.successful_lifts) + " " + successfulLift);
        textViewUnsuccessful.setText(getResources().getText(R.string.unsuccessful_lifts) + " " + unsuccessfulLift);
        textViewGrowPressed.setText(getResources().getText(R.string.number_of_grows) + " " + growPressed);

        // upgrade button
        // work sound to be played upon clicking the queen button
        forTheQueenSound = MediaPlayer.create(this, R.raw.forthequeen);
        Button upgradeButton = findViewById(R.id.upgradeButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (backButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                if (victoryCount < MainActivity.territoriesRequired || growPressed < MainActivity.growsRequired) {
                    MainActivity.nouSound.start();
                    Toast.makeText(
                            PopUpgrade.this, "must have " + MainActivity.territoriesRequired + " territories and " + MainActivity.growsRequired + " G R O W S", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                forTheQueenSound.start();
                // ADD LATER do something when upgrading tier (ex: dynamic background in MainActivity)
                // change colony strength
                textColonyStrength.setText(getResources().getText(R.string.colony_strength) + " " + strengthCount); // add actual var
                // update/double glory score
                MainActivity.gloryScore *= 2;
                textGloryScore.setText(getResources().getText(R.string.colony_glory_score) + " " + MainActivity.gloryScore);
                // upgrade the tier status
                //currentTier = upgradeTierName(currentTier);
                MainActivity.tier = upgradeTierName(MainActivity.tier);

                // REALLY  importanT antTitle = upgradeTierName(antTitle);
                textCurrentTier.setText(getResources().getText(R.string.tier) + " " + MainActivity.tier);
                // augment the requirements for next colony upgrade
                MainActivity.territoriesRequired *= 3;
                MainActivity.growsRequired *= 2;

                textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + MainActivity.territoriesRequired);
                textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + MainActivity.growsRequired);
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


    /**
     * This function changes the current tier and dynamically changes the background of the main activity.
     *
     * @param tier represents the current tier.
     * @return String representing the new tier.
     */
    // changes the tier name by comparing the current name with the next names in the progression
    public static String upgradeTierName(String tier) {
        // ordered from easiest to hardest to reduce the number of comparisons
        switch (tier) {
            // territories required increase by a factor of 3 starting at 2
            // grows required increase by a factor of 2 starting at 6
            case "Tribal Village":
                // territories required: 2
                // grows required: 6
                // programmatically change the background of the main activity
                MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background2);
                return "Burgeoning Estate";
            case "Burgeoning Estate":
                // territories required: 6
                // grows required: 12
                // programmatically change the background of the main activity
                MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background3);
                return "Humble County";
            case "Humble County":
                // territories required: 18
                // grows required: 24
                // programmatically change the background of the main activity
                MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background3);
                return "Blessed Duchy";
            case "Blessed Duchy":
                // territories required: 54
                // grows required: 48
                // programmatically change the background of the main activity
                MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background4);
                return "Glorious Queendom";
            case "Glorious Queendom":
                // territories required: 162
                // grows required: 96
                // programmatically change the background of the main activity
                MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background5);
                return "Divine Empire";
            // unlimited tiering system but Glorious is just added to the front of the current tier at the point when we don't expect anyone to be able to reach
            default:
                // below are for the first glorious append
                // territories required: 64
                // grows required: 192
                // programmatically change the background of the main activity
                MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background6);
                return "Glorious" + tier;
        }
    }
}
