package com.ulop.student;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ulop.chuvsu.app.R;
import com.ulop.models.StaticInfo;

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
        TextView body = (TextView) rootView.findViewById(R.id.body);

        title.setText(info.title);
        body.setText(Html.fromHtml(info.body));
        return rootView;
    }


}
