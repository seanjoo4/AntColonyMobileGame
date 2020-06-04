package com.e.antcolony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    ImageButton queen;
    Button settingsButton;
    Button upgradeButton;
    Button liftButton;
    Button biteButton;
    private int multiplier = 1;
    private int costToUpgrade = 10;
    // can you explain to me what these do lol
    private int liftIncreaseFactor = 0;


    private int antIncrease = 0;
    private int biteEffect = 0;
    // private int increase = 20;
    private int biteIncrease = 100;
    private int chance = 50;
    private int damage = 0;
    private int change = 1;
    public static final String EXTRA_TEXT = "com.e.antcolony.EXTRA_TEXT";
    AdView adView;
    // for switching through android activity cycles
    int antCountSave = 0;
    int unAntCountSave = 0;
    int numberToGrow = 10;
    TextView antCount;
    TextView unCount;
    TextView toGrowCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lifecycle", "onCreate invoked");

        // queen ant button - ALL HAIL THE QUEEN NOW BACK TO WORK!!
        /* need to place sound to QUEEN */
        //final MediaPlayer LIFTsound = MediaPlayer.create(this, R.raw.lift);
        queen = (ImageButton) findViewById(R.id.queen);
        queen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //LIFTsound.start();
                antCount = (TextView) findViewById(R.id.AntCount);
                unCount = (TextView) findViewById(R.id.UnemployedCount);
                antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + multiplier));
                unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + multiplier));
            }
        });

        // upgrade button
        final MediaPlayer GROWsound = MediaPlayer.create(this, R.raw.grow);
        upgradeButton = (Button) findViewById(R.id.growButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unCount = (TextView) findViewById(R.id.UnemployedCount);
                if (costToUpgrade > Integer.parseInt(unCount.getText().toString())) {
                    // toast telling user that they do not have enough idle ants
                    Toast.makeText(
                            MainActivity.this, "too few ants", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                GROWsound.start();
                unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) - costToUpgrade));
                costToUpgrade *= 3.333;
                toGrowCount = (TextView) findViewById(R.id.numberToGrow);
                toGrowCount.setText(Integer.toString(costToUpgrade));
                multiplier *= 2.333;
            }
        });
        // lift button
        final MediaPlayer LIFTsound = MediaPlayer.create(this, R.raw.lift);
        liftButton = (Button) findViewById(R.id.liftButton);
        liftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multiplier < 2){
                    Toast.makeText(
                            MainActivity.this, "must grow colony", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                LIFTsound.start();
                // THIS WAS REASON WHY IT WAS STARTING TWICE startActivity(new Intent(MainActivity.this, Pop.class));
                // weaken multiplier due to tired ants unless multiplier == 1 because then int will round to 0
                multiplier *= multiplier > 1 ? .9 : 1;
                /*
                ~50% likelihood of gain or loss
                good outcome is equivalent to  10 to 30 clicks on queen
                bad outcome is equivalent to 0 to 10 clicks on queen
                */
                liftIncreaseFactor = Math.random() <= .5 ? 11+(int)(20*Math.random()) : (int)(Math.random()*10);
                unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + liftIncreaseFactor*multiplier));
                antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + liftIncreaseFactor*multiplier));
                liftMessage();
                /* ORIGINAL
                if (chance < 6) {
                    unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + multiplier));
                    antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + multiplier));
                    //resultCount = (TextView) findViewById(R.id.gainedNum);
                    //resultCount.setText(Integer.toString(multiplier));
                    openLiftMessage();
                } else {
                    antIncrease = (int) (Math.random() * increase);
                    increase *= 1.5;
                    unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + antIncrease));
                    antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + antIncrease));
                    //resultCount = (TextView) findViewById(R.id.gainedNum);
                    //resultCount.setText(Integer.toString(antIncrease));
                    openLiftMessage2();
                }
                */
            }
        });
        // bite button
        // need a mediaplayer for bite
        biteButton = (Button) findViewById(R.id.biteButton);
        biteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // put mediaplayer here
                // no number of ants required, just number of total ants affects likelihood of success
                if (multiplier < 2){
                    Toast.makeText(
                            MainActivity.this, "must grow colony", Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                // ensures that
                // if (1 < (0 <= random num <1) + unemployed(.25) + total(.15)
                biteEffect = (int) (1 < Math.random() + ((unCount.getText().toString().length())*.025) + ((antCount.getText().toString().length())*.015)?
                                        // case victory: gain at most 75% of colony size
                                        Double.parseDouble(antCount.getText().toString()) * 0.75 * Math.random():
                                        // case loss: lose at most 50% of colony size
                                        Double.parseDouble(antCount.getText().toString()) * -0.5 * Math.random());

                unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + biteEffect));
                antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + biteEffect));
                // prevents unemployed ants from becoming negative
                if (Integer.parseInt(unCount.getText().toString()) < 0){
                    // weaken multiplier due to tired ants unless multiplier == 1 because then int will round to 0
                    multiplier *= multiplier > 1 ? .80 : 1;
                    unCount.setText(Integer.toString(0));
                }
                // prevents total ants from becoming negative
                if (Integer.parseInt(antCount.getText().toString()) < 0){
                    // weaken multiplier due to tired ants unless multiplier == 1 because then int will round to 0
                    multiplier *= multiplier > 1 ? .90 : 1;
                    antCount.setText(Integer.toString(0));
                }
                // lower to grow requirement
                costToUpgrade *= .85;
                toGrowCount.setText(Integer.toString((int)(Integer.parseInt(toGrowCount.getText().toString()) * .85)));
                biteMessage();


                // startActivity(new Intent(MainActivity.this, PopBite.class));
                /*
                chance = (int) (Math.random() * 10);
                if (chance < 8) {
                    damage = (int) (2 + (Math.random() * change));
                    change *= 2;
                    if ((Integer.parseInt(unCount.getText().toString()) - damage) < 0 &&
                            (Integer.parseInt(antCount.getText().toString()) - damage) < 0) {
                        unCount.setText(Integer.toString(0));
                        antCount.setText(Integer.toString(0));
                    } else if ((Integer.parseInt(unCount.getText().toString()) - damage) < 0 &&
                            (Integer.parseInt(antCount.getText().toString()) - damage) >= 0) {
                        unCount.setText(Integer.toString(0));
                        antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) - damage));
                    } else if ((Integer.parseInt(unCount.getText().toString()) - damage) >= 0 &&
                            (Integer.parseInt(antCount.getText().toString()) - damage) < 0) {
                        unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) - damage));
                        antCount.setText(Integer.toString(0));
                    } else {
                        unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) - damage));
                        antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) - damage));
                    }
                    openBiteMessage();
                } else {
                    antIncrease = (int) (Math.random() * biteIncrease);
                    biteIncrease *= 1.1;
                    unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + antIncrease));
                    antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + antIncrease));
                    openBiteMessage2();
                }
                */
            }
        });
        // ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void liftMessage() {
        String text = liftIncreaseFactor >= 11 ?
                "GAINED A MIGHTY " + liftIncreaseFactor + " ANTS FROM EXPLOITS OF FORAGING! ALL HAIL THE QUEEN!!!" :
                "GAINED A MEAGER " + liftIncreaseFactor + " ANTS FROM FORAGING! LIFT HARDER!!!" ;
        Intent intent = new Intent(this, Pop.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }

    public void biteMessage() {
        String text = biteEffect > 0 ?
                "VICTORY IS OURS!!! GAINED " + biteEffect + " ANTS!":
                "LOST " + biteEffect + " NOBLE ANTS!  RETREAT!!!";
        Intent intent = new Intent(this, PopBite.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume invoked");
        antCount = (TextView) findViewById(R.id.AntCount);
        unCount = (TextView) findViewById(R.id.UnemployedCount);
        antCount.setText(Integer.toString(antCountSave));
        unCount.setText(Integer.toString(unAntCountSave));

        // trial 2 to attempt to save progress
        /*SharedPreferences sh = getApplicationContext().getSharedPreferences("MyShared", MODE_PRIVATE);
        int total = sh.getInt("totalAnt", 0);
        int untotal = sh.getInt("untotalAnt", 0);
        int growth = sh.getInt("growthMult", 10);
        antCount.setText(String.valueOf(total));
        unCount.setText(String.valueOf(untotal));
        toGrowCount.setText(String.valueOf(growth));*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause invoked");
        antCount = (TextView) findViewById(R.id.AntCount);
        unCount = (TextView) findViewById(R.id.UnemployedCount);
        antCountSave = Integer.parseInt(antCount.getText().toString());
        unAntCountSave = Integer.parseInt(unCount.getText().toString());

        //trial 2 to attempt to save progress
        /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("totalAnt", Integer.parseInt(antCount.getText().toString()));
        myEdit.putInt("untotalAnt", Integer.parseInt(unCount.getText().toString()));
        myEdit.putInt("growthMult", Integer.parseInt(toGrowCount.getText().toString()));
        myEdit.commit();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop invoked");
        antCount = (TextView) findViewById(R.id.AntCount);
        unCount = (TextView) findViewById(R.id.UnemployedCount);
        antCountSave = Integer.parseInt(antCount.getText().toString());
        unAntCountSave = Integer.parseInt(unCount.getText().toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", "onRestart invoked");
        antCount = (TextView) findViewById(R.id.AntCount);
        unCount = (TextView) findViewById(R.id.UnemployedCount);
        antCount.setText(Integer.toString(antCountSave));
        unCount.setText(Integer.toString(unAntCountSave));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");
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