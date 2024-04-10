package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.DetailBillAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Detail_item;
import com.example.kiotviet_fake.models.DetailBill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBillActivity extends AppCompatActivity {
    RecyclerView listItemBill;
    DetailBillAdapter detailBillAdapter;
    ArrayList<DetailBill> listItemProduct;
    TextView tong1,tong2,tong3,title;
    ImageView gobackk;
    int id_bill;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        LoadData();
        fetchData();
        BtnOnClick();

    }

    private void BtnOnClick() {
      gobackk.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
    }

    private void fetchData() {
        Detail_item apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(Detail_item.class);
        Call<String> call =  apiService.getDetail_item();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        listItemProduct.clear(); // Xóa dữ liệu cũ trước khi cập nhật dữ liệu mới
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int bill_item_id = jsonObject.getInt("bill_item_id");
                            int quantity = jsonObject.getInt("quantity");
                            double total_price = jsonObject.getDouble("total_price");
                            int product_id = jsonObject.getInt("product_id");
                            int bill_id = jsonObject.getInt("bill_id");
                            String product_name = jsonObject.getString("product_name");

                            if (bill_id == id_bill) {
                                DetailBill detailBill = new DetailBill(bill_item_id, quantity, total_price, product_id, bill_id, product_name);
                                listItemProduct.add(detailBill);
                                System.out.println("test " + bill_id);
                            }
                        }
                        // Cập nhật dữ liệu mới cho adapter
                        detailBillAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công (ví dụ: mã lỗi không phải 200)
                    Toast.makeText(DetailBillActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }



    private void LoadData() {
        Intent intent = getIntent();
        id_bill = intent.getIntExtra("bill_id", 0);
        total = intent.getDoubleExtra("total", 0);


        tong1 = findViewById(R.id.TongTien1);
        tong2 = findViewById(R.id.TongTien2);
        tong3 = findViewById(R.id.TongTien3);
        gobackk = findViewById(R.id.goback);
        title = findViewById(R.id.textView7);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotal = decimalFormat.format(total);

        tong1.setText(formattedTotal);
        tong2.setText(formattedTotal);
        tong3.setText(formattedTotal);
        title.setText(String.valueOf(id_bill));

        listItemProduct = new ArrayList<DetailBill>();
        detailBillAdapter = new DetailBillAdapter(listItemProduct, this);
        listItemBill = findViewById(R.id.ListItemBill);
        listItemBill.setAdapter(detailBillAdapter);

    }
}