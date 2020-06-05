package com.e.antcolony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopGrow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_grow);

        Intent intent = getIntent();
        final int unemployedCount = intent.getIntExtra("unemployed", 0);
        final int costToUpgrade = intent.getIntExtra("costToUpgrade", 0);
        final int victoryCount = intent.getIntExtra("victoryCount", 0);

        TextView textViewIdleAnts = findViewById(R.id.idle_ants);
        textViewIdleAnts.setText("Idle Ants: " + unemployedCount);
        TextView textViewCostToUpgrade = findViewById(R.id.cost_to_upgrade);
        textViewCostToUpgrade.setText("Cost to Upgrade: " + costToUpgrade);
        TextView textViewVictory = findViewById(R.id.biteVictory);
        textViewVictory.setText("Number of Victories: " + victoryCount);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = unemployedCount - costToUpgrade;
                Intent aftermath = new Intent();
                aftermath.putExtra("result", result);
                setResult(RESULT_OK, aftermath);
                finish();
            }
        });
    }
}