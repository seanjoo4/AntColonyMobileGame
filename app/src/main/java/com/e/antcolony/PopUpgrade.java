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
    String currentTier = "Tribal Village";
    int gloryScore = 0;
    // number of territories and grows required to upgrade tier
    int territoriesToUpgrade = 2;
    int growsToUpgrade = 6;

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

        // update glory score
        gloryScore = victoryCount * 30 + successfulLift + growPressed * 5;

        // top colony aspects
        final TextView textGloryScore = findViewById(R.id.gloryScore);
        final TextView textCurrentTier = findViewById(R.id.currentTier);

        // setting text for top colony aspects
        textGloryScore.setText(getResources().getText(R.string.colony_glory_score) + " " + gloryScore);
        textCurrentTier.setText(getResources().getText(R.string.tier) + " " + currentTier);

        // upgrade requirements
        final TextView textTerritoryRequirement = findViewById(R.id.territoryRequirementText);
        final TextView textNumberOfGrowsRequirement = findViewById(R.id.numberOfGrowsRequirementText);

        // setting text for upgrade requirements
        textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + territoriesToUpgrade);
        textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + growsToUpgrade);

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
        textViewTotalAnts.setText(getResources().getText(R.string.total_ants) + " " + totalAntsCount); // add actual var
        textViewIdleAnts.setText(getResources().getText(R.string.idle_ants) + " " + unemployedCount);
        textColonyStrength.setText(getResources().getText(R.string.colony_strength) + " " + strengthCount); // add actual var
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
                if (victoryCount < territoriesToUpgrade || growPressed < growsToUpgrade) {
                    MainActivity.nouSound.start();
                    Toast.makeText(
                            PopUpgrade.this, "must have " + territoriesToUpgrade + " territories and " + growsToUpgrade + " G R O W S", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                forTheQueenSound.start();
                // ADD LATER do something when upgrading tier (ex: dynamic background in MainActivity)
                // change colony strength
                textColonyStrength.setText(getResources().getText(R.string.colony_strength) + " " + strengthCount); // add actual var
                // update/double glory score
                gloryScore *= 2;
                textGloryScore.setText(getResources().getText(R.string.colony_glory_score) + " " + gloryScore);
                // upgrade the tier status
                currentTier = upgradeTierName(currentTier);
                textCurrentTier.setText(getResources().getText(R.string.tier) + " " + currentTier);
                // augment the requirements for next colony upgrade
                territoriesToUpgrade *= 3;
                growsToUpgrade *= 2;
                textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + territoriesToUpgrade);
                textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + growsToUpgrade);
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

    // changes the tier name by comparing the current name with the next names in the progression
    public static String upgradeTierName(String tier) {
        // ordered from easiest to hardest to reduce the number of comparisons
        switch (tier) {
            // territories required increase by a factor of 3 starting at 2
            // grows required increase by a factor of 2 starting at 6
            case "Tribal Village":
                // territories required: 2
                // grows required: 6
                return "Burgeoning Estate";
            case "Burgeoning Estate":
                // territories required: 6
                // grows required: 12
                return "Humble County";
            case "Humble County":
                // territories required: 18
                // grows required: 24
                return "Blessed Duchy";
            case "Blessed Duchy":
                // territories required: 54
                // grows required: 48
                return "Glorious Queendom";
            case "Glorious Queendom":
                // territories required: 162
                // grows required: 96
                return "Divine Empire";
            // unlimited tiering system but Glorious is just added to the front of the current tier at the point when we don't expect anyone to be able to reach
            default:
                // below are for the first glorious append
                // territories required: 64
                // grows required: 192
                return "Glorious" + tier;
        }
    }
}
