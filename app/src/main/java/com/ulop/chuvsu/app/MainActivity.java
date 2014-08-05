package com.ulop.chuvsu.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ulop.Organiztions.OrganizationFragment;
import com.ulop.abiturients.AbiturientNewsFragment;
import com.ulop.dictionary.Dictionary;
import com.ulop.faculty.FacultyFragment;
import com.ulop.newscardlist.dummy.NewsCardFragment;
import com.ulop.student.PagesFragment;
import com.ulop.student.Student;
import com.ulop.syncadapter.SyncService;
import com.ulop.syncadapter.SyncUtils;
import com.ulop.util.MenuByTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "main";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int currentSelectedItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncUtils.CreateSyncAccount(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentSelectedItem = position;
        String s = new MenuByTime(getApplicationContext()).getItemTitle(position).toLowerCase();
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
                    .replace(R.id.container, Student.newInstance())
                    .commit();

        } else if (s.equals("организации")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, OrganizationFragment.newInstance())
                    .commit();

        } else if (s.equals("библиотека")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PagesFragment.newInstance(3))
                    .commit();

        } else {
        }

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
            getMenuInflater().inflate(R.menu.main, menu);
            if (currentSelectedItem != 3) {
                restoreActionBar();
            }
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
        String currentDBPath = "/data/com.ulop.chuvsu.app/databases/chuvsu.db";
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
