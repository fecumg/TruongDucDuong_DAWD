package fpt.edu.dawd_test.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.dawd_test.R;
import fpt.edu.dawd_test.entities.Employee;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    public static final String ONCLICK_EMPLOYEE_ACTION = "edit-employee";
    Activity activity;
    List<Employee> employees;

    public EmployeeAdapter(Activity activity, List<Employee> employees) {
        this.activity = activity;
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.textViewName.setText(employee.getName());
        holder.textViewDesignation.setText(employee.getDesignation());
        holder.textViewSalary.setText(String.valueOf(employee.getSalary()));

        holder.layoutEmployee.setOnClickListener(view -> onClickEmployee(view, employee.getId(), position));
    }

    @Override
    public int getItemCount() {
        return employees == null ? 0 : employees.size();
    }

    private void onClickEmployee(View view, int id, int position) {
        Intent intent = new Intent(ONCLICK_EMPLOYEE_ACTION);
        intent.putExtra("id", id);
        intent.putExtra("position", position);
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
    }

    public void refreshView(List<Employee> employees) {
        this.employees = employees;
        notifyItemRangeChanged(0, employees.size());
    }

    public void refreshNewItem(Employee employee) {
        this.employees.add(employee);
        notifyItemInserted(this.employees.size());
    }

    public void refreshChangedItem(int position, Employee employee) {
        this.employees.set(position, employee);
        notifyItemChanged(position);
    }

    public void refreshRemovedItem(int position) {
        this.employees.remove(position);
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDesignation, textViewSalary;

        RelativeLayout layoutEmployee;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDesignation = itemView.findViewById(R.id.textViewDesignation);
            textViewSalary = itemView.findViewById(R.id.textViewSalary);

            layoutEmployee = itemView.findViewById(R.id.layoutEmployee);
        }
    }
}
