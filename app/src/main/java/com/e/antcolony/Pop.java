package com.e.antcolony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// lift popup

public class Pop extends Activity {
    Button okayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        TextView message = (TextView) findViewById(R.id.liftMessage);

        message.setText(text);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        okayButton = (Button) findViewById(R.id.okayLift);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            super.onBackPressed();
            super.onBackPressed();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        // super.onBackPressed();
    }


}
