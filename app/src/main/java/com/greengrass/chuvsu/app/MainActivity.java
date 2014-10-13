package com.greengrass.chuvsu.app;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.greengrass.Organiztions.OrganizationFragment;
import com.greengrass.abiturients.AbiturientNewsFragment;
import com.greengrass.dictionary.Dictionary;
import com.greengrass.faculty.FacultyFragment;
import com.greengrass.newscardlist.dummy.NewsCardFragment;
import com.greengrass.student.AnnouncementFragment;
import com.greengrass.student.PagesFragment;
import com.greengrass.student.Student;
import com.greengrass.student.StudentFragment;
import com.greengrass.syncadapter.Info.InfoContract;
import com.greengrass.syncadapter.SyncService;
import com.greengrass.syncadapter.SyncUtils;
import com.greengrass.syncadapter.accounts.AuthenticatorService;
import com.greengrass.util.MenuByTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "main";
	private int lastItem = -1;

	/**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("last", lastItem);
	}

	@Override
	public boolean onNavigateUpFromChild(Activity child) {
		//if (lastItem != -1) onNavigationDrawerItemSelected(lastItem);
		return super.onNavigateUpFromChild(child);
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

		//getSupportActionBar().setBackgroundDrawable(R.drawable);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        String s = new MenuByTime(getApplicationContext()).getItemTitle(position).toLowerCase();
	    lastItem = position;
        if (s.equals("новости")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, NewsCardFragment.newInstance("lol", "lol"))
                    .commit();

        } else if (s.equals("первокурсник")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PagesFragment.newInstance(2))
                    .commit();

        } else if (s.equals("университет")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PagesFragment.newInstance(1))
                    .commit();

        } else if (s.equals("факультеты")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FacultyFragment.newInstance())
                    .commit();

        } else if (s.equals("абитуриент")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AbiturientNewsFragment.newInstance())
                    .commit();

        } else if (s.equals("справочник")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Dictionary.newInstance("s", "s"))
                    .commit();

        } else if (s.equals("студент")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, StudentFragment.newInstance(0))
                    .commit();

        } else if (s.equals("организации")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, StudentFragment.newInstance(1))
                    .commit();

        } else if (s.equals("библиотека")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PagesFragment.newInstance(3))
                    .commit();

        } else if (s.equals("анонсы")) {
	        fragmentManager.beginTransaction()
			        .replace(R.id.container, AnnouncementFragment.newInstance())
			        .commit();

        }
        else {
        }

    }

	@Override
	public void onAboutAppSelect() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, AboutAppFragment.newInstance())
				.commit();

	}

	public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
          //  getMenuInflater().inflate(R.menu.main, menu);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.refresh) {
            startService(new Intent(this, SyncService.class));
        }
        if (id == R.id.export) {
            exportDB();
        }
        return super.onOptionsItemSelected(item);
    }



    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/com.greengrass.chuvsu.app/databases/chuvsu.db";
        String backupDBPath = "chuvsu.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
