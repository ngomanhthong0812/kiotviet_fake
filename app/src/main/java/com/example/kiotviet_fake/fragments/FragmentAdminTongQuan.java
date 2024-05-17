package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Bills_Admin;
import com.example.kiotviet_fake.database.select.GetRevenue;
import com.example.kiotviet_fake.models.Bill_Admin;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminTongQuan extends Fragment {
    TextView totalRevenue;
    TextView totalBill;
    String id_shop, id,year,Month ;
    Double Total_Revenue;
    List<BarEntry> entries = new ArrayList<>();
    public FragmentAdminTongQuan() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_admin_tong_quan, container, false);

        totalRevenue = view.findViewById(R.id.totalRevenue);
        totalBill = view.findViewById(R.id.totalBill);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id_shop = sharedPreferences.getString("shop_id","");
        System.out.println("test id_shop : "+ id_shop);

        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CountQuantityAndTotalBills();
        GetRevenue();
        setupBarChart(view);
    }

    private void GetRevenue() {
        GetRevenue getRevenue =  RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(GetRevenue.class);

        Call<String> call = getRevenue.GetRevenue_Admin(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                             id = jsonObject.getString("id");
                             year = jsonObject.getString("year");
                             Month = jsonObject.getString("Month");
                            Total_Revenue = jsonObject.getDouble("TotalRevenue");

                            System.out.println( "totalRevenue1 " +  id );
                            System.out.println( "totalRevenue1 " +  year);
                            System.out.println( "totalRevenue1 " +   Month );
                            System.out.println( "totalRevenue1 " + Total_Revenue);

                            // Tạo một BarEntry với tháng làm trục X và tổng giá tiền làm trục Y

                            int monthValue = Integer.parseInt(Month);
                            float Revenue = Total_Revenue   .floatValue();
                            entries.add(new BarEntry(monthValue, Revenue));
                        }
                        setupBarChart(getView());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void CountQuantityAndTotalBills() {
        Bills_Admin billAdmin =  RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(Bills_Admin.class);

        Call<String> call = billAdmin.getBills_Admin(id_shop);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        double totalAmount = 0;
                        int countBill = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            double total_price = jsonObject.getDouble("total_price");
                             totalAmount += total_price;
                            countBill++;
                        }
                        String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalAmount);
                        totalRevenue.setText(formattedTotalAmount + " VND");
                        String count_Bill = NumberFormat.getNumberInstance(Locale.getDefault()).format(countBill);
                        totalBill.setText(count_Bill + " hóa đơn");
                        // Cập nhật dữ liệu mới cho adapter

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void setupBarChart(View view) {
        BarChart barChart = view.findViewById(R.id.barChart); // Thay đổi này


        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo tháng"); // Tên của dữ liệu


        // Thiết lập kích thước chữ
        dataSet.setValueTextSize(12f); // Kích thước chữ là 12px

        // Tạo dữ liệu cần hiển thị
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(0.5f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        // Cấu hình trục Y
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false); // Vô hiệu hóa trục Y bên phải
        yAxisLeft.setAxisMinimum(0f); // Giá trị tối thiểu của trục Y
        yAxisLeft.setLabelCount(4, true); // Đặt số lượng nhãn trục Y và tự động chỉnh sửa giá trị
        // Thiết lập kích thước chữ cho trục Y
        yAxisLeft.setTextSize(12f); // Kích thước chữ trên trục Y là 12px
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Sử dụng hàm formatCurrency để định dạng giá trị số
                return formatCurrency(value);
            }
        });

        // Tùy chỉnh màu sắc
        dataSet.setColor(Color.BLUE); // Màu của cột
        dataSet.setValueTextColor(Color.BLACK); // Màu của giá trị

        // Kiểm tra số lượng cột, nếu ít hơn hoặc bằng 2, giảm kích thước cột và khoảng cách giữa cột
        if (dataSet.getEntryCount() <= 3) {
            barData.setBarWidth(0.2f); // Đặt độ rộng cột
            barChart.getXAxis().setGranularity(1f); // Đặt độ rộng khoảng giữa các cột
        }
        // Vô hiệu hóa chú thích mô tả
        barChart.getDescription().setEnabled(false);

        // Cập nhật BarChart
        barChart.invalidate();

    }
    private String formatCurrency(float value) {
        // Làm tròn giá trị số
        long roundedValue = Math.round(value);

        // Nếu giá trị là hàng triệu trở lên, hiển thị dưới dạng "x tr"
        if (roundedValue >= 1000000) {
            return String.format("%d tr", roundedValue / 1000000);
        } else {
            // Nếu giá trị nhỏ hơn hàng triệu, hiển thị giá trị định dạng tiền tệ
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            return numberFormat.format(roundedValue);
        }
    }
}