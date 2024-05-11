package com.example.kiotviet_fake.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.TableAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategoriesTatCa extends Fragment {
    private ProductAdapter productAdapter;
    private ArrayList<Product> arrayProducts = new ArrayList<>();
    RecyclerView recyclerView;

    public FragmentCategoriesTatCa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories_tat_ca, container, false);
        recyclerView = view.findViewById(R.id.recycler_view); // Đảm bảo RecyclerView đã được tìm thấy trong layout
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //select data from api
        ProductSelectService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(ProductSelectService.class);
        Call<String> call = apiService.getProducts();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Product> products = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("product_name");
                            float price = Float.parseFloat(jsonObject.getString("price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(price);

                            int quantity = jsonObject.getInt("quantity");

                            String idProductItem = id + "Tất Cả";
                            products.add(new Product(id, idProductItem, name, formattedPrice, quantity, 1, 0, null, 0));
                        }

                        arrayProducts.clear();
                        arrayProducts.addAll(products);

                        // Tạo adapter nếu chưa có và cập nhật dữ liệu mới
                        if (productAdapter != null) {
                            productAdapter.updateData(arrayProducts);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            productAdapter = new ProductAdapter(arrayProducts, requireContext(), null);
                            recyclerView.setAdapter(productAdapter);
                        }

//                        // Thêm đường phân chia giữa các mục trong RecyclerView
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
                        dividerItemDecoration.setDrawable(drawable);
                        recyclerView.addItemDecoration(dividerItemDecoration);
//
//                        // Tạo adapter nếu chưa có và cập nhật dữ liệu mới
//                        productAdapter = new ProductAdapter(arrayProducts, requireContext(), null);
//                        recyclerView.setAdapter(productAdapter);
//                        productAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý lỗi khi không nhận được phản hồi từ API
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi khi gọi API không thành công
            }
        });
    }

    // Phương thức thực hiện tìm kiếm sản phẩm
    public void performSearch(String keyword) {
        keyword = removeAccents(keyword.toLowerCase());
        ArrayList<Product> searchResult = new ArrayList<>();
        if (keyword.isEmpty()) {
            // Hiển thị tất cả sản phẩm khi keyword rỗng
            searchResult.addAll(arrayProducts);
        } else {
            // Tiến hành tìm kiếm khi keyword không rỗng
            for (Product product : arrayProducts) {
                String nameTable = removeAccents(product.getName().toLowerCase());
                if (nameTable.contains(keyword)) {
                    searchResult.add(product);
                }
            }
        }
        // Cập nhật dữ liệu cho adapter
        if (productAdapter != null) {
            productAdapter.updateData(searchResult);
        } else {
            // Khởi tạo adapter và đặt cho recyclerView
            productAdapter = new ProductAdapter(searchResult, requireContext(), null);
            recyclerView.setAdapter(productAdapter);
        }
    }

    private String removeAccents(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
