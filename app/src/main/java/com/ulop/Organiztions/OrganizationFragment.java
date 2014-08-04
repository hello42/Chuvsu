package com.ulop.Organiztions;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ulop.chuvsu.app.R;
import com.ulop.models.Organization;
import com.ulop.util.TabsAdapter;

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
            "Студсовет"
    }, new Integer[]{
            R.id.sno,
            R.id.profcom,
            R.id.studsovet
    });

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
        }

        return rootView;
    }

    public static class OrganizationInfo extends Fragment{

        public static OrganizationInfo newInstance(int organizationType) {
            OrganizationInfo fragment = new OrganizationInfo();

            Bundle args = new Bundle();
            args.putInt("ORGANIZATION_TYPE", organizationType);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_about_university, container, false);

            int orgType = getArguments().getInt("ORGANIZATION_TYPE");
            Organization info = new Select()
                    .from(Organization.class)
                    .where("organizationID = ?", orgType + 1)
                    .executeSingle();

            ((TextView) rootView.findViewById(R.id.title)).setText(info.name);
            ((TextView) rootView.findViewById(R.id.body)).setText(Html.fromHtml(info.body));

            return rootView;
        }
    }

}
