package com.coderdeepayan.hospital.medibook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PrescriptionActivity extends AppCompatActivity {
    PrescriptionAdapter prescriptionAdapter;
    RecyclerView recyclerView;
    List<Prescription> prescriptionList = new ArrayList<>(),prescriptionList_filtered = new ArrayList<>();
    HospitalService hospitalService = new HospitalService();
    MaterialCardView accountButton;
    LinearLayout mainLayout,loadingLayout;
    List<Patient> patientList;
    TextView accountNameView,umidNumberView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        mainLayout = findViewById(R.id.main2);
        loadingLayout = findViewById(R.id.loadingLayout3);
        accountButton = findViewById(R.id.accountButton);
        recyclerView = findViewById(R.id.prescriptionRecyclerview);
        accountNameView = findViewById(R.id.accountNameView);
        umidNumberView = findViewById(R.id.accountUmidView);

        patientList = new HospitalDatabase(this).getPatients();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                false));

        prescriptionAdapter = new PrescriptionAdapter(prescriptionList, new DownloadPrescriptionListener() {
            @Override
            public void downloadPrescription(int position) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hospitalService.getPatientPrescriptionFile(prescriptionList_filtered.get(position));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PrescriptionActivity.this,
                                            "Prescription saved !", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.currentThread().interrupt();
                        } catch (Exception e) {
                            Log.e("Prescription ", "run: ",e );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PrescriptionActivity.this,
                                            "Error Occurred. Please retry again !", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.currentThread().interrupt();
                        }
                    }
                }).start();
            }
        });
        recyclerView.setAdapter(prescriptionAdapter);

        prescriptionAdapter.notifyDataSetChanged();

        mainLayout.setVisibility(View.GONE);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <patientList.size() ; i++) {
                        prescriptionList.addAll(hospitalService.getPatientVisitData(patientList.get(i)));
                    }
                }
                catch (Exception e) {
                    Log.e("Prescription", "run: "+e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Size Prescription", "run = "+prescriptionList.size());
                        showAccountDialogBox();
                        mainLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }).start();

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountDialogBox();
            }
        });
    }
    @SuppressLint("MissingInflatedId")
    private void showAccountDialogBox() {

        View view = LayoutInflater.from(this).inflate(R.layout.account_layout,null,false);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        RecyclerView recyclerView2 = view.findViewById(R.id.account_recyclerView);
        ImageView imageView = view.findViewById(R.id.closeButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prescriptionList_filtered.isEmpty()){
                    finish();
                }
                else {
                    alertDialog.dismiss();
                }
            }
        });
        AccountAdapter accountAdapter = new AccountAdapter(this, patientList, new AccountListener() {
            @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            @Override
            public void selectAccount(int position) {
                Log.d("Patient", "selectAccount: "+patientList.get(position).getName());
                prescriptionList_filtered = new ArrayList<>(prescriptionList
                        .stream().filter(new Predicate<Prescription>() {
                            @Override
                            public boolean test(Prescription prescription) {
                                return patientList.get(position).getCrNo().equals(prescription.getCrno());
                            }
                        }).toList());
                prescriptionAdapter.setPrescriptionList(prescriptionList_filtered);
                accountNameView.setText(patientList.get(position).getName());
                umidNumberView.setText(patientList.get(position).getUmid_id());
                mainLayout.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });
        recyclerView2.setAdapter(accountAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,
                 LinearLayoutManager.VERTICAL,false));
        accountAdapter.notifyDataSetChanged();
        alertDialog.show();
    }
}