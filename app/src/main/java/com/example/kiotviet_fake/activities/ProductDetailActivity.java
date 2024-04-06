package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiotviet_fake.R;

public class ProductDetailActivity extends AppCompatActivity {
    TextView nameProduct,txtPrice,idPr;
    EditText edtGiamGia,edtGiaBan;
    ImageView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        nameProduct = (TextView) findViewById(R.id.txtName);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        idPr =(TextView) findViewById(R.id.idProduct);
        edtGiamGia =(EditText) findViewById(R.id.edtGiamGia);
        edtGiaBan = (EditText) findViewById(R.id.EdtPrice);
        goBack = (ImageView) findViewById(R.id.btnCancel);


        LoadData();
        GoBack();
    }

    private void GoBack() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadData() {
        Intent intent = getIntent();
        int productId = intent.getIntExtra("product_id", 0);
        String productName = intent.getStringExtra("product_name");
        String productPrice = intent.getStringExtra("product_price");

        nameProduct.setText(productName);
        txtPrice.setText(productPrice);
        idPr.setText(String.valueOf(productId));
        edtGiaBan.setText(productPrice);
    }
}