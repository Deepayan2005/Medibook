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

public class HospitalAdapter extends RecyclerView.Adapter {
    Context context;
    List<Hospital> hospitalList;
    SelectHospitalListener selectHospitalListener;
    SearchButtonListener searchButtonListener;
    PrescriptionListener prescriptionListener;
    QueueButtonListener queueButtonListener;
    DiagnosticListener diagnosticListener;
    public HospitalAdapter(HospitalActivity context, List<Hospital> hospitalList,
                           SelectHospitalListener selectHospitalListener,
                           SearchButtonListener searchButtonListener,
                           PrescriptionListener prescriptionListener,
                           QueueButtonListener queueButtonListener,
                           DiagnosticListener diagnosticListener) {
        this.context = context;
        this.selectHospitalListener = selectHospitalListener;
        this.hospitalList = hospitalList;
        this.searchButtonListener = searchButtonListener;
        this.prescriptionListener = prescriptionListener;
        this.queueButtonListener = queueButtonListener;
        this.diagnosticListener = diagnosticListener;
    }

    @Override
    public int getItemViewType(int position) {
        return hospitalList.get(position).getViewType();
    }

    public void setHospitalList(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Hospital.HOSPITAL:
                return new HospitalHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hospital_view,parent,false));
            case Hospital.UTILITIES:
                return new UtilitiesHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.utilities_view,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (hospitalList.get(position).getViewType()){
            case Hospital.HOSPITAL:
                ((HospitalHolder)holder).setData(hospitalList.get(position));
                break;
            case Hospital.UTILITIES:
                ((UtilitiesHolder)holder).showButtons();
                break;

        }

    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    class HospitalHolder extends RecyclerView.ViewHolder{
        private MaterialCardView hospitalContainerView;
        private TextView hospitalNameView,addressView;
        public HospitalHolder(@NonNull View itemView) {
            super(itemView);
            hospitalNameView=itemView.findViewById(R.id.hospitalNameView);
            addressView=itemView.findViewById(R.id.addressView);
            hospitalContainerView=itemView.findViewById(R.id.hospitalContainerView);
        }
        public void setData(Hospital hospital){
            hospitalNameView.setText(hospital.getName());
            addressView.setText(hospital.getAddress());
            hospitalContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectHospitalListener.selectHospital(hospital);
                }
            });
        }
    }

    class UtilitiesHolder extends RecyclerView.ViewHolder{
        private MaterialCardView searchButton,prescriptionButton, queueButton,diagnosticButton;
        public UtilitiesHolder(@NonNull View itemView) {
            super(itemView);
            searchButton=itemView.findViewById(R.id.searchButton);
            prescriptionButton= itemView.findViewById(R.id.prescriptionButton);
            queueButton= itemView.findViewById(R.id.viewQueueButton);
            diagnosticButton = itemView.findViewById(R.id.analysisButton);
        }

        public void showButtons() {
            diagnosticButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diagnosticListener.viewLabTest();
                }
            });
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchButtonListener.wantSearch();
                }
            });
            prescriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prescriptionListener.seePrescription();
                }
            });
            queueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queueButtonListener.showQueue();
                }
            });
        }
    }
}
