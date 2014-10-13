package com.greengrass.student;



import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.StaticInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PagesFragment extends Fragment {

    public static PagesFragment newInstance(Integer page) {
        PagesFragment fragment = new PagesFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }
    public PagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 1);
        StaticInfo info = new Select()
                .from(StaticInfo.class)
                .where("page_id = ?", page)
                .executeSingle();

        View rootView = inflater.inflate(R.layout.fragment_about_university, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        WebView body = (WebView) rootView.findViewById(R.id.body);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
		String outData = getString(R.string.fonStyleStart) +
						info.body +
						getString(R.string.fonStyleEnd);

        title.setText(info.title);
        body.loadDataWithBaseURL("file:///android_asset/", outData, "text/html", "UTF-8", null);

        //body.setMovementMethod(LinkMovementMethod.getInstance());

        //body.setTypeface(typeface);
        title.setTypeface(typeface);
        return rootView;
    }


}
