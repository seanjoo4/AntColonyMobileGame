package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ResourceBundle;

/**
 * PopUpgrade: this class is employed to show the user statistics and colony upgrade requirements upon pressing the arrow button from the parent activity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 8, 2020
 */
public class PopUpgrade extends AppCompatActivity {

    // button music when upgrade tier is pressed
    public static MediaPlayer forTheQueenSound;

    // TextView
    TextView textViewVictory;

    // variables for SavedPreferences functionality
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Constants
    private static String SHARED_PREF = "sharedPref";
    private static String STRENGTH_COUNT = "strengthCount";
    private static String VICTORY_COUNT = "victoryCount";
    private static String SUCCESSFUL_LIFT = "successfulLift";
    private static String GROW_PRESSED = "growPressed";
    public static String CURRENT_TIER_STATE = "currentTierState";
    private static String DEFAULT_VILLAGE = "";
    private static String TOTAL_TERRITORIES_COUNT = "totalTerritoriesCount";
    private static String TOTAL_GROWS_REQUIRED = "totalGrowsRequired";
    private static int TOTAL_TERRITORIES_DEFAULT = 1;
    private static int TOTAL_GROWS_DEFAULT = 6;
    private static int DEFAULT = 0;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_upgrade);

        // set defualt tier to tier1
        DEFAULT_VILLAGE = getResources().getString(R.string.tier1);

        // change window colors
        // first checks if correct version
        // 21 Android IOS version
        if (android.os.Build.VERSION.SDK_INT >= 21 && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // change system nav bar color
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorDark));
            // change status bar color
            window.setStatusBarColor(this.getResources().getColor(R.color.colorDark));
        }

        // get data from main activity
        Intent intent = getIntent();

        // SEAN can you add a bunch of things i don't know how to do this part
        final int strengthCount = intent.getIntExtra(STRENGTH_COUNT, DEFAULT);
        final int victoryCount = intent.getIntExtra(VICTORY_COUNT, DEFAULT);
        final int successfulLift = intent.getIntExtra(SUCCESSFUL_LIFT, DEFAULT);
        final int growPressed = intent.getIntExtra(GROW_PRESSED, DEFAULT);


        // update glory score
        MainActivity.gloryScore = MainActivity.territoriesRequired * 30 + MainActivity.strength + successfulLift +
                growPressed * 2 + MainActivity.territoriesRequired * 50;

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
        //textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + MainActivity.territoriesRequired);
        //textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + MainActivity.growsRequired);

        // colony stats
        TextView textViewTotalAnts = findViewById(R.id.totalAntsText);
        TextView textViewIdleAnts = findViewById(R.id.idleAntsText);
        final TextView textColonyStrength = findViewById(R.id.colonyStrengthText);
        textViewVictory = findViewById(R.id.biteVictory);
        TextView textViewLoss = findViewById(R.id.biteLoss);
        TextView textViewSuccessful = findViewById(R.id.liftSuccessful);
        TextView textViewUnsuccessful = findViewById(R.id.liftUnsuccessful);
        TextView textViewGrowPressed = findViewById(R.id.growPressed);


        // SharedPreferences default values
        prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        MainActivity.tier = prefs.getString(CURRENT_TIER_STATE, DEFAULT_VILLAGE);
        MainActivity.territoriesRequired = prefs.getInt(TOTAL_TERRITORIES_COUNT, TOTAL_TERRITORIES_DEFAULT);
        MainActivity.growsRequired = prefs.getInt(TOTAL_GROWS_REQUIRED, TOTAL_GROWS_DEFAULT);
        MainActivity.territoriesRequired = prefs.getInt("territoriesRequired", 1);

        // set text to defaults
        textCurrentTier.setText(getResources().getText(R.string.tier) + " " + MainActivity.tier);
        textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + MainActivity.territoriesRequired);
        textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + MainActivity.growsRequired);


        // setting text for colony stats
        textViewTotalAnts.setText(getResources().getText(R.string.total_ants) + " " + MainActivity.antNumber);
        textViewIdleAnts.setText(getResources().getText(R.string.idle_ants) + " " + MainActivity.idleAntNumber);
        textColonyStrength.setText(getResources().getText(R.string.colony_strength) + " " + strengthCount);
        textViewVictory.setText(getResources().getText(R.string.territories_owned) + " " + MainActivity.territoriesClaimed);
        textViewLoss.setText(getResources().getText(R.string.bite_defeats) + " " + MainActivity.territoriesLost);
        textViewSuccessful.setText(getResources().getText(R.string.successful_lifts) + " " + MainActivity.successfulLift);
        textViewUnsuccessful.setText(getResources().getText(R.string.unsuccessful_lifts) + " " + MainActivity.unsuccessfulLift);
        textViewGrowPressed.setText(getResources().getText(R.string.number_of_grows) + " " + MainActivity.growPressed);

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

                    // alert user that they do not have enough territories and grows
                    Toast.makeText(
                            PopUpgrade.this, "must have " + MainActivity.territoriesRequired + " territories and " + MainActivity.growsRequired + " G R O W S", Toast.LENGTH_SHORT
                    ).show();

                    // prevent upgrade from occurring
                    return;
                }

                // upgrade the tier status
                // changes the tier name by comparing the current name with the next names in the progression
                // ordered from easiest to hardest to reduce the number of comparisons
                // territories required increase by a factor of 3 starting at 1
                // grows required increase by a factor of 2 starting at 6
                if (MainActivity.tier.equals(getResources().getString(R.string.tier1))) {
                    // territories required: 1
                    // grows required: 6
                    // programmatically change the background of the main activity
                    MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background2);
                    MainActivity.tier = getResources().getString(R.string.tier2);
                } else if (MainActivity.tier.equals(getResources().getString(R.string.tier2))) {
                    // territories required: 3
                    // grows required: 12
                    // programmatically change the background of the main activity
                    MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background3);
                    MainActivity.tier = getResources().getString(R.string.tier3);
                } else if (MainActivity.tier.equals(getResources().getString(R.string.tier3))) {
                    // territories required: 9
                    // grows required: 24
                    // programmatically change the background of the main activity
                    MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background4);
                    MainActivity.tier = getResources().getString(R.string.tier4);

                } else if (MainActivity.tier.equals(getResources().getString(R.string.tier4))) {
                    // territories required: 9
                    // grows required: 24
                    // programmatically change the background of the main activity
                    MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background5);
                    MainActivity.tier = getResources().getString(R.string.tier5);
                } else if (MainActivity.tier.equals(getResources().getString(R.string.tier5))) {
                    // territories required: 9
                    // grows required: 24
                    // programmatically change the background of the main activity
                    MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background6);
                    MainActivity.tier = getResources().getString(R.string.tier6);
                } else {
                    // programmatically change the background of the main activity
                    MainActivity.mainBackground.setBackgroundResource(R.drawable.ant_colony_background7);
                    // append "Glorious" or localized equivalent to front of current tier name
                    MainActivity.tier = getResources().getString(R.string.tier7append) + MainActivity.tier;
                }

                // play for the queen sound effect
                forTheQueenSound.start();

                // change colony strength
                MainActivity.strength += (int) (MainActivity.strength * Math.random());
                textColonyStrength.setText(getResources().getText(R.string.colony_strength) + " " + MainActivity.strength);
                MainActivity.strengthText.setText(getResources().getText(R.string.StrengthText) + " " + MainActivity.strength);

                // update/double glory score
                MainActivity.gloryScore = MainActivity.territoriesRequired * 30 + MainActivity.strength + MainActivity.successfulLift +
                        MainActivity.growPressed * 2 + MainActivity.territoriesRequired * 50;
                textGloryScore.setText(getResources().getText(R.string.colony_glory_score) + " " + MainActivity.gloryScore);

                // REALLY  importanT antTitle = upgradeTierName(antTitle);
                textCurrentTier.setText(getResources().getText(R.string.tier) + " " + MainActivity.tier);
                // augment the requirements for next colony upgrade
                MainActivity.territoriesRequired *= 3;
                MainActivity.growsRequired *= 2;

                textTerritoryRequirement.setText(getResources().getText(R.string.territories_needed) + " " + MainActivity.territoriesRequired);
                textNumberOfGrowsRequirement.setText(getResources().getText(R.string.number_of_grows) + " " + MainActivity.growsRequired);

                editor = prefs.edit();
                editor.putString(CURRENT_TIER_STATE, MainActivity.tier);
                editor.putInt(TOTAL_TERRITORIES_COUNT, MainActivity.territoriesRequired);
                editor.putInt(TOTAL_GROWS_REQUIRED, MainActivity.growsRequired);
                editor.putInt("territoriesRequired", MainActivity.territoriesRequired);
                editor.commit();

                // To show the latest value
                latestTierValue();
                latestTerritoriesRequired();
                latestGrowsRequired();
                latestTerritoryRequired();
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

    public void latestTerritoryRequired() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        MainActivity.territoriesRequired = prefs.getInt("territoriesRequired", 1);
    }

    public void latestGrowsRequired() {
        prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        MainActivity.growsRequired = prefs.getInt(TOTAL_GROWS_REQUIRED, TOTAL_GROWS_DEFAULT);
    }

    public void latestTerritoriesRequired() {
        prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        MainActivity.territoriesRequired = prefs.getInt(TOTAL_TERRITORIES_COUNT, TOTAL_TERRITORIES_DEFAULT);
    }

    public void latestTierValue() {
        prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        MainActivity.tier = prefs.getString(CURRENT_TIER_STATE, DEFAULT_VILLAGE);
    }
}
