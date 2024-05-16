package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;

public class AdminProductDetailActivity extends AppCompatActivity {
    TextView txtNameProduct, txtMaHang, txtLoaiHang, txtGiaBan;
    ImageView btnCancel, btnUpdate, btnDelete;

    int id;
    String name, categories_name, price, product_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);


        addControl();
        updateUI();
        btnClick();

    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductDetailActivity.this, AdminProductUpdateAndInsertActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("product_code", product_code);
                intent.putExtra("name", name);
                intent.putExtra("categories_name", categories_name);
                intent.putExtra("price", price);
                intent.putExtra("checkFlat", "update");
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminProductDetailActivity.this, "đang cập nhật", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        product_code = intent.getStringExtra("product_code");
        name = intent.getStringExtra("name");
        categories_name = intent.getStringExtra("categories_name");
        price = intent.getStringExtra("price");

        txtMaHang.setText(product_code);
        txtNameProduct.setText(name);
        txtLoaiHang.setText(categories_name);
        txtGiaBan.setText(price);
    }

    private void addControl() {
        txtNameProduct = (TextView) findViewById(R.id.tv_nameProduct);
        txtMaHang = (TextView) findViewById(R.id.tv_maHang);
        txtLoaiHang = (TextView) findViewById(R.id.tv_loaiHang);
        txtGiaBan = (TextView) findViewById(R.id.tv_giaBan);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnUpdate = (ImageView) findViewById(R.id.btnUpdate);
        btnDelete = (ImageView) findViewById(R.id.btnDelete);
    }
}