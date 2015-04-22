package com.greengrass.faculty;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.Faculty;
import com.squareup.picasso.Picasso;

public class FacultyInfoActivity extends ActionBarActivity {

	String faculty_id;
	Faculty fct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_info);

	    Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
	    setSupportActionBar(toolbar);

	    Intent intent = getIntent();
	    faculty_id = intent.getStringExtra("faculty_id");
	    fct = new Select()
			    .from(Faculty.class)
			    .where("faculty_id = ?", faculty_id)
			    .executeSingle();

	    setTitle(fct.faculty_name);

	    ImageView facultyLogo = (ImageView) findViewById(R.id.facultyLogo);
	    Picasso.with(this)
			    .load(fct.logo)
			    .into(facultyLogo);

	    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

	    TextView facultyName = (TextView) findViewById(R.id.facultyName);
	    facultyName.setTypeface(typeface);
	    facultyName.setText(fct.faculty_name);

	    TextView facultyInfo = (TextView) findViewById(R.id.facultyInfo);
	    facultyInfo.setTypeface(typeface);
	    facultyInfo.setText(fct.info);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faculty_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.faculty_url) {
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setData(Uri.parse(fct.url));
	        startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
