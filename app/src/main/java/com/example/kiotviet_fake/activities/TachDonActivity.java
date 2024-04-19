package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.TachDonAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TachDonActivity extends AppCompatActivity implements AdapterListener {
    ImageView btnCancel;

    int idTable;
    String nameTable;
    float priceTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tach_don);

        addControl();
        btnClick();
        initView();
    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView() {
        //thêm dữ liệu vào sessionManager
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();
        ArrayList<Product> arrayList = new ArrayList<>();
        for (Bill bill : bills) {
            arrayList.add(new Product(bill.getProductId(), "", bill.getNameProduct(), bill.getNameProduct(), 200, bill.getQuantity(), bill.getTableId(), bill.getNameTable()));
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
        TachDonAdapter tachDonAdapter = new TachDonAdapter(arrayList, getApplicationContext(), TachDonActivity.this); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
        recyclerView.setAdapter(tachDonAdapter);
        tachDonAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

    }

    @Override
    public void onItemDeleted() {

    }

    @Override
    public void finishActivity() {

    }
}