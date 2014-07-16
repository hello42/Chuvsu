package com.ulop.chuvsu.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.net.Uri;

import com.squareup.picasso.Picasso;
import com.ulop.syncadapter.Feed.FeedContract;


public class SplashScreenActivity extends Activity {

    private static final long SPLASH_DISPLAY_LENGTH = 2000;
    private static final String[] PROJECTION = new String[]{
            FeedContract.Entry.COLUMN_NAME_IMAGE};
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas_screen);



       // Picasso.with(this).
         //       load(R.drawable.chuvsu_logo).
           //     into((ImageView) findViewById(R.id.facultyLogo));


        getActionBar().hide();

        Uri uri = FeedContract.Entry.CONTENT_URI;
        Log.i(TAG, "Will work with " + uri);
        Cursor c = getContentResolver().query(uri, PROJECTION, null, null, null);
        Log.i(TAG, "Found " + c.getCount() + " entries.");

        while (c.moveToNext()){
            String url = c.getString(0);
            Log.i(TAG, "I want to load " + url);
            Picasso.with(this).load(url);
        }
        c.close();

        //NewsNotification.notify(this, "Приложение запущено!", 10);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
