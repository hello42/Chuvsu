package com.greengrass.student;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.InfoForStudent;

/**
 * Created by ulop on 03.09.14.
 */
public class StudentInfoFragment extends Fragment {

	public static StudentInfoFragment newInstance(String body) {
		StudentInfoFragment fragment = new StudentInfoFragment();

		Bundle args = new Bundle();
		args.putString("INFO", body);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.text_view_layout, container, false);
		String body = getArguments().getString("INFO");
		/*Log.i("TEST", newsType);
		InfoForStudent info = new Select()
				.from(InfoForStudent.class)
				.where("title = ?", newsType)
				.executeSingle();
		*/

		String outData = getString(R.string.fonStyleStart) +
				body +
				getString(R.string.fonStyleEnd);

		((WebView) rootView).loadDataWithBaseURL("file:///android_asset/", outData, "text/html", "UTF-8", null);

		return rootView;
	}
}