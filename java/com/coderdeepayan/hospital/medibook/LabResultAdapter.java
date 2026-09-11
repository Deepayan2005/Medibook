package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LabResultAdapter extends RecyclerView.Adapter<LabResultAdapter.PHolder> {
    Context context;
    List<DiagnosticResult> diagnosticResultList;

    public LabResultAdapter(Context context, List<DiagnosticResult> diagnosticResultList) {
        this.context = context;
        this.diagnosticResultList = diagnosticResultList;
    }

    @NonNull
    @Override
    public LabResultAdapter.PHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lab_result_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabResultAdapter.PHolder holder, int position) {
        holder.showResult(diagnosticResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return diagnosticResultList.size();
    }

    class PHolder extends RecyclerView.ViewHolder{
        private TextView parameterNameView,standardValueView,actualValueView;
        public PHolder(@NonNull View itemView) {
            super(itemView);
            parameterNameView = itemView.findViewById(R.id.parameterNameView);
            standardValueView  =itemView.findViewById(R.id.standardValueView);
            actualValueView = itemView.findViewById(R.id.actualValueView);
        }

        public void showResult(DiagnosticResult diagnosticResult) {
            parameterNameView.setText(diagnosticResult.getParameterName());
            standardValueView.setText(diagnosticResult.getStandardValue());
            actualValueView.setText(diagnosticResult.getValue());
            parameterNameView.setTextColor(diagnosticResult.isNormal() ?
                    context.getColor(R.color.green):context.getColor(R.color.red));
            actualValueView.setTextColor(diagnosticResult.isNormal() ?
                    context.getColor(R.color.green):context.getColor(R.color.red));
        }
    }
}
