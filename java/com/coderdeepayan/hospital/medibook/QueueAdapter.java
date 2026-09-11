package com.coderdeepayan.hospital.medibook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueHolder> {
    Context context;
    List<Queue> queueList;

    public QueueAdapter(Context context, List<Queue> queueList) {
        this.context = context;
        this.queueList = queueList;
    }

    @NonNull
    @Override
    public QueueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QueueHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queue_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, int position) {
        holder.setData(queueList.get(position));

    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    class QueueHolder extends RecyclerView.ViewHolder{

        private TextView nameView, depeartmentUnitView,queueView;
        public QueueHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.patientNameView2);
            depeartmentUnitView= itemView.findViewById(R.id.departmentUnitNameView);
            queueView=itemView.findViewById(R.id.queueNumberView);
        }
        public void setData(Queue queue){
            nameView.setText(queue.getName());
            depeartmentUnitView.setText(queue.getDepartmentUnitName());
            queueView.setText(queue.getQueueNumber());
        }
    }

}
