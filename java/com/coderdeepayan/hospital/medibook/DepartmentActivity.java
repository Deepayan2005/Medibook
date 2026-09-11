package com.coderdeepayan.hospital.medibook;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DepartmentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Department> departmentList;

    DepartmentAdapter departmentAdapter;
    LinearLayout loadingLayout, mainLayout;
    TextView textView;

    Thread thread;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread!=null){
            thread.interrupt();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        recyclerView=findViewById(R.id.departmentRecyclerView);
        loadingLayout=findViewById(R.id.loadingLayout);
        mainLayout = findViewById(R.id.mainLayout);
        textView=findViewById(R.id.hospitalNameView2);

        String name = getIntent().getStringExtra("name"),code = getIntent().getStringExtra("code");
        textView.setText(name);
        mainLayout.setVisibility(View.GONE);

        departmentAdapter = new DepartmentAdapter(this, departmentList, new DepartmentListener() {
            @Override
            public void selectDepartment(int position) {
                showBookingBottomSheet(departmentList.get(position),code);
            }
        });
        recyclerView.setAdapter(departmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        departmentAdapter.notifyDataSetChanged();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int call = 1;
                    while (departmentList == null) {
                        departmentList = new HospitalService().getDoctorsList(code);
                        Log.d("Calling", "Called for time " + call);
                        call++;
                    }
                    Log.d("Number of Departments", ""+departmentList.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (thread!=null){
                                thread.interrupt();
                            }
                            departmentAdapter.setDepartmentList(departmentList);
                            mainLayout.setVisibility(View.VISIBLE);
                            loadingLayout.setVisibility(View.GONE);
                        }
                    });

                } catch (Exception e) {

                    Log.e("Departments", "run: ",e );
                }

            }
        });
        thread.start();

    }
    @SuppressLint({"MissingInflatedId", "ResourceType", "NotifyDataSetChanged"})
    private void showBookingBottomSheet(Department department, String hospitalCode) {
        View view = LayoutInflater.from(this).inflate(R.layout.booking_sheet,null,false);


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);


        TextView textView2 = view.findViewById(R.id.unitNameView2),
                doctorNameView=view.findViewById(R.id.doctorNameView2);
        MaterialCardView materialContainer = view.findViewById(R.id.materialContainer1);
        RecyclerView recyclerView2 = view.findViewById(R.id.patientsRecyclerView),
                queueRecyclerView = view.findViewById(R.id.queueRecyclerView1);
        MaterialButton materialButton = view.findViewById(R.id.bookingButton),
                okayButton = view.findViewById(R.id.okayButon);
        LinearLayout bookingLayout = view.findViewById(R.id.bookingControlsLayout),
                        confirmationLayout = view.findViewById(R.id.confirmationLayout);
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);

        bookingLayout.setVisibility(View.VISIBLE);
        confirmationLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        List<Patient> patientList = new HospitalDatabase(this).getPatients();
        List<Boolean> booleanList = new ArrayList<>();
        for (int i = 0; i <patientList.size() ; i++) {
            booleanList.add(false);
        }

        PatientAdapter patientAdapter = new PatientAdapter(this, patientList,booleanList);
        recyclerView2.setAdapter(patientAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        patientAdapter.notifyDataSetChanged();

        if (department.getDoctorName().trim().length()>0){
            materialContainer.setVisibility(View.VISIBLE);
        }
        else {
            materialContainer.setVisibility(View.GONE);
        }

        textView2.setText(department.getOpdName());
        doctorNameView.setText(department.getDoctorName());

        List<Queue> queueList = new ArrayList<>();
        QueueAdapter queueAdapter = new QueueAdapter(this,queueList);
        queueRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        queueRecyclerView.setAdapter(queueAdapter);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booleanList.contains(true)){
                    bookingLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    Thread bookingThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HospitalService hospitalService = new HospitalService();
                                for (int i = 0; i <booleanList.size() ; i++) {
                                    if (booleanList.get(i)){
                                        String response = hospitalService
                                                .bookAppointment(patientList.get(i),department,hospitalCode);
                                        if (response!=null){
                                            queueList.add(new Queue(patientList.get(i).getName(),
                                                    department.getOpdName(),response));
                                        }
                                        else {
                                            Thread.currentThread().interrupt();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(DepartmentActivity.this,
                                                            "Department not working today!!!", Toast.LENGTH_SHORT).show();
                                                    bottomSheetDialog.dismiss();
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        confirmationLayout.setVisibility(View.VISIBLE);
                                        queueAdapter.notifyDataSetChanged();
                                    }
                                });
                            } catch (Exception e) {
                                Log.e("Booking", "",e );
                            }
                        }
                    });
                    bookingThread.start();
                }
                else {
                    Toast.makeText(DepartmentActivity.this,
                            "At least select any patient for appointment.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        bottomSheetDialog.show();

    }
}