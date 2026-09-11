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

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.PatientHolder> {
    Context context;
    List<Patient> patientList;
    AccountListener accountListener;

    public AccountAdapter(Context context, List<Patient> patientList, AccountListener accountListener) {
        this.context = context;
        this.patientList = patientList;
        this.accountListener = accountListener;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
        holder.setData(patientList.get(position));
    }
    @Override
    public int getItemCount() {
        return patientList.size();
    }
    class PatientHolder extends RecyclerView.ViewHolder{
        private TextView nameView, umid_IDView;
        private MaterialCardView materialCardView;
        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            nameView =itemView.findViewById(R.id.patientNameView2);
            umid_IDView=itemView.findViewById(R.id.patientUmidView2);
            materialCardView = itemView.findViewById(R.id.materialCardView1);
            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountListener.selectAccount(getLayoutPosition());
                }
            });
        }
        public void setData(Patient patient){
            nameView.setText(patient.getName());
            umid_IDView.setText(patient.getUmid_id());
        }
    }

}
