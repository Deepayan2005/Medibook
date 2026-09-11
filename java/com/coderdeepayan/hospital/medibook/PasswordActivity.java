package com.coderdeepayan.hospital.medibook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{
    String password = "";
    ImageView imageView1, imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,backSpaceButton;
    MaterialButton button1, button2, button3,button4,button5, button6, button7, button8,button9,button0;
    ImageView[] imageViews;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);


        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);

        imageView1 = findViewById(R.id.imageview_1);
        imageView2 = findViewById(R.id.imageview_2);
        imageView3 = findViewById(R.id.imageview_3);
        imageView4 = findViewById(R.id.imageview_4);
        imageView5 = findViewById(R.id.imageview_5);
        imageView6 = findViewById(R.id.imageview_6);
        imageView7 = findViewById(R.id.imageview_7);
        imageView8 = findViewById(R.id.imageview_8);
        backSpaceButton = findViewById(R.id.button_backspace);

        imageViews = new ImageView[]{imageView1, imageView2, imageView3, imageView4,
                                    imageView5, imageView6, imageView7, imageView8};

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);

        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length()>0){
                    password = password.substring(0,password.length()-1);
                    changeView();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        MaterialButton materialButton = findViewById(v.getId());
        if (password.length()<8){
            password=password.concat(materialButton.getText().toString().trim());
            if (password.length()==8) {
                if (new HospitalDatabase(PasswordActivity.this).matchPassword(password)){
                    startActivity(new Intent(PasswordActivity.this,HospitalActivity.class));
                    finish();
                }
            }
            changeView();
        }
        Log.d("Password", "onClick: "+password);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeView(){
        for (int i = 0; i <imageViews.length ; i++) {
            imageViews[i].setImageDrawable(getDrawable(R.drawable.empty_cicle));
        }
        for (int i = 0; i <password.length() ; i++) {
            imageViews[i].setImageDrawable(getDrawable(R.drawable.filled_cicle));
        }

    }
}