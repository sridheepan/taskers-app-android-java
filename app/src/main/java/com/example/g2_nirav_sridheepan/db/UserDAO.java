package com.example.g2_nirav_sridheepan.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.g2_nirav_sridheepan.models.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(User userToInsert);

    @Query("SELECT * FROM users")
    public List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE email_id = :emailID AND password = :password")
    public User getUser(String emailID, String password);

    @Query("SELECT * FROM users WHERE id = :id")
    public User getUserById(Integer id);

}
