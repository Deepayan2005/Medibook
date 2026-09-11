package com.coderdeepayan.hospital.medibook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DiagnosticAdapter extends RecyclerView.Adapter<DiagnosticAdapter.PHolder> {
    List<Diagnosis> diagnosisList;
    LabReportDownloadListener labReportDownloadListener;
    LabResultListener labResultListener;

    public DiagnosticAdapter(List<Diagnosis> diagnosisList,
                             LabReportDownloadListener labReportDownloadListener,
                             LabResultListener labResultListener) {
        this.diagnosisList = diagnosisList;
        this.labReportDownloadListener = labReportDownloadListener;
        this.labResultListener = labResultListener;
    }
    public void setDiagnosisList(List<Diagnosis> diagnosisList) {
        this.diagnosisList = diagnosisList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiagnosticAdapter.PHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnosis_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosticAdapter.PHolder holder, int position) {
        holder.showDiagnosticData(diagnosisList.get(position));
    }

    @Override
    public int getItemCount() {
        return diagnosisList.size();
    }

    class PHolder extends RecyclerView.ViewHolder{
        private MaterialCardView materialCardView;
        private TextView departmentNameView,labNameView,testNameView,requestDateView;
        private ImageView imageView;
        public PHolder(@NonNull View itemView) {
            super(itemView);
            departmentNameView = itemView.findViewById(R.id.departmentUnitNameView4);
            labNameView = itemView.findViewById(R.id.labNameView);
            imageView = itemView.findViewById(R.id.imageView2);
            testNameView = itemView.findViewById(R.id.testNameView);
            requestDateView = itemView.findViewById(R.id.requestDateView);
            materialCardView = itemView.findViewById(R.id.materialCardView2);

            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labResultListener.viewLabReport(getLayoutPosition());
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labReportDownloadListener.downloadLabReport(getLayoutPosition());
                }
            });
        }
        public void showDiagnosticData(Diagnosis diagnosis) {
            departmentNameView.setText(diagnosis.getDeptName());
            requestDateView.setText(diagnosis.getRequestDate());
            labNameView.setText(diagnosis.getLabName());
            testNameView.setText(diagnosis.getTestName());
            imageView.setVisibility(diagnosis.isReportMade() ? View.VISIBLE : View.GONE);
        }
    }
}
