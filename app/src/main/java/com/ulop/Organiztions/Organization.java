package com.ulop.Organiztions;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.ulop.chuvsu.app.R;
import com.ulop.util.TabsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Organization#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Organization extends Fragment {

    TabsAdapter adapter = new TabsAdapter(new String[]{
            "Профком",
            "СНО",
            "Студсовет"
    }, new Integer[]{
            R.id.profcom,
            R.id.sno,
            R.id.studsovet
    });

    public static Organization newInstance() {
        Organization fragment = new Organization();
        return fragment;
    }
    public Organization() {
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
        View rootView = inflater.inflate(R.layout.fragment_organization, container, false);

        FragmentManager fragmentManager = getFragmentManager();


        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        for (int i = 0; i < adapter.getCount(); i++) {
            tabSpec = tabHost.newTabSpec(adapter.getTagByID(i)).
                    setIndicator(adapter.getTitleByID(i)).
                    setContent(adapter.getResourceID(i));

            tabHost.addTab(tabSpec);
        }

        return rootView;
    }


}
