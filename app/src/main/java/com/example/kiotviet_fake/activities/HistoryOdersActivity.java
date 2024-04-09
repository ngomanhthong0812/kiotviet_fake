package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HistoryAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.HistoryService;
import com.example.kiotviet_fake.models.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryOdersActivity extends AppCompatActivity {
    RecyclerView listHistory;
    HistoryAdapter historyAdapter;
    ArrayList<History> historyList;
    ImageView btnClose;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_oders);
        LoadData();
        BtnClick();
        fetchData();
    }

    private void BtnClick() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryOdersActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchData() {
        HistoryService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(HistoryService.class);
        Call<String> call = apiService.getHistory();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        historyList.clear(); // Xóa dữ liệu cũ trước khi cập nhật dữ liệu mới
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTime_end");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("table_id");
                            int user_id = jsonObject.getInt("user_id");
                            double total_price = jsonObject.getDouble("total_price");
                            String nameTable = jsonObject.getString("table_name");

                            if (user_id == userId) {
                                History history = new History(id, dateTime, dateTime_end, code, table_id, user_id, total_price,nameTable);
                                historyList.add(history);
                            }
                        }
                        // Cập nhật dữ liệu mới cho adapter
                        historyAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công (ví dụ: mã lỗi không phải 200)
                    Toast.makeText(HistoryOdersActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình tải dữ liệu
                Toast.makeText(HistoryOdersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadData() {
        listHistory = findViewById(R.id.listHistory);
        btnClose = findViewById(R.id.btnClose);

        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", 0);

        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyList, this);
        listHistory.setLayoutManager(new LinearLayoutManager(this));
        listHistory.setAdapter(historyAdapter);
    }
}
