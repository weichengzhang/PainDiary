package com.example.mobilepaindiary.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface PainRecordDAO {

    @Insert
    long[] insert(PainRecord... painRecord);

    @Delete
    int delete(PainRecord... painRecord);

    @Update
    int update(PainRecord... painRecord);

    @Query("SELECT * FROM painrecord where user_mail = :userMail ORDER BY `current_time` DESC")
    LiveData<List<PainRecord>> getCurrentUserAllPainRecord(String userMail);

    @Query("SELECT * FROM painrecord where id = :id")
    LiveData<PainRecord> findById(int id);

    @Query("SELECT * FROM painrecord where `current_time` = :time and user_mail = :userMail")
    LiveData<PainRecord> findByTimeAndUserMail(Date time, String userMail);

    @Query("SELECT * FROM painrecord where user_mail = :userMail ORDER BY id DESC")
    LiveData<List<PainRecord>> findByUserMail(String userMail);

    @Query("SELECT count(pain_location)  FROM painrecord where user_mail = :userMail and pain_location = :location")
    int findLocationCount(String location, String userMail);

    @Query("SELECT * FROM painrecord where`current_time` BETWEEN :startTime and :endTime")
    List<PainRecord> findByTime( Date startTime, Date endTime);
}
