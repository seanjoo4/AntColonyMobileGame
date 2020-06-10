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
import android.widget.RelativeLayout;
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
 * @version June 9, 2020
 */
public class MainActivity extends AppCompatActivity {
    // MAIN ACTIVITY XML ELEMENTS
    TextView strengthText;
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
    private int idleAntNumber = 0;
    private int antNumber = 0;
    // number of ants gained per click on queen
    private int strength = 1;
    // GROW BUTTON
    // cost to grow in terms of idle ants
    private int costToGrow = 10;
    private int growPressed = 0;
    // BITE BUTTON
    private int biteEffect = 0;
    // start with one territory claimed to include home colony (cannot go below 1)
    // for statistics purposes
    private int territoriesClaimed = 1;
    private int territoriesLost = 0;
    // LIFT BUTTON
    // will attempt to ensure roughly 50% success rate of lifting
    private double liftInertia = 0;
    private int successfulLift = 0;
    private int unsuccessfulLift = 0;
    private int liftIncreaseFactor = 0;
    // VARIABLES AFFECTED BY PopUpgrade.java
    public static ConstraintLayout mainBackground;
    public static String tier = "Tribal Village";
    public static int gloryScore = 0;

    public static int territoriesRequired = 1;
    public static int growsRequired = 2;
    // string constant for intent functions: package_name.OUR_TEXT
    public static final String EXTRA_TEXT = "com.e.antcolony.EXTRA_TEXT";
    // ADS
    AdView adView;
    // SAVE STATE
    // for switching through android activity cycles (probably should delete)
    int antCountSave = 0;
    int unAntCountSave = 0;
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
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorOffBlack));
            // change status bar color
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        // create strengthText
        strengthText = findViewById(R.id.strengthNumber);
        antCount = findViewById(R.id.AntCount);
        unCount = findViewById(R.id.UnemployedCount);
        toGrowCount = findViewById(R.id.numberToGrow);

        // establish main background
        mainBackground = (ConstraintLayout) findViewById(R.id.mainBackground);

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

                // decrease idle ants
                idleAntNumber -= costToGrow;
                unCount.setText(idleAntNumber + "");

                // increase colony strength
                strength *= 2;
                strengthText.setText(strength + "");

                // increase the cost of idle ants for next grow
                costToGrow *= 3;
                toGrowCount.setText(costToGrow + "");
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
                    Toast.makeText(
                            MainActivity.this, "colony is too weak", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                // play lift sound effect
                liftSound.start();
                // weaken strength due to tired ants unless strength <= 1 because we want strength to always be greater than 0
                strength += strength > 1 ? (int) (-.1 * strength) : 0;

                /*
                ~50% likelihood of gain or loss
                good outcome is equivalent to  10 to 50 clicks on queen
                bad outcome is equivalent to 0 to 4 clicks on queen
                */
                // intertia designed to ensure ~50% lift rate success
                liftIncreaseFactor = Math.random() + liftInertia <= .5 ? 11 + (int) (40 * Math.random()) : (int) (Math.random() * 5);
                idleAntNumber += liftIncreaseFactor * strength;
                unCount.setText(idleAntNumber + "");
                antNumber += liftIncreaseFactor * strength;
                antCount.setText(antNumber + "");
                strengthText.setText(strength + "");
                liftMessage();
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

                // if (1 < (0 <= random num <1) + unemployed(.25) + total(.1)
                biteEffect = (int) (1 < Math.random() + ((unCount.getText().toString().length()) * .025) + ((antCount.getText().toString().length()) * .01) ?
                        // case victory: gain at most 75% of colony size
                        antNumber * 0.75 * Math.random() :
                        // case loss: lose at most 50% of colony size
                        antNumber * -0.5 * Math.random());

                // change idle and total number ants by bite effect
                idleAntNumber += biteEffect;
                antNumber += biteEffect;

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
                strengthText.setText(strength + "");
                toGrowCount.setText(costToGrow + "");

                // display bitMessage
                biteMessage();
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

    // music services
    private ServiceConnection Scon = new ServiceConnection() {

        /**
         * This function is called upon the creation of the app to connect the music service.
         * @param name represents the file component
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

        // trial 2 to attempt to save progress
        /*SharedPreferences sh = getApplicationContext().getSharedPreferences("MyShared", MODE_PRIVATE);
        int total = sh.getInt("totalAnt", 0);
        int untotal = sh.getInt("untotalAnt", 0);
        int growth = sh.getInt("growthMult", 10);
        antCount.setText(String.valueOf(total));
        unCount.setText(String.valueOf(untotal));
        toGrowCount.setText(String.valueOf(growth));*/

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

        //trial 2 to attempt to save progress
        /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("totalAnt", Integer.parseInt(antCount.getText().toString()));
        myEdit.putInt("untotalAnt", Integer.parseInt(unCount.getText().toString()));
        myEdit.putInt("growthMult", Integer.parseInt(toGrowCount.getText().toString()));
        myEdit.commit();*/
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

    // trial 1 to save attempt
   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        Log.d("save Instance State", "This is saving the instant state");
        savedInstanceState.putInt("employedAnt", antCountSave);
        savedInstanceState.putInt("unemployedAnt", unAntCountSave);
        savedInstanceState.putInt("growth", numberToGrow);
    }*/
    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        Log.d("Restore Instance State", "This is restoring the instant state");
        antCountSave = savedInstanceState.getInt("employedAnt");
        unAntCountSave = savedInstanceState.getInt("unemployedAnt");
        numberToGrow = savedInstanceState.getInt("growth");
        /*antCount.setText(antCountSave);
        unCount.setText(unAntCountSave);
        toGrowCount.setText(numberToGrow);

        antCount.setText(savedInstanceState.getInt((antCountSave) + ""));
        unCount.setText(savedInstanceState.getInt((unAntCountSave) + ""));
        toGrowCount.setText(savedInstanceState.getInt((numberToGrow) + ""));
    }*/
}