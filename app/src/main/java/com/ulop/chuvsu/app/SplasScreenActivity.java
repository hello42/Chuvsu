package com.ulop.chuvsu.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplasScreenActivity extends Activity {

    private static final long SPLASH_DISPLAY_LENGHT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas_screen);
        getActionBar().hide();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplasScreenActivity.this, MainActivity.class);
                SplasScreenActivity.this.startActivity(mainIntent);
                SplasScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

}
