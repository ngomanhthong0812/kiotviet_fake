package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ChangeTableItemAdapter;
import com.example.kiotviet_fake.adapters.CombineTableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.SelectTablesService;
import com.example.kiotviet_fake.database.select.TableSelectByUserIdService;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.models.TableGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableListActivity extends AppCompatActivity {
    ImageView btnCancel;
    int isTableUserId;
    String nameTable;
    int idTable;
    int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);

        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("id_table", 0);
        orderId = intent.getIntExtra("orderId", 0);

        contronle();
        BtnOnClick();
        SelectTable();
    }

    private void SelectTable() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<TableGroup> arrayList = new ArrayList<>();

        //select data from api
        SelectTablesService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(SelectTablesService.class);
        Call<String> call = apiService.getTable(isTableUserId, "Mang về");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String user_id = jsonObject.getString("user_id");
                            int id = Integer.parseInt(jsonObject.getString("table_id"));
                            String tableName = jsonObject.getString("table_name");
                            int status = Integer.parseInt(jsonObject.getString("status"));
                            String table_price = jsonObject.getString("table_price");
                            int product_quantity = jsonObject.getInt("product_quantity");
                            String order_id = jsonObject.getString("order_id");
                            System.out.println("tesst" + order_id);
                            int userId = 0; // Giá trị mặc định nếu không thể chuyển đổi
                            if (user_id != null && !user_id.equals("null") && !user_id.isEmpty()) {
                                userId = Integer.parseInt(user_id);
                            }
                            if (userId == isTableUserId && status == 1) {

                                arrayList.add(new TableGroup(user_id,id, tableName, status,table_price,product_quantity,Integer.parseInt(order_id)));
                            }

                        }
                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        CombineTableAdapter combineTableAdapter = new CombineTableAdapter(arrayList, TableListActivity.this, idTable, orderId); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(combineTableAdapter);
                        combineTableAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
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

    private void BtnOnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableListActivity.this,SingleGraftActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void contronle() {
        btnCancel = (ImageView) findViewById(R.id.btnCancell);
    }
}