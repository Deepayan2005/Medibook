package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookedDepartmentAdapter extends RecyclerView.Adapter<BookedDepartmentAdapter.BookedHolder> {
    Context context;
    List<Appointment> appointmentList;
    public BookedDepartmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public BookedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookedHolder(LayoutInflater.from(context).inflate(R.layout.booked_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookedHolder holder, int position) {
        holder.setData(appointmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    class BookedHolder extends RecyclerView.ViewHolder{
        private TextView hospitalNameView,unitDeptView,currentQueueView,bookedQueueView,doctorAvailableView;
        public BookedHolder(@NonNull View itemView) {
            super(itemView);
            hospitalNameView=itemView.findViewById(R.id.hospitalNameView3);
            unitDeptView=itemView.findViewById(R.id.departmentUnitNameView2);
            currentQueueView=itemView.findViewById(R.id.currentQueueView);
            bookedQueueView=itemView.findViewById(R.id.bookedQueueNoView);
            doctorAvailableView = itemView.findViewById(R.id.DoctorAvailableView);
        }
        public void setData(Appointment appointment){
            doctorAvailableView.setVisibility(appointment.isDoctorAvailable() ? View.VISIBLE:View.GONE );
            hospitalNameView.setText(appointment.getHospitalName());
            unitDeptView.setText(appointment.getDeptUnitName());
            currentQueueView.setText(appointment.getCurrentQueueNo());
            bookedQueueView.setText(appointment.getPatientQueueNo());
        }
    }

}
