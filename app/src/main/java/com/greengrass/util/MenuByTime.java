package com.greengrass.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import android.widget.ImageView;
import android.widget.TextView;


import com.greengrass.chuvsu.app.R;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by greengrass on 02.08.14.
 */
public class MenuByTime extends BaseAdapter{

    static ArrayList<DatePeriod> pList = new ArrayList<>();

    Context ctx;
    LayoutInflater lInflater;

    static {
	    addPeriod(new DatePeriod("Анонсы", R.drawable.anons));
	    addPeriod(new DatePeriod("Университет", R.drawable.university));
	    addPeriod(new DatePeriod("Абитуриент", "01/03", "25/08", R.drawable.cow_trans));
	    addPeriod(new DatePeriod("Первокурсник", "25/08", "01/11", R.drawable.abit));
	    addPeriod(new DatePeriod("Новости", R.drawable.news));
        addPeriod(new DatePeriod("Факультеты", R.drawable.facultets));
        addPeriod(new DatePeriod("Студент", R.drawable.student));
        addPeriod(new DatePeriod("Библиотека", R.drawable.library));
	    addPeriod(new DatePeriod("Организации", R.drawable.organisations));
        addPeriod(new DatePeriod("Справочник", R.drawable.spravochnik));
    }

    public MenuByTime(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static void addPeriod(DatePeriod period){
        if (needShow(period)) {
            pList.add(period);
        }
    }

    public static boolean needShow(DatePeriod period){

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
	    Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Regular.ttf");
		//item.setTypeface(typeface);

	    ImageView imageView = (ImageView) view.findViewById(R.id.menu_item_icon);
	    imageView.setImageResource(((DatePeriod) getItem(i)).iconID);

	    item.setText(((DatePeriod) getItem(i)).periodName);
        return view;
    }

    public static ArrayList<IDrawerItem> getDrawerItems(){
        ArrayList<IDrawerItem> dList = new ArrayList<>();
        for(DatePeriod period:pList){
            dList.add(new PrimaryDrawerItem()
                            .withName(period.periodName)
                            .withIcon(period.iconID)
                            .withDisabledIconColor(Color.parseColor("#ffcd0ae0"))
                            .withSelectedIconColor(Color.parseColor("#ff1d4474"))
                            .withTintSelectedIcon(true)
                            //.add something
            );
        }
        return dList;
    }
}
