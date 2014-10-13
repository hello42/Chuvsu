package com.greengrass.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import android.widget.TextView;


import com.greengrass.chuvsu.app.R;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by greengrass on 02.08.14.
 */
public class MenuByTime extends BaseAdapter{

    ArrayList<DatePeriod> pList = new ArrayList<DatePeriod>();

    Context ctx;
    LayoutInflater lInflater;

    {
	    addPeriod(new DatePeriod("Анонсы"));
	    addPeriod(new DatePeriod("Университет"));
	    addPeriod(new DatePeriod("Абитуриент", "01/03", "25/08"));
	    addPeriod(new DatePeriod("Первокурсник", "25/08", "01/11"));
	    addPeriod(new DatePeriod("Новости"));
        addPeriod(new DatePeriod("Факультеты"));
        addPeriod(new DatePeriod("Студент"));
        addPeriod(new DatePeriod("Библиотека"));
	    addPeriod(new DatePeriod("Организации"));
        addPeriod(new DatePeriod("Справочник"));
    }

    public MenuByTime(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addPeriod(DatePeriod period){
        if (needShow(period)) {
            pList.add(period);
        }
    }

    public boolean needShow(DatePeriod period){

        Date today = new Date();
        today.setYear(70);

        return today.getTime() >= period.startDate.getTime() &&
                today.getTime() <= period.endDate.getTime();
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int i) {
        return pList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public String getItemTitle(int id){
        return ((DatePeriod) getItem(id)).periodName;
    }

	public int getIndexByTitle(String aTitile){
		int index = 0;
		for (int i = 0; i < pList.size(); i++) {
			if (getItemTitle(i).equals(aTitile)) {
				index = i;
			}
		}
		return index;
	}

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.menu_element, parent, false);
        }
	    TextView item = (TextView) view.findViewById(R.id.item);
	    Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Medium.ttf");
		item.setTypeface(typeface);
	    item.setText(((DatePeriod) getItem(i)).periodName);
        return view;
    }
}
