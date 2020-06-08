package com.e.antcolony;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

/**
 * PopBite: a popup class that displays the result of the bite button. The class will display how many ants were lost/gained,
 * a description of the event, and a dynamic background depending on the output. This class is a child of MainActivity.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 5, 2020
 */
public class PopBite extends Activity {
    Button okayButton;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popbitewindow);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        TextView message = (TextView) findViewById(R.id.biteMessage);
        message.setText(text);

        // if the first character is 'L' from "Lost", then not victorious
        boolean isVictorious = text.charAt(0) != 'L';
        // set description to generated message
        TextView description = (TextView) findViewById(R.id.biteDescription);
        description.setText(getBattleDescription(isVictorious));

        // set background depending on victory status
        RelativeLayout bgElement = (RelativeLayout) findViewById(R.id.container);
        if (isVictorious) {
            // if victorious, then make the background green
            bgElement.setBackgroundColor(getResources().getColor(R.color.biteVictory));
        } else {
            // if loss, then make the background red
            bgElement.setBackgroundColor(getResources().getColor(R.color.biteLoss));
        }

        // make the pop up full screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        // listen for a click on the okay (back) button
        okayButton = (Button) findViewById(R.id.okayBite);
        okayButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (okayBite) is clicked.
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
     * This function gives the app an output on the bite button. This is determined by Math.random().
     * @param isVictorious a boolean that tells the function whether the battle was won or not.
     * @return the description of the battle.
     */
    public static String getBattleDescription(boolean isVictorious) {
        // for each battle outcome, there are 10 possible battle descriptions which are randomly chosen to be displayed
        int textChoice = (int) (Math.random() * 10);
        // the +'s are purely for  ease of reading source code
        if (isVictorious) {
            switch (textChoice) {
                case 0:
                    return "The stealth and guile of our mighty ants sanctioned our raiding group to obnubilate " +
                            "under the cover of tenebrosity and take the enemies by surprise. Such feats of glory shall" +
                            " be told in the annals of ant history!";
                case 1:
                    return "Lady fortuity graced us on this day. Authentically it was an act of providence. On leaf paper, " +
                            "their inundating numbers should have proved insurmountable. But our ants proved to be the " +
                            "insurmountable force! Our Queen shall perpetually be the apex of all life forms.";
                case 2:
                    return "Prior to our ant’s arrival at the scene, a tribe of spiders decimated the enemy, leaving them " +
                            "to be feeble pickings! Let us rejoice in such facile triumphs!";
                case 3:
                    return "Our ants arrived at a near-empty mound. How peculiar but fortuitous!";
                case 4:
                    return "After several sun cycles of battle, both sides incurred grave losses. To verbally express it " +
                            "was a tug of war would be an understatement. It was only in the final push that it became " +
                            "pellucid that our Queen’s colony were the victors of such an engagement. Battles of such " +
                            "heated natures shall bring glory to our queendom!";
                case 5:
                    return "A hungry anteater stumbles upon our habitation! He believes he’s going to encounter a tasty treat, " +
                            "but our soldier ants emerge and BITE!!! The anteater recedes in fear! Well done!";
                case 6:
                    return "An anonymous note tells our ants that the Queen is in grave danger. Our spies search, find the culprit, " +
                            "and take down the evil cockroach in emphatic fashion! Our ants celebrate this great victory!";
                case 7:
                    return "Oh no! One of our own betrays us and starts fighting our troops! But, our ants react expeditiously " +
                            "and take control of the situation! In the Queen’s name, we shall not back down!";
                case 8:
                    return "A praying mantis wants to have a bite off to claim territory. Our best bite ants have the game of " +
                            "their lives and come out victorious! Continue to BITE!!!";
                default:
                    return " Till now it was a myth that ants can defeat a murder hornet. Till today. Our troops battle in dramatic " +
                            "fashion and defeat the hornets even humans feared!";
            }
        } else {
            switch (textChoice) {
                case 0:
                    return "Outnumbered and outmaneuvered, our noble ants have been truncated to meager numbers. " +
                            "Those noble souls may be lost but they are not forgotten. Anon we shall exact our vengeance " +
                            "upon the mound of innoble ants!";
                case 1:
                    return "The fallen ants decorated the grounds of the battlefield. Before the remaining ants lay " +
                            "the remains of their comrades, their friends, their family. Nothing is more devastating " +
                            "than the death of the doted. However, when they summon images of the Queen, pride fills " +
                            "their heart and that glory comes to those who serve the all mighty Queen.";
                case 2:
                    return "Our mighty ants were interrupted from a glorious ant battle upon the arrival of the malicious " +
                            "tribe of feathered birds of prey. Both parties had no cull but to recede in the face of such " +
                            "a Brobdingnagian threat! We mighty ants are ready for the battle of next without such intrusions!";
                case 3:
                    return "Over the course of several sun cycles, all sides incurred losses. No clear victor seemed to " +
                            "emerge—only death. We shall be back with more ants!";
                case 4:
                    return "Our ants scarcely left the Queen’s mound before they were ambushed by the dastardly beetles " +
                            "and caterpillars! Such evil subsists in this world!";
                case 5:
                    return "It’s been a rough battle. We have been tested, but know that we will come back stronger next time! " +
                            "Strengthen our army, and we will go at them again! Hail the Queen!";
                case 6:
                    return "A giant foot appears from the sky! Predicated upon the legends, it seems like it’s a human’s foot. " +
                            "Our ants cannot control this situation, and the human emerges triumphant.";
                case 7:
                    return "Today does not seem like our day! Our ants hurry back to our home, and the ants scatter back. " +
                            "One minor setback for a larger comeback!";
                case 8:
                    return "A dog spots one of our ants and sprints to us! This bite strength is something we have never seen " +
                            "before! Such power! Our ants alarmed, retreats.";
                default:
                    return "This was totally uncalled for. A situation so unique, our ants have been left astonished. " +
                            "Troubled, the only feasible option is to retreat.";
            }
        }
    }

}
