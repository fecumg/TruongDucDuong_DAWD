package fpt.edu.dawd_test.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.dawd_test.R;
import fpt.edu.dawd_test.entities.Employee;

public class EmployeeAdapter extends BaseAdapter<Employee, EmployeeAdapter.ViewHolder> {

    public EmployeeAdapter(List<Employee> objects, String action) {
        super(objects, action);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Employee employee = objects.get(position);
        holder.textViewName.setText(employee.getName());
        holder.textViewDesignation.setText(employee.getDesignation());
        holder.textViewSalary.setText(String.valueOf(employee.getSalary()));

        holder.layoutEmployee.setOnClickListener(view -> onClickItem(view, employee.getId(), position));
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
