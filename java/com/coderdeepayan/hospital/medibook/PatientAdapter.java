package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {
    Context context;
    List<Patient> patientList;
    List<Boolean> booleanList;

    public PatientAdapter(Context context, List<Patient> patientList, List<Boolean> booleanList) {
        this.context = context;
        this.patientList = patientList;
        this.booleanList = booleanList;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PatientHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
        holder.setData(patientList.get(position),booleanList.get(position));
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    class PatientHolder extends RecyclerView.ViewHolder{
        private CheckBox checkBox;
        private TextView nameView, umid_IDView;
        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            nameView =itemView.findViewById(R.id.patientNameView);
            umid_IDView=itemView.findViewById(R.id.patientUmidView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                    booleanList.set(getLayoutPosition(),isChecked);
                }
            });
        }
        public void setData(Patient patient, boolean b){
            nameView.setText(patient.getName());
            umid_IDView.setText(patient.getUmid_id());
            checkBox.setChecked(b);
        }
    }

}
