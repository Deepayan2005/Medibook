package com.coderdeepayan.hospital.medibook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {
    HospitalService hospitalService = new HospitalService();
    HospitalDatabase hospitalDatabase;
    TextInputEditText phoneNumberInput,inputPassword, inputConfirmPassword;
    MaterialButton materialButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        hospitalDatabase=new HospitalDatabase(this);
        phoneNumberInput=findViewById(R.id.inputPhoneNumber);
        inputPassword=findViewById(R.id.inputPassword);
        inputConfirmPassword=findViewById(R.id.inputConfirmPassword);
        materialButton=findViewById(R.id.submitButton);
        progressBar=findViewById(R.id.progressBar1);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberInput.getText().toString().length()==10 && inputPassword.getText().toString().trim()
                        .equalsIgnoreCase(inputConfirmPassword.getText().toString().trim()) &&
                inputPassword.getText().toString().trim().length()==8 &&
                        inputConfirmPassword.getText().toString().trim().length()==8){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        phoneNumberInput.setEnabled(false);
                                        inputPassword.setEnabled(false);
                                        inputConfirmPassword.setEnabled(false);
                                        materialButton.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                });
                                List<Patient> patients= hospitalService.getUMID_data(
                                        phoneNumberInput.getText().toString().trim());
                                if (patients!=null){
                                    hospitalDatabase.savePatients(patients);
                                    hospitalDatabase.savePassword(inputConfirmPassword.getText().toString().trim());
                                    startActivity(new Intent(CreateAccountActivity.this,
                                            PasswordActivity.class));
                                    finish();
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CreateAccountActivity.this,
                                                    "No user found with this phone number.", Toast.LENGTH_SHORT).show();
                                            phoneNumberInput.setEnabled(true);
                                            inputPassword.setEnabled(true);
                                            inputConfirmPassword.setEnabled(true);
                                            materialButton.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }


                            } catch (Exception e) {
                                Log.e("Account Creation", e.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        phoneNumberInput.setEnabled(true);
                                        inputPassword.setEnabled(true);
                                        inputConfirmPassword.setEnabled(true);
                                        materialButton.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }).start();

                } else if (phoneNumberInput.getText().toString().trim().length()!=10) {
                    Toast.makeText(CreateAccountActivity.this,
                            "Phone Number must be 10-digits.", Toast.LENGTH_SHORT).show();
                } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                    Toast.makeText(CreateAccountActivity.this,
                            "Both Password must be same.", Toast.LENGTH_SHORT).show();
                }
                else if (inputPassword.getText().toString().length()<8 ||
                        inputConfirmPassword.getText().toString().length()<8){
                    Toast.makeText(CreateAccountActivity.this,
                            "Both passwords must be 8-digits.", Toast.LENGTH_SHORT).show();
                }


            }
        });







    }
}