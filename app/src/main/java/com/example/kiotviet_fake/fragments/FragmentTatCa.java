package com.example.kiotviet_fake.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.TableSelectService;
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

public class FragmentTatCa extends Fragment {
    private ArrayList<Table> tableList = new ArrayList<>();

    public FragmentTatCa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tat_ca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Table> arrayList = new ArrayList<>();

        //select data from api
        TableSelectService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(TableSelectService.class);
        Call<String> call = apiService.getTable();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("id"));
                            String tableName = jsonObject.getString("table_name");
                            int status = Integer.parseInt(jsonObject.getString("status"));
                            float  table_price = Float.parseFloat(jsonObject.getString("table_price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(table_price);


                            String userIdString = jsonObject.getString("user_id");
                            int userId = 0; // Giá trị mặc định nếu không thể chuyển đổi
                            if (userIdString != null && !userIdString.equals("null") && !userIdString.isEmpty()) {
                                userId = Integer.parseInt(userIdString);
                            }
                            arrayList.add(new Table(id, tableName, status, userId,formattedPrice));


                        }
                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        TableAdapter tableAdapter = new TableAdapter(arrayList, requireContext()); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(tableAdapter);
                        tableAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView
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
