package com.ulop.newscardlist.dummy;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ulop.chuvsu.app.R;

import java.util.ArrayList;

public class NewsCardAdapter extends BaseAdapter {
	
	  Context ctx;
	  LayoutInflater lInflater;
	  public static ArrayList<NewsCard> objects = new ArrayList<NewsCard>();

	public NewsCardAdapter(Context context) {
		// TODO Auto-generated constructor stub
		ctx = context;
		lInflater = (LayoutInflater) ctx
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}



    public static void addItem(NewsCard item) {
        objects.add(0, item);

    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return objects.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.news_card, parent, false);
		}
		NewsCard nCard = getNewsCard(position);
		((TextView) view.findViewById(R.id.Title)).setText(nCard.title);
		((TextView) view.findViewById(R.id.newsText)).setText(Html.fromHtml(nCard.content));
		((TextView) view.findViewById(R.id.publishing_date)).setText(nCard.publicTime);
		
		return view;
	}
	
	NewsCard getNewsCard(int position){
		return ((NewsCard) getItem(position));
	}



    public static class NewsCard {
        public String title;
        public String content;
        public String publicTime;
        public String id;
        public int image;

        public NewsCard(String _title, String _content, String _time, int _image){
            title = _title;
            content = _content;
            publicTime = _time;
            image = _image;
        }

        @Override
        public String toString() {
            return content;
        }
    }

}
