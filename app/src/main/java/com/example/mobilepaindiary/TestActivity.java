package com.example.mobilepaindiary;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepaindiary.roomdb.CustomerDatabase;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.example.mobilepaindiary.roomdb.PainRecordDAO;

import java.util.Calendar;
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.btn).setOnClickListener(v -> {
//            PainRecord[] painRecord = new PainRecord[9];
//            for (int i = 0; i < painRecord.length; i++) {
                Calendar instance = Calendar.getInstance();
                instance.set(Calendar.YEAR, 2021);
                instance.set(Calendar.MONTH, 4);
                instance.set(Calendar.DAY_OF_MONTH, 12);
                PainRecord record = new PainRecord();
                record.userMail = "test1@163.com";
                record.targetSteps = "7451" ;
                record.currentSteps = "111";
                record.painLevel = 5;
                record.painLocation = 4;
                Random random = new Random();
                record.painMood = random.nextInt(4) % (4 - 0 + 1) + 0;
                record.currentTime = instance.getTime();
                record.currentPressure = "" + random.nextInt(100) % (100 - 5 + 1) + 5;
                record.currentHumidity = "" + random.nextInt(100) % (100 - 5 + 1) + 5;
                record.currentTemperature = "" + random.nextInt(100) % (100 - 5 + 1) + 5;
//                painRecord[i] = record;
//            }
            PainRecordDAO painRecordDAO = CustomerDatabase.getInstance(TestActivity.this).painRecord();
            long[] insert = painRecordDAO.insert(record);
            Log.e("size ", String.valueOf(insert.length));
        });

        findViewById(R.id.btn).performClick();

    }
}