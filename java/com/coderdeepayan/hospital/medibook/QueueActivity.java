package com.coderdeepayan.hospital.medibook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QueueActivity extends AppCompatActivity {
    List<Patient> patientsBookingList = new ArrayList<>();
    HospitalDatabase hospitalDatabase;
    AppointmentPatientAdapter appointmentPatientAdapter;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    Thread thread;
    HospitalService hospitalService = new HospitalService();
    TextView noBookingView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread!=null){
            if (thread.isAlive()){
                thread.interrupt();
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        recyclerView=findViewById(R.id.queueRecyclerView2);
        linearLayout=findViewById(R.id.loadingLayout2);
        noBookingView = findViewById(R.id.noBookingMadeView);

        hospitalDatabase=new HospitalDatabase(this);

        linearLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        noBookingView.setVisibility(View.GONE);

        appointmentPatientAdapter=new AppointmentPatientAdapter(this,patientsBookingList);
        recyclerView.setAdapter(appointmentPatientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Patient> patientsRecords = hospitalDatabase.getPatients();

                    for (int i = 0; i <patientsRecords.size() ; i++) {
                        List<Appointment> appointmentList = new ArrayList<>();

                        String data = new JSONArray(hospitalService.getLiveQueueStatus(patientsRecords.get(i).getCrNo()))
                                .getJSONObject(0).getString("qnodata");
                        JSONArray live_JsonArray= new JSONArray(data);
                        JSONArray booking_JsonArray = new JSONObject(hospitalService.getBookingData(
                                patientsRecords.get(i).getCrNo())).getJSONArray("episode_list");

                        Log.d("live", "Found live queue Data of Size = "+live_JsonArray.length());
                        Log.d("Booking", "Found Booking Data of Size = "+booking_JsonArray.length());

                        for (int j = 0; j <live_JsonArray.length() ; j++) {

                            Appointment appointment = new Appointment();
                            String unitName = live_JsonArray.getJSONObject(j).getString("unit_name").toLowerCase();
                            for (int k = 0; k <booking_JsonArray.length() ; k++) {
                                if (unitName.equalsIgnoreCase(booking_JsonArray.getJSONObject(k)
                                        .getString("DEPTUNITNAME"))
                                        && patientsRecords.get(i).getCrNo().equalsIgnoreCase(booking_JsonArray.getJSONObject(k)
                                        .getString("PATCRNO"))){
                                    appointment.setDeptUnitName(booking_JsonArray.getJSONObject(k)
                                            .getString("DEPTUNITNAME"));
                                    appointment.setPatientQueueNo(booking_JsonArray.getJSONObject(k)
                                            .getString("QUEUENO"));
                                    appointment.setHospitalName(booking_JsonArray.getJSONObject(k)
                                            .getString("HOSPNAME"));
                                    appointment.setCurrentQueueNo(live_JsonArray.getJSONObject(j)
                                            .getInt("hrgnum_que_no")+"");
                                    appointment.setDoctorAvailable(live_JsonArray.getJSONObject(j)
                                            .getInt("per_person_waiting_time_mins")>0);
                                    appointmentList.add(appointment);
                                }
                            }

                        }
                        if(appointmentList.size()>0){
                            patientsBookingList.add(
                                    new Patient(patientsRecords.get(i).getName(),
                                            patientsRecords.get(i).getCrNo(),appointmentList));
                        }

                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (patientsBookingList.size()>0){
                                linearLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                appointmentPatientAdapter.notifyDataSetChanged();
                                noBookingView.setVisibility(View.GONE);
                            }
                            else {
                                linearLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                noBookingView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                catch (Exception e) {
                    Log.e("Live Error", "run: ",e);
                }
            }
        });

        thread.start();
    }
}