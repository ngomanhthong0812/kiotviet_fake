package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;

import java.util.ArrayList;

public class TableDetailActivity extends AppCompatActivity {
    ImageView btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_detail);
        initView();
        addControl();
        btnClick();
    }
    public void btnClick(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addControl(){
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
    }

    public void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true); //tối ưu hóa dữ liệu, ko bị ảnh hưởng bởi nd trong adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        ArrayList<Product> arrayList = new ArrayList<>();
        arrayList.add(new Product("BÀN 4", "18,000", "1"));
        arrayList.add(new Product("BÀN 3", "19,000", "2"));
        arrayList.add(new Product("BÀN 3", "18,000", "2"));
        arrayList.add(new Product("BÀN 3", "18,000", "2"));
        arrayList.add(new Product("BÀN 3", "18,000", "2"));
        arrayList.add(new Product("BÀN 3", "18,000", "2"));
        arrayList.add(new Product("BÀN 3", "18,000", "2"));
        arrayList.add(new Product("BÀN 3", "18,000", "2"));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ProductAdapter productAdapter = new ProductAdapter(arrayList, getApplicationContext());
        recyclerView.setAdapter(productAdapter);
    }
}