package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;

import android.util.Log;

/**
 * MainActivity: the main class that runs the main interface of the app.
 *
 * @author Aidan Andrucyk and Sean Joo
 * @version June 10, 2020
 */
public class MainActivity extends AppCompatActivity {
    // MAIN ACTIVITY XML ELEMENTS
    public static TextView strengthText;
    TextView antCount;
    TextView unCount;
    TextView toGrowCount;
    ImageButton queen;
    Button settingsButton;
    Button growButton;
    Button liftButton;
    Button biteButton;
    Button upgradeButton;
    // OVERARCHING STATS
    public static int idleAntNumber = 0;
    public static int antNumber = 0;
    // GROW BUTTON
    // cost to grow in terms of idle ants
    private int costToGrow = 10;
    public static int growPressed = 0;
    // BITE BUTTON
    private int biteEffect = 0;
    // start with one territory claimed to include home colony (cannot go below 1)
    // for statistics purposes
    public static int territoriesClaimed = 1;
    public static int territoriesLost = 0;
    private int biteInertia = 0;
    // LIFT BUTTON
    // will attempt to ensure roughly 50% success rate of lifting
    private double liftInertia = 0;
    public static int successfulLift = 0;
    public static int unsuccessfulLift = 0;
    private int liftIncreaseFactor = 0;
    // VARIABLES AFFECTED BY PopUpgrade.java
    public static ConstraintLayout mainBackground;
    // number of ants gained per click on queen
    public static int strength = 1;
    public static String tier = "";
    public static int gloryScore = 0;
    public static int territoriesRequired = 1;
    public static int growsRequired = 6;
    public static boolean musicState = true;
    // string constant for intent functions: package_name.OUR_TEXT
    public static final String EXTRA_TEXT = "com.e.antcolony.EXTRA_TEXT";
    // ADS
    AdView adView;
    // AUDIO
    HomeWatcher mHomeWatcher;
    private boolean mIsBound = false;
    // background audio
    private MusicService mServ;
    public static MediaPlayer workSound;
    public static MediaPlayer growSound;
    public static MediaPlayer liftSound;
    public static MediaPlayer biteSound;
    public static MediaPlayer nouSound;
    // variables for SavedPreferences functionality
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState used when activity needs to be created/recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lifecycle", "onCreate invoked");

        // change window colors
        // first checks if correct version
        if (android.os.Build.VERSION.SDK_INT >= 21 && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // change system nav bar color
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorPrimary));
            // change status bar color
            window.setStatusBarColor(this.getResources().getColor(R.color.colorDark));
        }

        // create strengthText
        unCount = findViewById(R.id.UnemployedCount);
        antCount = findViewById(R.id.AntCount);
        toGrowCount = findViewById(R.id.toGrow);
        strengthText = findViewById(R.id.currentStrength);

        // SharedPreferences default values
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        costToGrow = prefs.getInt("growthCost", 10);
        strength = prefs.getInt("strengthCount", 1);
        idleAntNumber = prefs.getInt("idleAntCounts", 0);
        antNumber = prefs.getInt("totalAntCounts", 0);
        territoriesClaimed = prefs.getInt("territoryWonCount", 1);
        territoriesLost = prefs.getInt("territoryLostCount", 0);
        successfulLift = prefs.getInt("successfulLiftCount", 0);
        unsuccessfulLift = prefs.getInt("unsuccessfulLiftCount", 0);
        growPressed = prefs.getInt("growPressedCount", 0);
        tier = prefs.getString(PopUpgrade.CURRENT_TIER_STATE, "");

        // set text to defaults
        toGrowCount.setText(getResources().getText(R.string.ToGROW) + " " + costToGrow);
        strengthText.setText(getResources().getText(R.string.StrengthText) + " " + strength);
        unCount.setText(idleAntNumber + "");
        antCount.setText(antNumber + "");

        // establish main background
        mainBackground = (ConstraintLayout) findViewById(R.id.mainBackground);

        // set tier to localized name name
        setMainBackground();

        // no sounds
        nouSound = MediaPlayer.create(this, R.raw.nou);

        // deselect colony name after finished editing
        EditText colonyName = (EditText) findViewById(R.id.ColonyName);
        colonyName.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            /**
             *
             * @param v
             * @param actionId
             * @param event
             * @return
             */
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.e("TAG", "Done pressed");
                }
                return false;
            }
        });

        // work sound to be played upon clicking the queen button
        workSound = MediaPlayer.create(this, R.raw.work);
        // original audio was a little too quiet during recording
        workSound.setVolume(1f, 1f);
        // queen button
        queen = (ImageButton) findViewById(R.id.queen);
        queen.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (settingsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            public void onClick(View v) {
                workSound.start();
                // add ant count by the modifier
                idleAntNumber += strength;
                unCount.setText(idleAntNumber + "");
                antNumber += strength;
                antCount.setText(antNumber + "");

                // Save the incremented value
                editor = prefs.edit();
                editor.putInt("idleAntCounts", idleAntNumber);
                editor.putInt("totalAntCounts", antNumber);
                editor.commit();

                // To show the latest number
                latestIdleCount();
                latestTotalCount();
            }
        });

        growSound = MediaPlayer.create(this, R.raw.grow);
        // grow button
        growButton = (Button) findViewById(R.id.growButton);
        growButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (settingsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                unCount = (TextView) findViewById(R.id.UnemployedCount);
                if (costToGrow > idleAntNumber) {
                    // play "can't do" sound
                    nouSound.start();
                    // toast telling user that they do not have enough idle ants
                    Toast.makeText(
                            MainActivity.this, "too few ants", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                // play grow sound effect
                growSound.start();
                // increment the number of times grow pressed
                growPressed++;

                // Load the latest int value in onclick
                costToGrow = prefs.getInt("growthCost", 10);

                // decrease idle ants
                idleAntNumber -= costToGrow;
                unCount.setText(idleAntNumber + "");

                // increase colony strength 1,2,4,7,11,17,26
                strength += (int) (.5 * strength + 1);
                strengthText.setText(getResources().getText(R.string.StrengthText) + " " + strength);

                // increase the cost of idle ants for next grow 10 20 40 80 160 320
                costToGrow *= 2;
                toGrowCount.setText(getResources().getText(R.string.ToGROW) + " " + costToGrow);

                // Save the incremented value
                editor = prefs.edit();
                editor.putInt("growthCost", costToGrow);
                editor.putInt("strengthCount", strength);
                editor.putInt("idleAntCounts", idleAntNumber);
                editor.putInt("growPressedCount", growPressed);
                editor.commit();

                // To show the latest number
                latestGrowth();
                latestStrength();
                latestIdleCount();
                latestGrowPressed();
            }
        });

        // lift button
        liftSound = MediaPlayer.create(this, R.raw.lift);
        liftButton = (Button) findViewById(R.id.liftButton);
        liftButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (settingsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                if (strength < 2) {

                    // play "can't do" sound
                    nouSound.start();

                    // alert user that colony is too weak
                    Toast.makeText(
                            MainActivity.this, "colony is too weak", Toast.LENGTH_SHORT
                    ).show();

                    // prevent pop up
                    return;
                }

                // play lift sound effect
                liftSound.start();

                // weaken strength due to tired ants unless strength <= 1 because we want strength to always be greater than 0
                strength += strength > 1 ? (int) (-.1 * strength) : 0;

                // ~50% likelihood of gain or loss
                // intertia designed to ensure ~50% lift rate success
                liftIncreaseFactor = (int) (Math.random() + liftInertia <= .5 ?
                        // good outcome is equivalent to  10 to 50 clicks on queen
                        11 + (40 * Math.random()) :
                        // bad outcome is equivalent to 0 to 4 clicks on queen
                        (Math.random() * 5));

                // change number of ants by liftIncreaseFactor times strength
                idleAntNumber += liftIncreaseFactor * strength;
                antNumber += liftIncreaseFactor * strength;

                // set texts
                unCount.setText(idleAntNumber + "");
                antCount.setText(antNumber + "");
                strengthText.setText(getResources().getText(R.string.StrengthText) + " " + strength);
                liftMessage();

                // Save the incremented value
                editor = prefs.edit();
                editor.putInt("strengthCount", strength);
                editor.putInt("idleAntCounts", idleAntNumber);
                editor.putInt("totalAntCounts", antNumber);
                editor.putInt("successfulLiftCount", successfulLift);
                editor.putInt("unsuccessfulLiftCount", unsuccessfulLift);
                editor.commit();

                // To show the latest number
                latestStrength();
                latestIdleCount();
                latestTotalCount();
                latestLiftSuccess();
                latestLiftUnsuccessful();
            }
        });

        // bite button
        biteSound = MediaPlayer.create(this, R.raw.bite);
        biteButton = (Button) findViewById(R.id.biteButton);
        biteButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (settingsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // no number of ants required, just number of total ants affects likelihood of success
                if (strength < 2) {
                    // play "can't do" sound
                    nouSound.start();
                    Toast.makeText(
                            MainActivity.this, "colony is too weak", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                // play bite sound effect
                biteSound.start();

                // if (1 < (0 <= random num <1) + inertia(.1)  + unemployed(.25) + total(.1)
                biteEffect = (int) (1 < Math.random() + (biteInertia * .1) + ((unCount.getText().toString().length()) * .025) + ((antCount.getText().toString().length()) * .01) ?
                        // case victory: gain at most 75% of colony size
                        antNumber * 0.75 * Math.random() :
                        // case loss: lose at most 50% of colony size
                        antNumber * -0.5 * Math.random());

                // change idle and total number ants by bite effect
                idleAntNumber += biteEffect;
                antNumber += biteEffect;

                // if won, make it harder to win, else make it easier for next bite
                biteInertia += biteEffect > 0 ? -1 : 1;

                // lower to grow requirement after battle
                costToGrow *= .85;

                // only lowers strength when unemployed ants < 0
                // prevents unemployed ants from becoming negative
                if (idleAntNumber < 0) {
                    // weaken strength due to tired ants unless strength <= 1 because we want strength to always be greater than 0
                    strength += strength > 2 ? (int) (-.20 * strength) : 0;
                    idleAntNumber = 0;
                }
                // prevents total ants from becoming negative
                if (antNumber < 0) {
                    // weaken strength due to tired ants unless strength == 1 because then int will round to 0
                    strength += strength > 2 ? (int) (-.10 * strength) : 0;
                    antNumber = 0;
                }

                // update texts
                unCount.setText(idleAntNumber + "");
                antCount.setText(antNumber + "");
                strengthText.setText(getResources().getText(R.string.StrengthText) + " " + strength);
                toGrowCount.setText(getResources().getText(R.string.ToGROW) + " " + costToGrow);

                // display bitMessage
                biteMessage();

                editor = prefs.edit();
                editor.putInt("growthCost", costToGrow);
                editor.putInt("strengthCount", strength);
                editor.putInt("idleAntCounts", idleAntNumber);
                editor.putInt("totalAntCounts", antNumber);
                editor.putInt("territoryWonCount", territoriesClaimed);
                editor.putInt("territoryLostCount", territoriesLost);
                editor.commit();

                // To show the latest number
                latestGrowth();
                latestStrength();
                latestIdleCount();
                latestTotalCount();
                latestTerritoryWon();
                latestTerritoryLost();
            }
        });

        // Upgrade Button
        upgradeButton = (Button) findViewById(R.id.upgradeButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (settingsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                // opening the pop_grow to transfer data back and forth
                Intent intent = new Intent(MainActivity.this, PopUpgrade.class);
                //intent.putExtra() values we are going to pass back and forth
                intent.putExtra("totalAnts", antNumber);
                intent.putExtra("unemployed", idleAntNumber);
                intent.putExtra("strengthCount", strength);
                intent.putExtra("costToGrow", costToGrow);
                intent.putExtra("victoryCount", territoriesClaimed);
                intent.putExtra("lossCount", territoriesLost);
                intent.putExtra("successfulLift", successfulLift);
                intent.putExtra("unsuccessfulLift", unsuccessfulLift);
                intent.putExtra("growPressed", growPressed);
                intent.putExtra("antTitle", tier);
                intent.putExtra("gloryScore", gloryScore);
                intent.putExtra("totalTerritories", territoriesRequired);
                intent.putExtra("totalGrows", growsRequired);

                // for variables, we should create constants to avoid confusion (ex: unemployed & 1)
                startActivityForResult(intent, 1);
            }
        });

        // Settings Popup
        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (settingsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(intent, 1);
            }
        });

        // music services
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

        // checks if user presses the home button => pauses music
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {

            /**
             * This function will pause the music when the system nav bar home button is pressed
             */
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            /**
             * This function will pause the music when the system nav bar home button is "long" pressed
             */
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        // Google Firebase ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {

            /**
             * @param initializationStatus represents
             */
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
    // OUTSIDE OF OnCreate!

    /*public void latestTerritoryRequired() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        territoriesRequired = prefs.getInt("territoriesRequired", 1);
    }*/

    public void latestGrowPressed() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        growPressed = prefs.getInt("growPressedCount", 0);
    }

    public void latestLiftUnsuccessful() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        unsuccessfulLift = prefs.getInt("unsuccessfulLiftCount", 0);
    }

    public void latestLiftSuccess() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        successfulLift = prefs.getInt("successfulLiftCount", 0);
    }

    public void latestTerritoryLost() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        territoriesLost = prefs.getInt("territoryLostCount", 0);
    }

    public void latestTerritoryWon() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        territoriesClaimed = prefs.getInt("territoryWonCount", 1);
    }

    public void latestTotalCount() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        antNumber = prefs.getInt("totalAntCounts", 0);
        antCount.setText(antNumber + "");
    }

    public void latestIdleCount() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        idleAntNumber = prefs.getInt("idleAntCounts", 0);
        unCount.setText(idleAntNumber + "");
    }

    public void latestStrength() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        strength = prefs.getInt("strengthCount", 1);
        strengthText.setText(getResources().getText(R.string.StrengthText) + " " + strength);

    }

    // SavedPreference for Growth
    public void latestGrowth() {
        prefs = getSharedPreferences("sharedPref", MODE_PRIVATE);
        costToGrow = prefs.getInt("growthCost", 10);
        toGrowCount.setText(getResources().getText(R.string.ToGROW) + " " + costToGrow);
    }

    /**
     * This function displays the lift message depending on output.
     */
    public void liftMessage() {
        String text = liftIncreaseFactor >= 11 ?
                "GAINED A MIGHTY " + liftIncreaseFactor * strength + " ANTS! \n ALL HAIL THE QUEEN!!!" :
                "ONLY GAINED A MEAGER " + liftIncreaseFactor * strength + " ANTS! \n LIFT HARDER!!!";
        // if victory, increment by lift inertia by 1 else decrement by 1
        liftInertia += text.charAt(0) == 'G' ? .1 : -.1;
        if (text.charAt(0) == 'G') {
            successfulLift++;
        } else {
            unsuccessfulLift++;
        }
        // get pop up
        Intent intent = new Intent(this, PopLift.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }

    /**
     * This function displays the bite message depending on output.
     */
    public void biteMessage() {
        String text = biteEffect > 0 ?
                "VICTORY IS OURS!!! GAINED " + biteEffect + " ANTS!" :
                "LOST " + Math.abs(biteEffect) + " NOBLE ANTS! RETREAT!!!";
        // if victorious, then increase territories claimed
        if (text.charAt(0) == 'V') {
            territoriesClaimed++;
        } else {
            territoriesLost++;
            // prevents territories claimed from becoming 0 or negative
            if (territoriesClaimed <= 0) {
                // reset to 1
                territoriesClaimed = 1;
                // huge blow to strength
                strength *= .75;
            }
        }
        Intent intent = new Intent(this, PopBite.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }


    /**
     * This function changes the current tier and dynamically changes the background of the main activity.
     */
    public void setMainBackground() {
        // ordered from easiest to hardest to reduce the number of comparisons
        if (tier.equals(getResources().getString(R.string.tier1)) || tier.equals("")) {
            return;
        } else if (tier.equals(getResources().getString(R.string.tier2))) {
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background2);
        } else if (tier.equals(getResources().getString(R.string.tier3))) {
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background3);
        } else if (tier.equals(getResources().getString(R.string.tier4))) {
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background4);
        } else if (tier.equals(getResources().getString(R.string.tier5))) {
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background5);
        } else if (tier.equals(getResources().getString(R.string.tier6))) {
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background6);
        } else {
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background7);
        }
    }


    /*
    // changes the tier name by comparing the current name with the next names in the progression
    public void setTier(int territoriesRequired) {
        // ordered from easiest to hardest to reduce the number of comparisons
        if (territoriesRequired == 1) {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier1);
        } else if (territoriesRequired == 3) {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier2);
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background2);
        } else if (territoriesRequired == 9) {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier3);
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background3);
        } else if (territoriesRequired == 18) {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier4);
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background4);
        } else if (territoriesRequired == 54) {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier5);
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background5);
        } else if (territoriesRequired == 162) {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier6);
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background6);
        } else {
            // set the tier to localized string
            tier = getResources().getString(R.string.tier7append) + tier;
            // programmatically change the background of the main activity
            mainBackground.setBackgroundResource(R.drawable.ant_colony_background7);
            // append "Glorious" or localized equivalent to front of current tier name
        }
    }
     */


    // music services
    private ServiceConnection Scon = new ServiceConnection() {

        /**
         * This function is called upon the creation of the app to connect the music service.
         *
         * @param name   represents the file component
         * @param binder tells the app what it is responsible for incrementing and maintaining
         */
        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        /**
         * This function disconnects music service.
         * @param name
         */
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    /**
     * This function attempts to establish a connection with the music service without supporting component replacement by other
     * applications.
     */
    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /**
     * This function releases information regarding the music service's state.
     */
    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    /**
     * This function is called when the activity is visible to the user
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "onStart invoked");
    }

    /**
     * This function is called when the activity is interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume invoked");

        // music
        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    /**
     * This function is called when the app still can be visible to the user, but is about to experience
     * stoppage or destruction.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause invoked");

        // stop music
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

    /**
     * This function is called when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop invoked");

    }

    /**
     * This function is called when the activity is stopped before it starting again.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", "onRestart invoked");

    }

    /**
     * This function is called during the final stage when the activity is going to be destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");
        // music
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);
    }
}