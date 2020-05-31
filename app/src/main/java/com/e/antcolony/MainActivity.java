package com.e.antcolony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    ImageButton saveButton;
    Button upgradeButton;
    private int multiplier = 1;
    private int costToUpgrade = 10;
    AdView adView;
    // for switching through android activity cycles
    int antCountSave = 0;
    int unAntCountSave = 0;
    int numberToGrow = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lifecycle","onCreate invoked");


        final MediaPlayer LIFTsound = MediaPlayer.create(this, R.raw.lift);
        queen = (ImageButton) findViewById(R.id.queen);
        queen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                LIFTsound.start();
                Toast.makeText(
                        MainActivity.this,"+" + multiplier, Toast.LENGTH_SHORT
                ).show();
                TextView antCount =(TextView) findViewById(R.id.AntCount);
                TextView unCount =(TextView)findViewById(R.id.UnemployedCount);
                antCount.setText(Integer.toString(Integer.parseInt(antCount.getText().toString()) + multiplier));
                unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) + multiplier));
            }
        });
        final MediaPlayer GROWsound = MediaPlayer.create(this, R.raw.grow);
        upgradeButton = (Button) findViewById(R.id.growButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView unCount =(TextView)findViewById(R.id.UnemployedCount);
                if (costToUpgrade > Integer.parseInt(unCount.getText().toString())) {
                    return;
                }
                GROWsound.start();
                unCount.setText(Integer.toString(Integer.parseInt(unCount.getText().toString()) - costToUpgrade));
                costToUpgrade *= 3.333;
                TextView toGrowCount =(TextView)findViewById(R.id.numberToGrow);
                toGrowCount.setText(Integer.toString(costToUpgrade));
                multiplier *=2.333;
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        // ads
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
        TextView antCount =(TextView) findViewById(R.id.AntCount);
        TextView unCount =(TextView)findViewById(R.id.UnemployedCount);
        antCount.setText(Integer.toString(antCountSave));
        unCount.setText(Integer.toString(unAntCountSave));
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
        TextView antCount =(TextView) findViewById(R.id.AntCount);
        TextView unCount =(TextView)findViewById(R.id.UnemployedCount);
        antCountSave = Integer.parseInt(antCount.getText().toString());
        unAntCountSave = Integer.parseInt(unCount.getText().toString());
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
        TextView antCount =(TextView) findViewById(R.id.AntCount);
        TextView unCount =(TextView)findViewById(R.id.UnemployedCount);
        antCountSave = Integer.parseInt(antCount.getText().toString());
        unAntCountSave = Integer.parseInt(unCount.getText().toString());
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
        TextView antCount =(TextView) findViewById(R.id.AntCount);
        TextView unCount =(TextView)findViewById(R.id.UnemployedCount);
        antCount.setText(Integer.toString(antCountSave));
        unCount.setText(Integer.toString(unAntCountSave));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
    }
}