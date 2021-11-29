package com.example.mobilepaindiary.roomdb;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/9 18:45
 */
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(String value)  {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return  new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

}
