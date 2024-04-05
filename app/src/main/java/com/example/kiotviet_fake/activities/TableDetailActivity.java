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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.OrderProductAdapter;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.CategoriesService;
import com.example.kiotviet_fake.database.OrdersService;
import com.example.kiotviet_fake.database.ProductService;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableDetailActivity extends AppCompatActivity {
    ImageView btnCancel;
    TextView txtNameTable, txtCode, txtQuantity,txtTotalPrice;

    int idTable;
    int quantityTotal = 0;
    float priceTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_detail);

        addControl();
        updateUI();
        btnClick();
        initView();
    }

    public void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);

    }

    private void updateUI() {
        Intent intent = getIntent();
        String nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);

        txtNameTable.setText(nameTable);
    }

    public void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void initView() {
        //select data from api
        OrdersService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(OrdersService.class);
        Call<String> call = apiService.getOrders();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Product> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String code = jsonObject.getString("code");
                            String dateTime = jsonObject.getString("dateTime");
                            int table_id = Integer.parseInt(jsonObject.getString("table_id"));
                            int user_id = Integer.parseInt(jsonObject.getString("user_id"));
                            int product_id = Integer.parseInt(jsonObject.getString("product_id"));
                            String product_name = jsonObject.getString("name");
                            float price = Integer.parseInt(jsonObject.getString("price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(price);

                            float totalPrice = Integer.parseInt(jsonObject.getString("totalPrice"));
                            int quantity = Integer.parseInt(jsonObject.getString("quantity"));

                            if (idTable == table_id) {
                                arrayList.add(new Product(product_id, product_name, formattedPrice, 200, quantity));
                                quantityTotal += quantity;
                                priceTotal += totalPrice;
                                txtCode.setText(code);
                            }

                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(priceTotal);

                            txtQuantity.setText("Tổng tiền " + quantityTotal);
                            txtTotalPrice.setText(formatPrice);


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
                        ProductAdapter productAdapter = new ProductAdapter(arrayList, getApplicationContext()); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("TAG", "Failed to fetch data: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });
    }
}