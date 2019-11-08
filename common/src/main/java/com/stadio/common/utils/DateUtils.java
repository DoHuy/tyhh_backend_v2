package com.stadio.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils
{
    private static Logger logger = LogManager.getLogger(DateUtils.class);

    public static Date dateFormStr(String inDate, String dateFormatStr)
    {
        logger.debug(">>> dateFormStr: " + inDate + " -> " + dateFormatStr);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        dateFormat.setLenient(false);
        try
        {
            return dateFormat.parse(inDate.trim());
        }
        catch (ParseException pe)
        {
            return null;
        }
    }

    public static String normalFormatGMT7DateString(Date date) {
        DateFormat df = new SimpleDateFormat("dd MM yyyy HH-mm-ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return df.format(date);
    }

}
