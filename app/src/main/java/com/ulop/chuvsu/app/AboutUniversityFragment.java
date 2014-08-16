package com.ulop.chuvsu.app;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUniversityFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AboutUniversityFragment extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static AboutUniversityFragment newInstance() {
        AboutUniversityFragment fragment = new AboutUniversityFragment();
        return fragment;
    }
    public AboutUniversityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_university, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView body = (TextView) view.findViewById(R.id.body);

        //TODO доделать
        return view;
    }


}
