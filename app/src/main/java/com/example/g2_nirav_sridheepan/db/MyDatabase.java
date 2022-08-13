package com.example.g2_nirav_sridheepan.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.g2_nirav_sridheepan.models.Hire;
import com.example.g2_nirav_sridheepan.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: Add the database annotation
@Database(entities = {User.class, Hire.class}, version=2, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract HireDAO hireDAO();

    private static volatile MyDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "mydb")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

