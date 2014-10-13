package com.greengrass.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by greengrass on 02.08.14.
 */
public class DatePeriod {
    public String periodName;
    public Date startDate;
    public Date endDate;

    public DatePeriod(String periodName, Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodName = periodName;
    }

    public DatePeriod(String periodName){
        this.periodName = periodName;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1970");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DatePeriod(String periodName, String startDate, String endDate){
        this.periodName = periodName;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        try {
            this.startDate = sdf.parse(startDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        try {
            this.endDate = sdf.parse(endDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }
}
