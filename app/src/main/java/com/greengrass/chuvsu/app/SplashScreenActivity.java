package com.greengrass.chuvsu.app;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import com.greengrass.syncadapter.Info.InfoContract;
import com.greengrass.syncadapter.SyncUtils;


public class SplashScreenActivity extends ActionBarActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 2500;
    private static final String[] PROJECTION = new String[]{
            InfoContract.Entry.COLUMN_NAME_IMAGE};
    private static final String TAG =  "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_splas_screen);

	    SyncUtils.CreateSyncAccount(this);

	    SyncUtils.TriggerRefresh();

    /*    Uri uri = InfoContract.Entry.CONTENT_URI;
        Log.i(TAG, "Will work with " + uri);
        Cursor c = getContentResolver().query(uri, PROJECTION, null, null, null);
        Log.i(TAG, "Found " + c.getCount() + " entries.");

        while (c.moveToNext()){
            String url = c.getString(0);
            Log.i(TAG, "I want to load " + url);
            Picasso.with(this).load(url);
        }
        c.close();*/

         new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
