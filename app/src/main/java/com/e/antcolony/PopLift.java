package com.e.antcolony;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * PopLift: this class shows the result of the lift button. It will display how many ants were gained and a description
 * of the event. This class is a child of MainActivity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 5, 2020
 */
public class PopLift extends Activity {

    Button okayButton;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get xml layout to display
        setContentView(R.layout.activity_lift);

        TextView message = (TextView) findViewById(R.id.liftMessage);
        message.setText(getLiftReport());

        // set description to generated message
        TextView liftDescription = findViewById(R.id.liftDescription);
        liftDescription.setText(getHarvestDescription());

        // set background depending on victory status
        LinearLayout bgElement = (LinearLayout) findViewById(R.id.container);
        if (MainActivity.isSuccessful) {
            // if victorious, then make the background green
            bgElement.setBackgroundColor(getResources().getColor(R.color.liftVictory));
        } else {
            // if loss, then make the background red
            bgElement.setBackgroundColor(getResources().getColor(R.color.liftLoss));
        }

        // make the pop up full screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        // listen for a click on the okay (back) button
        okayButton = (Button) findViewById(R.id.okayLift);
        okayButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayLift) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * This function returns a report for the respective win/loss bite state.
     *
     * @return the report of how many ants were loss/gained.
     */
    public String getLiftReport() {

        // if not in english, get localized report
        if (!getResources().getString(R.string.lang).equals("en")) {
            // number of ants gained/loss + respective language translation for the description
            return MainActivity.liftIncreaseFactor * MainActivity.strength + getResources().getString(R.string.lift_report);
        }

        // if in english
        return MainActivity.isSuccessful ?
                // MainActivity.liftIncreaseFactor * MainActivity.strength represents the number of ants gained/lost
                "GAINED A MIGHTY " + MainActivity.liftIncreaseFactor * MainActivity.strength + " ANTS! \n ALL HAIL THE QUEEN!!!" :
                "ONLY GAINED A MEAGER " + MainActivity.liftIncreaseFactor * MainActivity.strength + " ANTS! \n LIFT HARDER!!!";
    }


    /**
     * This function randomly returns a description for the respective win/loss lift state.
     *
     * @return the description of the adventure.
     */
    public String getHarvestDescription() {

        // gives a different description if not in english
        if (!getResources().getString(R.string.lang).equals("en")) {
            if (MainActivity.isSuccessful) {
                return getResources().getString(R.string.lift_description_good);
            }
            // else if loss
            return getResources().getString(R.string.lift_description_bad);
        }


        // for each battle outcome, there are 10 possible lift descriptions which are randomly chosen to be displayed
        int textChoice = (int) (Math.random() * 10);
        // the +'s are purely for  ease of reading source code
        if (MainActivity.isSuccessful) {
            switch (textChoice) {
                case 0:
                    return "The Queen’s ants found a package of severely genetically modified strawberries!";
                case 1:
                    return "Honeydew has been found nearby! Bless the aphids!";
                case 2:
                    return "An assortment of vegetables were found nearby!";
                case 3:
                    return "The Queen’s ants discovered a package of human pies! Let us feast!";
                case 4:
                    return "The aphids have graced us with sweet honeydew!";
                case 5:
                    return "White powder?! SUGAR!!!";
                case 6:
                    return "Powell made printers go BRRR! Human minions make green stonks and spend it lavishly on food they can't finish!";
                case 7:
                    return "One of our brothers found a coconut! He cannot lift on his own. We must help! LIFT!";
                case 8:
                    return "A kind human drops a clump of delicious orange peels!";
                default:
                    return "A human leaves their sausage left unattended! Lets lift!";
            }
        } else {
            switch (textChoice) {
                case 0:
                    return "No honeydew to be found…";
                case 1:
                    return "Scarce pickings of thicc leaves.";
                case 2:
                    return "Seems we’ve been lifting too much recently.";
                case 3:
                    return "A terrible harvesting season results in little food available.";
                case 4:
                    return "A rival ant colony already plundered the bountiful human baby cabinet!";
                case 5:
                    return "So heavy... so tired ...";
                case 6:
                    return "We have been searching for hours, but cannot find anything.";
                case 7:
                    return "The ants harvesting got greedy and ate our harvest. BAD ANTS!";
                case 8:
                    return "No humans are throwing away scraps!";
                default:
                    return "Our ants are struggling. We need to ANTi-depressANTS";
            }
        }
    }

}
