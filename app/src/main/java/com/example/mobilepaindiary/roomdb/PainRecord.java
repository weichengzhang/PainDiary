package com.example.mobilepaindiary.roomdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class PainRecord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_mail")
    public String userMail;

    @ColumnInfo(name = "pain_level")
    public int painLevel;

    @ColumnInfo(name = "pain_location")
    public int painLocation;

    @ColumnInfo(name = "pain_mood")
    public int painMood;

    @ColumnInfo(name = "target_steps")
    public String targetSteps;

    @ColumnInfo(name = "current_steps")
    public String currentSteps;

    @ColumnInfo(name = "current_time")
    public Date currentTime;

    /**
     * 温度
     */
    @ColumnInfo(name = "current_temperature")
    public String currentTemperature;

    /**
     *湿度
     */
    @ColumnInfo(name = "current_humidity")
    public String currentHumidity;

    /**
     *压力
     */
    @ColumnInfo(name = "current_pressure")
    public String currentPressure;

    @Override
    public String toString() {
        return "PainRecord{" +
                "id=" + id +
                ", userMail='" + userMail + '\'' +
                ", painLevel=" + painLevel +
                ", painLocation=" + painLocation +
                ", painMood=" + painMood +
                ", targetSteps='" + targetSteps + '\'' +
                ", currentSteps='" + currentSteps + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", currentTemperature='" + currentTemperature + '\'' +
                ", currentHumidity='" + currentHumidity + '\'' +
                ", currentPressure='" + currentPressure + '\'' +
                '}';
    }
}
