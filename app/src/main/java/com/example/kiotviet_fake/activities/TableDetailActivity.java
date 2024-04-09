package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.ProductAdapter;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteApiClient;
import com.example.kiotviet_fake.database.deleteOrder.OrderDeleteService;
import com.example.kiotviet_fake.database.deleteOrderItems.OrderDeleteItemsApiClient;
import com.example.kiotviet_fake.database.deleteOrderItems.OrderDeleteItemsService;
import com.example.kiotviet_fake.database.insertBillItems.BillsInsertItemsApiClient;
import com.example.kiotviet_fake.database.insertBillItems.BillsInsertItemsService;
import com.example.kiotviet_fake.database.insertBills.BillsInsertApiClient;
import com.example.kiotviet_fake.database.insertBills.BillsInsertService;
import com.example.kiotviet_fake.database.select.Orders_OrderItem_Product_SelectService;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Bill;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableDetailActivity extends AppCompatActivity implements AdapterListener {
    ImageView btnCancel, btnThem;
    TextView txtNameTable, txtCode, txtQuantity, txtTotalPrice;
    Button btnThanhToan, btnTamTinh, btnThongBao;

    int idTable;
    int newOrderId;
    String nameTable;
    int quantityTotal = 0;
    float priceTotal = 0;

    int idOrderByDelete;

    int newBillId;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_detail);

        addControl();
        updateUI();
        btnClick();
        initView();

    }

    public void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
        btnTamTinh = (Button) findViewById(R.id.btnTamTinh);
        btnThongBao = (Button) findViewById(R.id.btnThongBao);
        btnThem = (ImageView) findViewById(R.id.btnThem);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void updateUI() {
        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);

        txtNameTable.setText(nameTable);
    }

    public void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // thêm hiệu ứng loading
                progressBar.setVisibility(View.VISIBLE);

                insertBill("11168851", "60-dayfreetrial");
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTableOrderProductActivity();
            }
        });
    }


    public void initView() {
        //select data from api
        Orders_OrderItem_Product_SelectService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(Orders_OrderItem_Product_SelectService.class);
        Call<String> call = apiService.getOrders();

        //thêm dữ liệu vào sessionManager
        SessionManager sessionManager = SessionManager.getInstance();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        ArrayList<Product> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("id"));
                            int order_id = Integer.parseInt(jsonObject.getString("orderId"));
                            String code = jsonObject.getString("code");
                            String dateTime = jsonObject.getString("dateTime");
                            int table_id = Integer.parseInt(jsonObject.getString("table_id"));
                            int user_id = Integer.parseInt(jsonObject.getString("user_id"));
                            int product_id = Integer.parseInt(jsonObject.getString("product_id"));
                            String product_name = jsonObject.getString("name");
                            float price = Integer.parseInt(jsonObject.getString("price"));
                            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                            String formattedPrice = formatter.format(price);

                            float totalPrice = Integer.parseInt(jsonObject.getString("totalPrice"));
                            int quantity = Integer.parseInt(jsonObject.getString("quantity"));

                            if (idTable == table_id) {
                                arrayList.add(new Product(id, "", product_name, formattedPrice, 200, quantity, idTable, nameTable));
                                quantityTotal += quantity;
                                priceTotal += totalPrice;
                                txtCode.setText(code);
                                idOrderByDelete = id;
                                newOrderId = order_id;

                                // thêm vào kho lưu trữ bill
                                Bill bill = new Bill(dateTime, "demo", code, table_id, user_id, quantity, price * quantity, product_id);
                                sessionManager.addBill(bill);
                            }

                            NumberFormat formatterNumberFormat = NumberFormat.getInstance(Locale.getDefault());
                            String formatPrice = formatterNumberFormat.format(priceTotal);

                            txtQuantity.setText("Tổng tiền " + quantityTotal);
                            txtTotalPrice.setText(formatPrice);


                        }
                        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Sử dụng getView() để lấy view được inflate từ layout
                        recyclerView.setHasFixedSize(true);
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false); // Thay vì FragmentTatCa.this, sử dụng requireContext()
                        recyclerView.setLayoutManager(layoutManager);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider);
                        dividerItemDecoration.setDrawable(drawable);
                        recyclerView.addItemDecoration(dividerItemDecoration); // Thêm dường viền vào RecyclerView

                        // Tạo và thiết lập Adapter mới sau khi đã thêm dữ liệu từ API
                        ProductAdapter productAdapter = new ProductAdapter(arrayList, getApplicationContext(), TableDetailActivity.this); // Sử dụng requireContext() thay vì getContext() để đảm bảo không trả về null
                        recyclerView.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged(); // Thông báo cập nhật dữ liệu cho RecyclerView

                        updateTable("11168851", "60-dayfreetrial"); // cập nhật giá và trạng thái cảu bàn

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

    public void deleteOrder_items(String username, String password) throws ParseException {

        OrderDeleteItemsService service = OrderDeleteItemsApiClient.createService(username, password);
        Call<String> call = service.deleteOrderItem(idOrderByDelete);
        Log.e("TAG", "deleteOrder_items: " + idOrderByDelete);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // kiểm tra Retrofit đã hoàn thành
                if (response.isSuccessful()) {
                    try {
                        deleteOrder("11168851", "60-dayfreetrial");
                        isUpdateStatusTable("11168851", "60-dayfreetrial");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    public void deleteOrder(String username, String password) throws ParseException {

        OrderDeleteService service = OrderDeleteApiClient.createService(username, password);
        Call<String> call = service.deleteOrder(idOrderByDelete);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // kiểm tra Retrofit đã hoàn thành
                if (response.isSuccessful()) {
                    navigateToTableMainActivity();
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void isUpdateStatusTable(String username, String password) {

        int id = idTable;
        double status = 0;
        float table_price = 0;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void insertBill(String username, String password) {
        //lấy thời gian hiện tại
        Date currentDate = new Date();
        SimpleDateFormat formatterSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatterSimpleDateFormat.format(currentDate);

        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();
        Bill firstBill = bills.get(0);


        String txtTotal = txtTotalPrice.getText().toString();
        txtTotal = txtTotal.replace(",", "");

        BillsInsertService service = BillsInsertApiClient.createService(username, password);
        Call<String> call = service.insertBills(firstBill.getDateTime(), formattedDateTime, firstBill.getCode(), firstBill.getTableId(), firstBill.getUserId(), Float.parseFloat(txtTotal));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        newBillId = jsonObject.getInt("billId");
                        insertBillItems("11168851", "60-dayfreetrial");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void insertBillItems(String username, String password) {
        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Bill> bills = sessionManager.getBills();

        for (Bill bill : bills) {
            Log.e("TAG", "insertBill: " + bill);
            BillsInsertItemsService service = BillsInsertItemsApiClient.createService(username, password);
            Call<String> call = service.insertBillItems(bill.getQuantity(), bill.getTotalPrice(), bill.getProductId(), newBillId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try {
                            deleteOrder_items("11168851", "60-dayfreetrial");
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Xử lý phản hồi không thành công
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Xử lý lỗi
                }
            });
        }
    }

    private void navigateToTableMainActivity() {
        Intent intent = new Intent(TableDetailActivity.this, MainActivity.class);
        startActivity(intent);

        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.removeBillAll();
//        // ẩn hiệu ứng loading
//        progressBar.setVisibility(View.GONE);

    }

    private void navigateToTableOrderProductActivity() {
        Intent intent = new Intent(TableDetailActivity.this, OrderProductActivity.class);
        intent.putExtra("idTable", idTable);
        intent.putExtra("nameTable", nameTable);
        intent.putExtra("idOrder", newOrderId);

        String txtTotal = txtTotalPrice.getText().toString();
        txtTotal = txtTotal.replace(",", "");
        intent.putExtra("totalPriceTable", Integer.parseInt(txtTotal));

        startActivity(intent);
    }

    // nhận thông báo từ productAdapter khi item đã được xoá
    @Override
    public void onItemDeleted() {
        quantityTotal = 0;
        priceTotal = 0;
        initView();  // chạy lại initView
    }

    private void updateTable(String username, String password) {

        int id = idTable;
        double status = 1;
        float table_price = priceTotal;

        TableUpdateStatusService service = TableUpdateStatusApiClient.createService(username, password);
        Call<String> call = service.updateData(id, status, table_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                } else {
                    // Xử lý phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }
}