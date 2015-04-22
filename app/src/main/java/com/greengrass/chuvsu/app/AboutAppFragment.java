package com.greengrass.chuvsu.app;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AboutAppFragment extends Fragment {

    public static AboutAppFragment newInstance() {
        AboutAppFragment fragment = new AboutAppFragment();
        return fragment;
    }
    public AboutAppFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

	private void openLink(Uri link){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(link);
		startActivity(intent);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    View rootView = inflater.inflate(
			    R.layout.activity_about_app, container, false);
	    rootView.findViewById(R.id.chuvsuLogo).setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    openLink(Uri.parse("http://www.chuvsu.ru/index.php"));
		    }
	    });

	   /* rootView.findViewById(R.id.grasslogo).setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    openLink(Uri.parse("http://vtrave.com/"));
		    }
	    });*/
	    return rootView;
    }


}
