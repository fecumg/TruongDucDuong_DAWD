package fpt.edu.dawd_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.dawd_test.adapters.EmployeeAdapter;
import fpt.edu.dawd_test.database.AppDatabase;
import fpt.edu.dawd_test.entities.Employee;

public class MainActivity extends AppCompatActivity {

    EditText textEditName, textEditDesignation, textEditSalary;
    Button buttonAdd, buttonUpdate, buttonDelete;
    RecyclerView recyclerViewEmployee;

    int editEmployeeId;

    AppDatabase appDatabase;
    EmployeeAdapter employeeAdapter;
    List<Employee> employees = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.getInstance(this);

        textEditName = findViewById(R.id.textEditName);
        textEditDesignation = findViewById(R.id.textEditDesignation);
        textEditSalary = findViewById(R.id.textEditSalary);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        int editId = getIntent().getIntExtra("id", 0);
        if (editId != 0) {
            editEmployeeId = editId;
            System.out.println(editId);
            bindEditEmployee(editEmployeeId);
            buttonUpdate.setOnClickListener(view -> this.updateEmployee(editEmployeeId));
            buttonDelete.setOnClickListener(view -> this.deleteEmployee(editEmployeeId));
        }

        this.initView();
        this.fetchEmployees();

        buttonAdd.setOnClickListener(view -> this.addEmployee());
    }

    private void initView() {
        recyclerViewEmployee = findViewById(R.id.recyclerViewEmployee);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewEmployee.setLayoutManager(layoutManager);

        employeeAdapter = new EmployeeAdapter(this, employees);
        recyclerViewEmployee.setAdapter(employeeAdapter);
    }

    private void fetchEmployees() {
        employees = appDatabase.employeeDao().findAll();
        employeeAdapter.refreshView(employees);
    }

    private void addEmployee() {
        Employee employee = validateEmployee();

        if (employee == null) {
            return;
        }

        appDatabase.employeeDao().save(employee);
        Toast.makeText(this, "New employee added successfully", Toast.LENGTH_SHORT).show();
        this.fetchEmployees();
        this.flushTextView();
    }

    private Employee validateEmployee() {
        String errorMessage = null;
        String name = textEditName.getText().toString();
        String designation = textEditDesignation.getText().toString();
        String salaryStr = textEditSalary.getText().toString();
        int salary = this.parseSalary(salaryStr);

        if (name.trim().isEmpty()) {
            errorMessage = "Employee name cannot be empty";
        } else if (designation.trim().isEmpty()) {
            errorMessage = "Designation must be filled";
        } else if (salaryStr.trim().isEmpty()) {
            errorMessage = "Salary must be filled";
        } else if (salary == -1) {
            Toast.makeText(this, "Salary must be a number", Toast.LENGTH_SHORT).show();
        }

        if (errorMessage == null) {
            return new Employee(name, designation, salary);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        return null;
    }

    public void bindEditEmployee(int id) {
        Employee employee = appDatabase.employeeDao().findById(id);
        textEditName.setText(employee.getName());
        textEditDesignation.setText(employee.getDesignation());
        textEditSalary.setText(String.valueOf(employee.getSalary()));
    }

    private void updateEmployee(int id) {
        if (id != 0) {
            Employee employee = validateEmployee();

            if (employee == null) {
                return;
            }

            employee.setId(id);

            appDatabase.employeeDao().update(employee);
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
        }
        this.fetchEmployees();
    }

    private void deleteEmployee(int id) {
        if (id != 0) {
            Employee employee = appDatabase.employeeDao().findById(id);
            appDatabase.employeeDao().delete(employee);
            Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
            this.fetchEmployees();
            this.flushTextView();
        }
    }

    private void flushTextView() {
        textEditName.setText("");
        textEditDesignation.setText("");
        textEditSalary.setText("");
        editEmployeeId = 0;
    }

    private int parseSalary(String salaryStr) {
        int salary = -1;
        try {
            salary = Integer.parseInt(salaryStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return salary;
    }
}