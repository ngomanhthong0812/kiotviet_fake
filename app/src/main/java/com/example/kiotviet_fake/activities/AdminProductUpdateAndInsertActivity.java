package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;

public class AdminProductUpdateAndInsertActivity extends AppCompatActivity {
    EditText txtNameProduct, txtMaHang, txtLoaiHang, txtGiaBan;
    TextView txtTitle, btnLuu;
    ImageView btnCancel, setImage;

    int id;
    String name, categories_name, product_code, price, checkFlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update_and_insert);

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
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkFlat) {
                    case "update":
                        Toast.makeText(AdminProductUpdateAndInsertActivity.this, "luu những thay doi lên data by id || maHang", Toast.LENGTH_LONG).show();
                        break;
                    case "add":
                        Toast.makeText(AdminProductUpdateAndInsertActivity.this, "add", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(AdminProductUpdateAndInsertActivity.this, "not key", Toast.LENGTH_LONG).show();
                }
            }
        });
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminProductUpdateAndInsertActivity.this, "Chức năng đang được cập nhật", Toast.LENGTH_LONG).show();
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
        checkFlat = intent.getStringExtra("checkFlat");

        switch (checkFlat) {
            case "update":
                txtTitle.setText("Sửa hàng hoá");
                break;
            case "add":
                txtTitle.setText("Thêm hàng hoá");
                break;
            default:
                Toast.makeText(AdminProductUpdateAndInsertActivity.this, "not key", Toast.LENGTH_LONG).show();
        }
        txtMaHang.setText(product_code);
        txtNameProduct.setText(name);
        txtLoaiHang.setText(categories_name);
        txtGiaBan.setText(price);
    }

    private void addControl() {
        txtNameProduct = (EditText) findViewById(R.id.et_tenHang);
        txtMaHang = (EditText) findViewById(R.id.et_maHang);
        txtLoaiHang = (EditText) findViewById(R.id.et_loaiHang);
        txtGiaBan = (EditText) findViewById(R.id.et_giaBan);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        setImage = (ImageView) findViewById(R.id.setImage);
        btnLuu = (TextView) findViewById(R.id.btn_luu);
    }
}