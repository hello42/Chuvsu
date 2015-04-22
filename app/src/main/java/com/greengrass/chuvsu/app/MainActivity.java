package com.greengrass.chuvsu.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.greengrass.abiturients.AbiturientNewsFragment;
import com.greengrass.dictionary.Dictionary;
import com.greengrass.faculty.FacultyFragment;
import com.greengrass.newscardlist.dummy.NewsCardFragment;
import com.greengrass.student.AnnouncementFragment;
import com.greengrass.student.PagesFragment;
import com.greengrass.student.StudentFragment;
import com.greengrass.syncadapter.SyncService;
import com.greengrass.util.MenuByTime;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";
	private int lastItem = -1;
	private Drawer.Result drawer = null;
	private String lastOpened = "";
	static FragmentManager fragmentManager = null;


	/**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment mNavigationDrawerFragment;
	private ArrayList<IDrawerItem> menuItems = null;


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

		Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
		setSupportActionBar(toolbar);

		fragmentManager = getSupportFragmentManager();

		menuItems = MenuByTime.getDrawerItems();

		drawer = new Drawer()
				.withActivity(this)
				.withToolbar(toolbar)
				.withActionBarDrawerToggle(true)
				.withDelayOnDrawerClose(100)
				.withStickyHeader(R.layout.drawer_header)
				.withHeaderDivider(true)
				.withDrawerItems(menuItems)
				//.
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int position, long l, IDrawerItem iDrawerItem) {
						onNavigationDrawerItemSelected(position);
					}
				})
				.build();

				drawer.setSelection(3);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    public void onNavigationDrawerItemSelected(int position) {
        //update the main content by replacing fragments
        String s = new MenuByTime(getApplicationContext()).getItemTitle(position).toLowerCase();
		if (s.equals(lastOpened)) return;
		Log.i(TAG, s);
	    lastItem = position;
	    switch (s) {
		    case "новости":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, NewsCardFragment.newInstance("lol", "lol"))
					    .commit();

			    break;
		    case "первокурсник":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, PagesFragment.newInstance(2))
					    .commit();

			    break;
		    case "университет":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, PagesFragment.newInstance(1))
					    .commit();

			    break;
		    case "факультеты":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, FacultyFragment.newInstance())
					    .commit();

			    break;
		    case "абитуриент":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, AbiturientNewsFragment.newInstance())
					    .commit();

			    break;
		    case "справочник":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, Dictionary.newInstance("s", "s"))
					    .commit();

			    break;
		    case "студент":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, StudentFragment.newInstance(0))
					    .commit();

			    break;
		    case "организации":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, StudentFragment.newInstance(1))
					    .commit();

			    break;
		    case "библиотека":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, PagesFragment.newInstance(3))
					    .commit();

			    break;
		    case "анонсы":
			    fragmentManager.beginTransaction()
					    .replace(R.id.container, AnnouncementFragment.newInstance())
					    .commit();

			    break;
		    default:
			    break;
	    }
		lastOpened = s;

    }


	public void onAboutAppSelect() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, AboutAppFragment.newInstance())
				.commit();

	}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	   // return !mNavigationDrawerFragment.isDrawerOpen() || super.onCreateOptionsMenu(menu);
		return false;
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
        FileChannel source;
        FileChannel destination;
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

	@Override
	public void onBackPressed() {
		if (drawer != null) {
			if (drawer.isDrawerOpen()) super.onBackPressed();
			else drawer.openDrawer();

		} else {
			super.onBackPressed();
		}
	}
}
