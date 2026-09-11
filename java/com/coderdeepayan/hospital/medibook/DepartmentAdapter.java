package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentHolder> {
    Context context;
    List<Department> departmentList;
    DepartmentListener listener;

    public DepartmentAdapter(Context context, List<Department> departmentList, DepartmentListener listener) {
        this.context = context;
        this.departmentList = departmentList;
        this.listener = listener;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new DepartmentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentHolder holder, int position) {
        holder.setData(departmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    class DepartmentHolder extends RecyclerView.ViewHolder{
        private MaterialCardView deptContainerView;
        private TextView deptNameView,unitNameView,doctorNameView;
        public DepartmentHolder(@NonNull View itemView) {
            super(itemView);
            deptContainerView=itemView.findViewById(R.id.deptContainerView);
            deptNameView=itemView.findViewById(R.id.departmentNameView);
            unitNameView=itemView.findViewById(R.id.unitNameView);
            doctorNameView=itemView.findViewById(R.id.doctorNameView);

        }
        public void setData(Department department){
            deptNameView.setText(department.getDepartmentName());
            unitNameView.setText(department.getOpdName());
            if (department.getDoctorName().trim().length()>0){
                doctorNameView.setText(department.getDoctorName());
                doctorNameView.setVisibility(View.VISIBLE);
            }
            else {
                doctorNameView.setVisibility(View.GONE);
            }
            deptContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.selectDepartment(getLayoutPosition());
                }
            });
        }
    }

}
