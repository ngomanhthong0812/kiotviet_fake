package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.AdminProductUpdateAndInsertActivity;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.adapters.ProductAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.ProductSelectService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminHangHoa extends Fragment implements AdapterListener {
    private ProductAdminAdapter productAdminAdapter;
    private ArrayList<Product> arrayProducts = new ArrayList<>();
    RecyclerView recyclerView;
    private View view;
    int countProduct = 0;
    TextView txtCountProduct, txtTitle;
    ImageView btnClose, btnSearch, btnClear, btnThem;

    public FragmentAdminHangHoa() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_fragment_admin_hang_hoa, container, false);
        txtCountProduct = view.findViewById(R.id.txtCountProduct);
        txtTitle = view.findViewById(R.id.txtTitle);
        btnClose = view.findViewById(R.id.btnClose);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnThem = view.findViewById(R.id.btnThem);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        btnClick();
    }

    private void btnClick() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClear.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.VISIBLE);
                btnThem.setVisibility(View.VISIBLE);
                productAdminAdapter.closeSelection();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiển thị dialog xác nhận
                productAdminAdapter.clearSelection();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdminProductUpdateAndInsertActivity.class);
                intent.putExtra("checkFlat", "add");
                startActivity(intent);
            }
        });


    }

    public void initView() {
        SessionProducts sessionProducts = SessionProducts.getInstance();
        ArrayList<Product> listProducts = sessionProducts.getProductAll();

        recyclerView = view.findViewById(R.id.recycler_view); // Đảm bảo RecyclerView đã được tìm thấy trong layout
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Product> products = new ArrayList<>();
        for (Product listProduct : listProducts) {
            Product product = new Product(listProduct.getId(), listProduct.getIdProductItem(), listProduct.getName(), listProduct.getPrice(), listProduct.getQuantity(), listProduct.getQuantityOrder(), listProduct.getIdTable(), listProduct.getNameTable(), listProduct.getIdProduct(), listProduct.getNameCategories(), listProduct.getProduct_code());
            products.add(product);
            arrayProducts.add(product);
            countProduct++;
        }

        txtCountProduct.setText(countProduct + " hàng hoá");

        // Tạo adapter nếu chưa có và cập nhật dữ liệu mới
        if (productAdminAdapter != null) {
            productAdminAdapter.notifyDataSetChanged();
        } else {
            productAdminAdapter = new ProductAdminAdapter(products, requireContext(), FragmentAdminHangHoa.this);
            recyclerView.setAdapter(productAdminAdapter);
        }

        // Thêm đường phân chia giữa các mục trong RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemDeleted() {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void update_totalQuantity_totalPrice(int quantity, float priceTotal) {

    }

    @Override
    public void notification_insertOrder(int idTable, String nameTable) {

    }

    @Override
    public void notification_arrIdDeleteSize(ArrayList<Integer> arrIdDelete) {
        if (arrIdDelete.size() > 0) {
            btnClear.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.GONE);
            txtTitle.setVisibility(View.GONE);
            btnThem.setVisibility(View.GONE);
        }
    }
}