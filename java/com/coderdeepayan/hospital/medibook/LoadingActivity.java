package com.coderdeepayan.hospital.medibook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class LoadingActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    HospitalService hospitalService = new HospitalService();
    static List<Hospital> hospitalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        linearLayout = findViewById(R.id.linearLayout1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HospitalDatabase hospitalDatabase = new HospitalDatabase(LoadingActivity.this);
                    hospitalList=hospitalService.getHospitalList();

                    if (hospitalDatabase.hasUserDetails()){
                        startActivity(new Intent(LoadingActivity.this, PasswordActivity.class));
                        finish();
                        Thread.currentThread().interrupt();

                    }
                    else {
                        startActivity(new Intent(LoadingActivity.this, CreateAccountActivity.class));
                        finish();
                        Thread.currentThread().interrupt();
                    }

                } catch (Exception e) {
                    Log.e("Hospitals", "run: "+e);
                }

            }
        }).start();



    }
}