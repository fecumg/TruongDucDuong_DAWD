package fpt.edu.dawd_test.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fpt.edu.dawd_test.entities.Employee;

@Dao
public interface EmployeeDao {
    @Insert
    void save(Employee employee);

    @Update
    void update(Employee employee);

    @Query(value = "SELECT * FROM employee WHERE id=:id")
    Employee findById(int id);

    @Query(value = "SELECT * FROM employee")
    List<Employee> findAll();

    @Delete
    void delete(Employee employee);
}
