package com.greengrass.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greengrass on 01.08.14.
 */public class TabsAdapter{
    List<String> tList;
    List<Integer> vList;


    public TabsAdapter(String[] tabs, Integer[] views) {
        tList = new ArrayList<String>();
        vList = new ArrayList<Integer>();
        for (String e:tabs){
            tList.add(e);
        }

        for (Integer e:views){
            vList.add(e);
        }
    }

    public int getCount(){
        return tList.size();
    }

    public String getTitleByID(int id){
        return tList.get(id);
    }

    public String getTagByID(int id){
        return String.valueOf(tList.get(id).hashCode());
    }

    public Integer getResourceID(int id){
        return vList.get(id);
    }


}
