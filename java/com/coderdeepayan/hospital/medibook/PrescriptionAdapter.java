package com.coderdeepayan.hospital.medibook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PHolder> {
    List<Prescription> prescriptionList;
    DownloadPrescriptionListener downloadPrescriptionListener;

    public PrescriptionAdapter(List<Prescription> prescriptionList,
                               DownloadPrescriptionListener downloadPrescriptionListener) {
        this.prescriptionList = prescriptionList;
        this.downloadPrescriptionListener = downloadPrescriptionListener;
    }

    public void setPrescriptionList(List<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrescriptionAdapter.PHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pres_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionAdapter.PHolder holder, int position) {
        holder.showPrescription(prescriptionList.get(position));
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    class PHolder extends RecyclerView.ViewHolder{
        private TextView departmentNameView,dateView,hospitalNameView;
        private ImageView imageView;
        public PHolder(@NonNull View itemView) {
            super(itemView);
            departmentNameView = itemView.findViewById(R.id.departmentUnitNameView3);
            dateView = itemView.findViewById(R.id.dateView);
            imageView = itemView.findViewById(R.id.imageView);
            hospitalNameView = itemView.findViewById(R.id.hospitalNameView4);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPrescriptionListener.downloadPrescription(getLayoutPosition());
                }
            });
        }
        public void showPrescription(Prescription prescription){
            departmentNameView.setText(prescription.getDepartmentUnitName());
            dateView.setText(prescription.getFormattedDate());
            hospitalNameView.setText(prescription.getHospitalName());
        }
    }
}
