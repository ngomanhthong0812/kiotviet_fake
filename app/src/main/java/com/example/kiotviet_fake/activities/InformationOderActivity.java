package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kiotviet_fake.R;

public class InformationOderActivity extends AppCompatActivity {
    ImageView btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_oder);
        addControl();
        BtnOnclick();
    }

    private void BtnOnclick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
    }
}