package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
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
    ListView listHistory ;
    HistoryAdapter historyAdapter;
    ArrayList<History> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_oders);
        LoadData();
        fetchData();
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

                            History history = new History(id, dateTime, dateTime_end, code, table_id, user_id, total_price);
                            historyList.add(history);
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
        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyList, this);
        listHistory.setAdapter(historyAdapter);
    }
}
