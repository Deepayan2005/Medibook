package com.coderdeepayan.hospital.medibook;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DiagnosticActivity extends AppCompatActivity {
    List<Patient> patientList;
    HospitalService hospitalService = new HospitalService();
    MaterialCardView accountButton;
    RecyclerView recyclerView;
    LinearLayout mainLayout,loadingLayout;
    TextView accountNameView,umidNumberView,noDiagnosticView;
    List<Diagnosis> diagnosisList = new ArrayList<>(),
    diagnosisList_filtered = new ArrayList<>();
    DiagnosticAdapter diagnosticAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic);

        accountButton = findViewById(R.id.accountButton2);
        recyclerView = findViewById(R.id.diagnosticRecyclerview);
        mainLayout = findViewById(R.id.main2);
        loadingLayout = findViewById(R.id.loadingLayout4);
        noDiagnosticView = findViewById(R.id.noDiagnosticView);
        accountNameView = findViewById(R.id.accountNameView2);
        umidNumberView = findViewById(R.id.accountUmidView2);

        patientList = new HospitalDatabase(this).getPatients();
        mainLayout.setVisibility(View.GONE);

        diagnosticAdapter = new DiagnosticAdapter(diagnosisList, new LabReportDownloadListener() {
            @Override
            public void downloadLabReport(int position) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hospitalService.getLabReportFile(diagnosisList_filtered.get(position));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DiagnosticActivity.this,
                                            "Lab report file downloaded.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (Exception e) {
                            Log.e("Download Lab Report", "run: ",e );
                        }
                        Thread.currentThread().interrupt();
                    }
                }).start();

            }
        }, new LabResultListener() {
            @Override
            public void viewLabReport(int position) {
                List<DiagnosticResult> diagnosticResultList = diagnosisList_filtered.get(position)
                        .getDiagnosticResultList();
                if (diagnosticResultList!=null){
                    showLabResultDialogBox(diagnosticResultList);
                }
                else {
                    Toast.makeText(DiagnosticActivity.this,
                            "No Result available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(diagnosticAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        diagnosticAdapter.notifyDataSetChanged();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <patientList.size() ; i++) {
                        diagnosisList.addAll(hospitalService.getLabTestByDoctor(patientList.get(i).getCrNo(),
                                patientList.get(i).getName()));
                    }
                }
                catch (Exception e) {
                    Log.e("Diagnostic", "run: ",e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAccountDialogBox();
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
    private void showLabResultDialogBox(List<DiagnosticResult> diagnosticResultList) {
        View view = LayoutInflater.from(this).inflate(R.layout.lab_result_layout,null,false);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        RecyclerView recyclerView2 = view.findViewById(R.id.lab_ResultRecyclerView);

        LabResultAdapter labResultAdapter = new LabResultAdapter(this,diagnosticResultList);
        recyclerView2.setAdapter(labResultAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        labResultAdapter.notifyDataSetChanged();
        alertDialog.show();

    }

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
                if (diagnosisList_filtered.isEmpty()){
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
                diagnosisList_filtered = new ArrayList<>(diagnosisList
                        .stream().filter(new Predicate<Diagnosis>() {
                            @Override
                            public boolean test(Diagnosis diagnosis) {
                                return patientList.get(position).getCrNo().equals(diagnosis.getCrNo());
                            }
                        }).toList());
                diagnosticAdapter.setDiagnosisList(diagnosisList_filtered);
                accountNameView.setText(patientList.get(position).getName());
                umidNumberView.setText(patientList.get(position).getUmid_id());
                mainLayout.setVisibility(View.VISIBLE);
                alertDialog.dismiss();

                if (diagnosisList_filtered.isEmpty()){
                    noDiagnosticView.setVisibility(View.VISIBLE);
                }
                else {
                    noDiagnosticView.setVisibility(View.GONE);
                }
            }
        });
        recyclerView2.setAdapter(accountAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        accountAdapter.notifyDataSetChanged();
        alertDialog.show();
    }
}