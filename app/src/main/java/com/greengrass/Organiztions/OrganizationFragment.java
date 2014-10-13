package com.greengrass.Organiztions;



import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.Organization;
import com.greengrass.util.TabsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OrganizationFragment extends Fragment {

    TabsAdapter adapter = new TabsAdapter(new String[]{
            "СНО",
            "Профком",
            "Студсовет",
		    "ТурКлуб"
    }, new Integer[]{
            R.id.sno,
            R.id.profcom,
            R.id.studsovet,
		    R.id.tourclub
    });
    private int[] fragments = new int[]{
            R.id.snoFragment,
            R.id.profcomFragment,
            R.id.studsovetFragment,
		    R.id.tourclubFragment
    };

    public static OrganizationFragment newInstance() {
        OrganizationFragment fragment = new OrganizationFragment();
        return fragment;
    }
    public OrganizationFragment() {
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
	        tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundResource(R.drawable.chuvsu_tab_indicator);
	        ((TextView) tabHost.getTabWidget()
			        .getChildAt(i).findViewById(android.R.id.title))
			        .setTextColor(Color.parseColor("#ffffff"));

            fragmentManager.beginTransaction()
                    .replace(fragments[i], OrganizationInfo.newInstance(adapter.getTitleByID(i)))
                    .commit();
        }

        return rootView;
    }

    public static class OrganizationInfo extends Fragment{

        public static OrganizationInfo newInstance(String organizationType) {
            OrganizationInfo fragment = new OrganizationInfo();

            Bundle args = new Bundle();
            args.putString("ORGANIZATION_TYPE", organizationType);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.text_view_layout, container, false);

            String orgType = getArguments().getString("ORGANIZATION_TYPE");
            Organization info = new Select()
                    .from(Organization.class)
                    .where("name = ?", orgType)
                    .executeSingle();
	        String outData = getString(R.string.fonStyleStart) +
			        info.body +
			        getString(R.string.fonStyleEnd);
	        ((WebView) rootView).loadDataWithBaseURL("file:///android_asset/", outData, "text/html", "UTF-8", null);

            return rootView;
        }
    }

}
