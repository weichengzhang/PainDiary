package com.example.mobilepaindiary;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws ParseException {
//        System.out.println(new Date().toString());
//        System.out.println(new Date().toLocaleString());
//        System.out.println(new Date().toGMTString());
//        Date date = new Date();
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        assertEquals(4, 2 + 2);
//        System.out.println(1/10/100);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String beginTimeString = "2017-03-27";
//
//        String endTimeString = "2017-03-28";
//
//        String nowtimeString = sdf.format(new Date());
//        Date beginTimeDate = sdf.parse(beginTimeString);
//        Date endTimeDate = sdf.parse(endTimeString);
//        System.out.println(beginTimeDate.before(endTimeDate));
//
//        System.out.println(
//                new SimpleDateFormat("yyyy-MM-dd").format(new Date())
//        );
//
//        Calendar instance = Calendar.getInstance();
//        String time = String.format("%d-%d-%d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1, instance.get(Calendar.DAY_OF_MONTH));
//        System.out.println(time);
        String[] split = "15:05".split(":");
        int i = Integer.parseInt(split[1]);
        System.out.println(""+i);
        System.out.println(Arrays.toString(split));


    }
}