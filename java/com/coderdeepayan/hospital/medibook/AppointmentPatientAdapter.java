package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentPatientAdapter extends RecyclerView.Adapter<AppointmentPatientAdapter.APHolder> {
    Context context;
    List<Patient> patientList;

    public AppointmentPatientAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public APHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new APHolder(LayoutInflater.from(context).inflate(R.layout.ap_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull APHolder holder, int position) {
        holder.setData(patientList.get(position));

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
    class APHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        private RecyclerView recyclerView;
        private BookedDepartmentAdapter bookedDepartmentAdapter;
        public APHolder(@NonNull View itemView) {
            super(itemView);
            nameView=itemView.findViewById(R.id.patientNameView_AP);
            recyclerView=itemView.findViewById(R.id.bookedDepartmentsRecyclerView);
        }
        public void setData(Patient patient){
            nameView.setText(patient.getName());

            bookedDepartmentAdapter = new BookedDepartmentAdapter(context,patient.getAppointmentList());
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,
                    false));
            recyclerView.setAdapter(bookedDepartmentAdapter);
            bookedDepartmentAdapter.notifyDataSetChanged();
        }
    }

}
