package com.e.antcolony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

// bite popup class

public class PopBite extends Activity {
    Button okayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popbitewindow);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        TextView message = (TextView) findViewById(R.id.biteMessage);
        message.setText(text);

        boolean isVictorious = text.indexOf("-") != -1 ? false: true;
        TextView description = (TextView) findViewById(R.id.biteDescription);
        description.setText(getBattleDescription(isVictorious));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        // biteVictoryMessage.setText();
        // boolean isVictorious = message.getText().toString().indexOf("-");
        // getBattleDescription()
        // button to end pop up
        okayButton = (Button) findViewById(R.id.okayBite);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String getBattleDescription(boolean isVictorious){
        int textChoice = (int)(Math.random()*10);
        if (isVictorious){
            switch(textChoice){
                // PLACEHOLDER TEXT => CHANGE TO A DESCRIPTION OF THE BATTLE
                case 0:
                    return "After a bloody diversionary, bloody constant, heroic and prodigious, dreadful legal, quite nonfunctional, careful mock, bruising political, old, horned, predawn aerial, ruinous fifth, horrible, fearful, important upcoming, silent but strenuous BATTLE, YOUR ANTS TOOK THE ENEMY BASE!";
                case 1:
                    return "INSERT WIN DESCRIPTION";
                case 2:
                    return "INSERT WIN DESCRIPTION";
                case 3:
                    return "INSERT WIN DESCRIPTION";
                case 4:
                    return "INSERT WIN DESCRIPTION";
                case 5:
                    return "INSERT WIN DESCRIPTION";
                case 6:
                    return "INSERT WIN DESCRIPTION";
                case 7:
                    return "INSERT WIN DESCRIPTION";
                case 8:
                    return "INSERT WIN DESCRIPTION";
                default:
                    return "lol damn you actually won wtf";
            }
        }
        else{
            switch(textChoice){
                case 0:
                    return "INSERT LOSE DESCRIPTION";
                case 1:
                    return "INSERT LOSE DESCRIPTION";
                case 2:
                    return "INSERT LOSE DESCRIPTION";
                case 3:
                    return "INSERT LOSE DESCRIPTION";
                case 4:
                    return "INSERT LOSE DESCRIPTION";
                case 5:
                    return "INSERT LOSE DESCRIPTION";
                case 6:
                    return "INSERT LOSE DESCRIPTION";
                case 7:
                    return "INSERT LOSE DESCRIPTION";
                case 8:
                    return "INSERT LOSE DESCRIPTION";
                default:
                    return "LOSE";
            }
        }
    }

}
