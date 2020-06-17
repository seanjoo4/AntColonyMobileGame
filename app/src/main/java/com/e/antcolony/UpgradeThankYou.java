package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpgradeThankYou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_thank_you);

        TextView nowTier = findViewById(R.id.nowTierText);
        TextView nowStrength = findViewById(R.id.nowStrengthText);

        nowTier.setText("New Tier: " + MainActivity.tier);
        nowStrength.setText("New Strength: " + MainActivity.strength);


        // popup layout
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        // Back Button
        Button okay = findViewById(R.id.popupOkayButton);
        okay.setOnClickListener(new View.OnClickListener() {

            /**
             * It is a callback for when the button (creditsButton) is clicked.
             *
             * @param v used when a view is clicked.
             */
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}