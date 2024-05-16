package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.BillAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Bills_Admin;
import com.example.kiotviet_fake.database.select.HistoryService;
import com.example.kiotviet_fake.models.Bill_Admin;
import com.example.kiotviet_fake.models.History;
import com.example.kiotviet_fake.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminHoaDon extends Fragment {
    private ArrayList<Bill_Admin> Bill_AdminArayList;
    private BillAdminAdapter billAdminAdapter;
    private RecyclerView recyclerView_bill;
    private TextView totalAmountTextView;
    public FragmentAdminHoaDon() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_admin_hoa_don, container, false);
        recyclerView_bill = view.findViewById(R.id.listView_bill);

        totalAmountTextView = view.findViewById(R.id.total_all_bills);

        Bill_AdminArayList = new ArrayList<>();
        billAdminAdapter = new BillAdminAdapter(Bill_AdminArayList, getContext());
        recyclerView_bill.setAdapter(billAdminAdapter);

        // Thiết lập LayoutManager cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_bill.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoadDataHoaDon(); // Di chuyển hàm gọi LoadDataHoaDon() sau khi adapter được thiết lập
        addDividerToRecyclerView();
    }

    private void addDividerToRecyclerView() {
        // Thêm dường viền vào RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_bill.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView_bill.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView_bill.addItemDecoration(dividerItemDecoration);
    }

    public void LoadDataHoaDon() {
        Bills_Admin billAdmin =  RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(Bills_Admin.class);
        Call<String> call = billAdmin.getBills_Admin();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        Bill_AdminArayList.clear();
                        double totalAmount = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id_bill = jsonObject.getInt("id_bill");

                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTimeEnd");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("tableId");
                            int user_id = jsonObject.getInt("userId");
                            double total_price = jsonObject.getDouble("total_price");
                            String name_user = jsonObject.getString("name_user");

                            totalAmount += total_price; // Cộng dồn giá tiền vào tổng giá tiền


                            Bill_Admin billsAdmin = new  Bill_Admin(id_bill, dateTime, dateTime_end, code, table_id, user_id, total_price, name_user);
                            Bill_AdminArayList.add(billsAdmin);

                        }
                        String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalAmount);
                        totalAmountTextView.setText(formattedTotalAmount + " VND");
                        // Cập nhật dữ liệu mới cho adapter
                        billAdminAdapter.notifyDataSetChanged();
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
}