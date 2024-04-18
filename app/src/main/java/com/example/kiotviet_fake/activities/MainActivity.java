package com.example.kiotviet_fake.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HomePagerAdapter;
import com.example.kiotviet_fake.adapters.NotificationPagerAdapter;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.fragments.FragmentSuDung;
import com.example.kiotviet_fake.fragments.FramentHome;
import com.example.kiotviet_fake.models.Table;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.lang.String;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView btnNotification, logo_kiotViet;
    SearchView searchView;

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    String shopName;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("NotificationActivity", "Activity is created");
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true); // Hiển thị biểu tượng mặc định
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.black)); // Đặt màu đen cho biểu tượng


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FramentHome()).commit();
        }

        // lấy ra tên shop vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        shopName = sharedPreferences.getString("shopName", "");
        userId = sharedPreferences.getInt("userId", 0);

        addControl();
        btnClick();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
//        MenuItem notificationItem = menu.findItem(R.id.action_notification);

//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnSearchClickListener(v -> {
//            notificationItem.setVisible(false);
//
//        });
//
//        searchView.setOnCloseListener(() -> {
//            notificationItem.setVisible(true);
//            return false;
//        });

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }

    private void addControl() {
        btnNotification = (ImageView) findViewById(R.id.btnNotification);
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
        if (item.getItemId() == R.id.DangXuat) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

            // reset userId về 0
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