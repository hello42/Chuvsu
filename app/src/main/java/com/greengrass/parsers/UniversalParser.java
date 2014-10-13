package com.greengrass.parsers;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by greengrass on 05.08.14.
 */
public class UniversalParser extends BaseParser{
    public <T extends Model> List<T> getAsModelLis(Class<? extends Model> type, InputStream inputStream){
        List<T> tList = new ArrayList<T>();
        List<Field> fields = new LinkedList<Field>( new TableInfo(type).getFields());

        try {
            JSONArray jArray = getJSONArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return tList;
    }
}
