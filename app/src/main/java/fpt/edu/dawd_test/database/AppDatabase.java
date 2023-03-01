package fpt.edu.dawd_test.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fpt.edu.dawd_test.daos.EmployeeDao;
import fpt.edu.dawd_test.entities.Employee;

@Database(entities = {Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public abstract EmployeeDao employeeDao();

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "DAWD.db")
                    .allowMainThreadQueries().build();
        }
        return appDatabase;
    }
}
