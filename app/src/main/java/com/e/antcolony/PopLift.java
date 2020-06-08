package com.e.antcolony;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        setContentView(R.layout.popliftwindow);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        TextView message = (TextView) findViewById(R.id.liftMessage);
        message.setText(text);

        // if the first character is 'O' from "Only gained", then not successful
        boolean isSuccessful = text.charAt(0) != 'O';
        // set description to generated message
        TextView liftDescription = findViewById(R.id.liftDescription);
        liftDescription.setText(getHarvestDescription(isSuccessful));

        // set background depending on victory status
        RelativeLayout bgElement = (RelativeLayout) findViewById(R.id.container);
        if (isSuccessful) {
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
     * This function gives the app an output on the lift button. This is determined by Math.random().
     * @param isSuccessful a boolean that tells the function whether the lift was successful or not.
     * @return the description of the adventure.
     */
    public static String getHarvestDescription(boolean isSuccessful){
        // for each battle outcome, there are 10 possible lift descriptions which are randomly chosen to be displayed
        int textChoice = (int)(Math.random()*10);
        // the +'s are purely for  ease of reading source code
        if (isSuccessful){
            switch(textChoice){
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
                    return "Thicc leaves!";
                case 7:
                    return "One of our brothers found a coconut! He cannot lift on his own. We must help! LIFT!";
                case 8:
                    return "A kind human drops a clump of delicious orange peels!";
                default:
                    return "A human leaves their sausage left unattended! Lets lift!";
            }
        }
        else{
            switch(textChoice){
                case 0:
                    return "No honeydew to be found…";
                case 1:
                    return "Scarce pickings of thicc leaves.";
                case 2:
                    return "Seems we’ve been lifting too much recently.";
                case 3:
                    return "A terrible harvesting season results in little food available.";
                case 4:
                    return "Our ants cannot lift the loot that is given! Come back when our army is bigger!";
                case 5:
                    return "So heavy … so tired ...";
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
