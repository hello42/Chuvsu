package com.ulop.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ulop on 02.08.14.
 */
public class MenuByTime extends BaseAdapter{

    ArrayList<DatePeriod> pList = new ArrayList<DatePeriod>();

    Context ctx;
    LayoutInflater lInflater;

    {
        addPeriod(new DatePeriod("Новости"));
        addPeriod(new DatePeriod("Университет"));
        addPeriod(new DatePeriod("Факультеты"));
        addPeriod(new DatePeriod("Абитуриент"));
        addPeriod(new DatePeriod("Справочник"));
        addPeriod(new DatePeriod("Студент"));
        addPeriod(new DatePeriod("Организации", "01/01/2014", "01/05/2014"));
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

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        ((TextView) view).setText(((DatePeriod) getItem(i)).periodName);
        return view;
    }
}
