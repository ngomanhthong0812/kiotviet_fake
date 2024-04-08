package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.BillsSelectService;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndOfDayReportActivity extends AppCompatActivity {
    ImageView btnCancel;
    TextView txtTongDoanhThu, txtDoanhThu, txtDoanhThuThuan, txtDoanhThuThuan_1, txtSoHoaDon, txtDoanhThuTBD,txtDate;
    LinearLayout btnSetDate;

    int totalRevenueToday = 0;
    int countBills = 0;
    String selectedDate;
    int isUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_day_report);

        // lấy id tài khoang đang login
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isUserId = sharedPreferences.getInt("userId", 0);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(currentDate);

        addControl();

        //set date (UI) ngày hiện tại
        txtDate.setText(selectedDate);

        btnClick();
        initView();

    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtTongDoanhThu = (TextView) findViewById(R.id.txtTongDoanhThu);
        txtDoanhThu = (TextView) findViewById(R.id.txtDoanhThu);
        txtDoanhThuThuan = (TextView) findViewById(R.id.txtDoanhThuThuan);
        txtDoanhThuThuan_1 = (TextView) findViewById(R.id.txtDoanhThuThuan_1);
        txtSoHoaDon = (TextView) findViewById(R.id.txtSoHoaDon);
        txtDoanhThuTBD = (TextView) findViewById(R.id.txtDoanhThuTBD);
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnSetDate = (LinearLayout) findViewById(R.id.btnSetDate);
    }

    private void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndOfDayReportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetDate();
            }
        });
    }

    public void openSetDate() {
        Calendar calendar = Calendar.getInstance();

        // Lấy ngày, tháng và năm hiện tại
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EndOfDayReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Hiển thị ngày đã chọn
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                calendar.set(year, month, dayOfMonth);
                selectedDate = dateFormat.format(calendar.getTime());

                //set date
                txtDate.setText(selectedDate);

                //reset về 0
                totalRevenueToday = 0;
                countBills = 0;

                // chạy lại initView
                initView();
            }
        }, currentYear, currentMonth, currentDay);

        // Đặt giới hạn cho DatePickerDialog, không cho phép chọn ngày trước ngày hiện tại
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Mở hộp thoại chọn ngày
        datePickerDialog.show();
    }


    public void initView() {
        //select data from api
        BillsSelectService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(BillsSelectService.class);
        Call<String> call = apiService.getBills();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTimeEnd");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("tableId");
                            int user_id = jsonObject.getInt("userId");
                            int total_price = Integer.parseInt(jsonObject.getString("total_price"));

                            String[] parts = dateTime_end.split(" ");
                            if (parts[0].equals(selectedDate) && user_id == isUserId) {
                                totalRevenueToday += total_price;
                                countBills++;
                            }
                            Log.e("TAG", "onResponse: "+selectedDate );
                        }
                        updateUI();
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

    public void updateUI() {
        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        String formattedPrice = formatter.format(totalRevenueToday);

        String trungBinhDon = "";
        if (countBills <= 0) {
            trungBinhDon = "0";
        } else {
            trungBinhDon = formatter.format(totalRevenueToday / countBills);
        }

        txtTongDoanhThu.setText(formattedPrice);
        txtDoanhThu.setText(formattedPrice);
        txtDoanhThuThuan.setText(formattedPrice);
        txtDoanhThuThuan_1.setText(formattedPrice);
        txtSoHoaDon.setText(String.valueOf(countBills));
        txtDoanhThuTBD.setText(trungBinhDon);
    }
}