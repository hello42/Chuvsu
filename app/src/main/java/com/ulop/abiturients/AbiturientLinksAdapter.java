package com.ulop.abiturients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ulop on 21.07.14.
 */
public class AbiturientLinksAdapter extends BaseAdapter {


    Context ctx;
    LayoutInflater lInflater;
    public static ArrayList<Link> objects = new ArrayList<Link>();

    static {
        addItem(new Link("Приемная коммисия университета", "http://abiturient.chuvsu.ru/"));
        addItem(new Link("Прием 2014", "http://abiturient.chuvsu.ru/index.php/priem-2014"));
        addItem(new Link("Список подавших заявление", "http://cool.chuvsu.ru/~sabirov/statistic/rating.php"));

    }

    public AbiturientLinksAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static void addItem(Link link){
        objects.add(link);
    }

    public Link getLinkItem(int i){
        return (Link) getItem(i);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        ((TextView) view).setText(((Link) getItem(i)).title);
        return view;
    }

    public static class Link{
        public String title;
        public String link;

        public Link(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }
}
