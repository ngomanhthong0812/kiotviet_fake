package com.example.kiotviet_fake.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.fragments.FragmentTatCa;
import com.example.kiotviet_fake.fragments.FramentHome;
import com.example.kiotviet_fake.models.Table;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView btnNotification;

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    String shopName;
    int userId;
    private LinearLayout headerA, inputSearch;
    private ImageView btnSearch, btnClose;
    private EditText searchEditText;

     FragmentTatCa fragmentTatCa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("NotificationActivity", "Activity is created");
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.black));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FramentHome()).commit();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        shopName = sharedPreferences.getString("shopName", "");
        userId = sharedPreferences.getInt("userId", 0);


        // Initialize fragmentTatCa
        fragmentTatCa = new FragmentTatCa();

        // Thêm fragment vào FragmentManager
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentTatCa, "fragment_tat_ca")
                .commit();

        addControl();
        btnClick();
        search();

    }

    private void search() {
        headerA = findViewById(R.id.header_A);
        inputSearch = findViewById(R.id.inputSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnClose = findViewById(R.id.btnClose);
        searchEditText = findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setVisibility(View.VISIBLE);
                headerA.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setVisibility(View.GONE);
                headerA.setVisibility(View.VISIBLE);
                searchEditText.setText("");
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString();
                System.out.println("ERRRR MainActivity: " + keyword);
                 fragmentTatCa = (FragmentTatCa) getSupportFragmentManager().findFragmentByTag("fragment_tat_ca");
                if (fragmentTatCa != null) {
                    fragmentTatCa.performSearch(keyword);
                } else {
                    Log.e("ERRRR MainActivity:", "FragmentTatCa is null");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void addControl() {
        btnNotification = findViewById(R.id.btnNotification);
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView txtNameShop = headerView.findViewById(R.id.txtNameShop);
        txtNameShop.setText(shopName);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.PhongBan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FramentHome()).commit();
        }
        if (item.getItemId() == R.id.DongBo) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FramentHome()).commit();
        }
        if (item.getItemId() == R.id.BaoCaoCuoiNgay) {
            Intent intent = new Intent(this, EndOfDayReportActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.history) {
            Intent intent = new Intent(this, HistoryOdersActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.DangXuat) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", 0);
            editor.putString("shopName", shopName);
            editor.apply();
        }
        if (item.getItemId() == R.id.ThongBaoBep) {
            Intent intent = new Intent(MainActivity.this, Notification.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void btnClick() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });
    }
}
