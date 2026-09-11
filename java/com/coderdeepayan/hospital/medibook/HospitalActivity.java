package com.coderdeepayan.hospital.medibook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class HospitalActivity extends AppCompatActivity {
    HospitalAdapter hospitalAdapter;
    RecyclerView recyclerView;
    List<Hospital> copiedHospital = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        List<Hospital> hospitalList =new ArrayList<>();

        recyclerView = findViewById(R.id.hospitalRecyclerView);
        hospitalList = new ArrayList<>(LoadingActivity.hospitalList);
        hospitalList.add(0,new Hospital(Hospital.UTILITIES));

        hospitalAdapter = new HospitalAdapter(this, hospitalList,
                new SelectHospitalListener() {
                    @Override
                    public void selectHospital(Hospital hospital) {
                        Intent intent = new Intent(HospitalActivity.this, DepartmentActivity.class);
                        intent.putExtra("name", hospital.getName());
                        intent.putExtra("code", hospital.getCode());
                        startActivity(intent);
                    }
                },
                new SearchButtonListener() {
                    @Override
                    public void wantSearch() {
                        showSearchDialog(HospitalActivity.this);
                    }
                },
                new PrescriptionListener() {
                    @Override
                    public void seePrescription() {
                        startActivity(new Intent(HospitalActivity.this, PrescriptionActivity.class));
                    }
                },
                new QueueButtonListener() {
                    @Override
                    public void showQueue() {
                        startActivity(new Intent(HospitalActivity.this, QueueActivity.class));
                    }
                },
                new DiagnosticListener() {
                    @Override
                    public void viewLabTest() {
                        startActivity(new Intent(HospitalActivity.this, DiagnosticActivity.class));
                    }
                }
        );

        recyclerView.setAdapter(hospitalAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        hospitalAdapter.notifyDataSetChanged();

    }
    @SuppressLint({"MissingInflatedId","NewApi"})

    private void showSearchDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_view,null,false);
        AlertDialog alertDialog  = new AlertDialog.Builder(context).setView(view).create();

        RecyclerView recyclerView2 = view.findViewById(R.id.searchHospitalRecyclerView);
        TextInputEditText inputEditText = view.findViewById(R.id.searchHospitalInput);

        List<Hospital> hospitals2 = new ArrayList<>(LoadingActivity.hospitalList);

        HospitalAdapter hospitalAdapter2 = new HospitalAdapter(this, hospitals2,
                new SelectHospitalListener() {
                    @Override
                    public void selectHospital(Hospital hospital) {
                        Intent intent = new Intent(HospitalActivity.this, DepartmentActivity.class);
                        intent.putExtra("name",hospital.getName());
                        intent.putExtra("code", hospital.getCode());
                        startActivity(intent);
                    }
                },null,null,null,null);

        recyclerView2.setAdapter(hospitalAdapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));

        hospitalAdapter2.notifyDataSetChanged();

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Hospital> list3 = hospitals2.stream().filter(new Predicate<Hospital>() {
                    @Override
                    public boolean test(Hospital hospital) {
                        return hospital.getName().toLowerCase().contains(s.toString().toLowerCase());
                    }
                }).toList();
                hospitalAdapter2.setHospitalList(list3);
            }
        });


        alertDialog.show();

    }
}