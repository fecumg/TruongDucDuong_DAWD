package fpt.edu.dawd_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import fpt.edu.dawd_test.adapters.EmployeeAdapter;
import fpt.edu.dawd_test.database.AppDatabase;
import fpt.edu.dawd_test.entities.Employee;

public class MainActivity extends AppCompatActivity {

    EditText textEditName, textEditDesignation, textEditSalary;
    Button buttonAdd, buttonUpdate, buttonDelete;
    RecyclerView recyclerViewEmployee;

    AppDatabase appDatabase;
    EmployeeAdapter employeeAdapter;

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

        buttonAdd.setOnClickListener(view -> this.addEmployee());

        this.initView();
        employeeAdapter.refreshView(appDatabase.employeeDao().findAll());

        this.listenToAdapterOnClick();
    }

    private void initView() {
        recyclerViewEmployee = findViewById(R.id.recyclerViewEmployee);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewEmployee.setLayoutManager(layoutManager);

        employeeAdapter = new EmployeeAdapter(this, new ArrayList<>());
        recyclerViewEmployee.setAdapter(employeeAdapter);
    }

    private void listenToAdapterOnClick() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int editEmployeeId = intent.getIntExtra("id", 0);
                int editEmployeePosition = intent.getIntExtra("position", -1);
                if (editEmployeeId != 0) {
                    bindEditEmployee(editEmployeeId);
                    buttonUpdate.setOnClickListener(view -> updateEmployee(editEmployeeId, editEmployeePosition));
                    buttonDelete.setOnClickListener(view -> deleteEmployee(editEmployeeId, editEmployeePosition));
                }
            }
        }, new IntentFilter(EmployeeAdapter.ONCLICK_EMPLOYEE_ACTION));
    }

    private void addEmployee() {
        Employee employee = validateEmployee();

        if (employee == null) {
            return;
        }

        int newId = (int) appDatabase.employeeDao().save(employee);
        Toast.makeText(this, "New employee added successfully", Toast.LENGTH_SHORT).show();
        employeeAdapter.refreshNewItem(appDatabase.employeeDao().findById(newId));
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

    private void updateEmployee(int id, int position) {
        if (id != 0) {
            Employee employee = validateEmployee();

            if (employee == null) {
                return;
            }

            employee.setId(id);

            appDatabase.employeeDao().update(employee);
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
            employeeAdapter.refreshChangedItem(position, appDatabase.employeeDao().findById(id));
        }
    }

    private void deleteEmployee(int id, int position) {
        if (id != 0) {
            Employee employee = appDatabase.employeeDao().findById(id);
            appDatabase.employeeDao().delete(employee);
            Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
            employeeAdapter.refreshRemovedItem(position);
            this.flushTextView();
        }
    }

    private void flushTextView() {
        textEditName.setText("");
        textEditDesignation.setText("");
        textEditSalary.setText("");
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