package com.example.g2_nirav_sridheepan.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.g2_nirav_sridheepan.models.Hire;

import java.util.List;

@Dao
public interface HireDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Hire hireToInsert);

    @Query("SELECT * FROM hires WHERE user_id = :userID")
    public List<Hire> getAllHires(int userID);

    @Query("SELECT * FROM hires WHERE id = :hireID")
    public Hire getHire(int hireID);

    @Update
    public void update(Hire hireToUpdate);

}
