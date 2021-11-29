package com.example.mobilepaindiary.roomdb;


import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao// Data access object
public interface CustomerDAO {
    @Query("SELECT * FROM customer ORDER BY last_name ASC")
    LiveData<List<Customer>> getAll();

    @Query("SELECT * FROM customer WHERE uid = :customerId LIMIT 1")
    Customer findByID(int customerId);

    @Insert
    void insert(Customer customer);

    @Delete
    void delete(Customer customer);

    @Update
    void updateCustomer(Customer customer);

    @Query("DELETE FROM customer")
    void deleteAll();

    @Query("SELECT * FROM customer ORDER BY uid DESC LIMIT 1")
    Customer getLatestCustomer();
}