package com.ulop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ulop on 02.08.14.
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
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2090");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DatePeriod(String periodName, String startDate, String endDate){
        this.periodName = periodName;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
