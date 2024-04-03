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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HomePagerAdapter;
import com.example.kiotviet_fake.adapters.NotificationPagerAdapter;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.fragments.FragmentSuDung;
import com.example.kiotviet_fake.fragments.FramentHome;
import com.example.kiotviet_fake.models.Table;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView btnNotification,logo_kiotViet;
    SearchView searchView;

    private DrawerLayout drawerLayout;
    Toolbar toolbar;

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

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.PhongBan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FramentHome()).commit();
        }
        if (item.getItemId() == R.id.DongBo) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FramentHome()).commit();
        }
        if (item.getItemId() == R.id.DangXuat) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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