package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.NotificationPagerAdapter;
import com.example.kiotviet_fake.adapters.OrderProductAdapter;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.session.SessionManager;
import com.google.android.material.tabs.TabLayout;

public class OrderProductActivity extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;

    TextView txtNameTable, btnThemVaoDon;
    ImageView btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);


        addControl();
        btnClick();
        updateUI();

    }

    private void updateUI() {
        Intent intent = getIntent();
        String nameTable = intent.getStringExtra("nameTable");
        txtNameTable.setText(nameTable);
    }

    private void addControl() {
        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        btnThemVaoDon = (TextView) findViewById(R.id.btnThemVaoDon);

        FragmentManager manager = getSupportFragmentManager();
        OrderProductAdapter adapter = new OrderProductAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }

    public void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderProductActivity.this, MainActivity.class);
                SessionManager.getInstance().setCount(0);
                startActivity(intent);
            }
        });
    }

}