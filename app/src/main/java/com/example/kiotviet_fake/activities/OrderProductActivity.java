package com.example.kiotviet_fake.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.OrderProductAdapter;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertApiClient;
import com.example.kiotviet_fake.database.insertOrderItems.OrderInsertItemsService;
import com.example.kiotviet_fake.database.insertOrders.OrderInsertService;
import com.example.kiotviet_fake.database.insertOrderItems.OrderInsertItemsApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusApiClient;
import com.example.kiotviet_fake.database.updateTableStatus.TableUpdateStatusService;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.session.SessionManager;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderProductActivity extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;

    TextView txtNameTable, btnThemVaoDon, bntChonLai;
    ImageView btnCancel;

    int idTable;
    String nameTable;
    int newOrderId;

    float tableTotalPrice;
    int isTableUserId;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        // lấy ra userId vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        isTableUserId = sharedPreferences.getInt("userId", 0);


        addControl();
        btnClick();
        updateUI();
    }

    private void updateUI() {
        Intent intent = getIntent();
        nameTable = intent.getStringExtra("nameTable");
        idTable = intent.getIntExtra("idTable", 0);
        txtNameTable.setText(nameTable);

    }

    private void addControl() {
        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        txtNameTable = (TextView) findViewById(R.id.txtNameTable);
        btnThemVaoDon = (TextView) findViewById(R.id.btnThemVaoDon);
        bntChonLai = (TextView) findViewById(R.id.bntChonLai);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FragmentManager manager = getSupportFragmentManager();
        OrderProductAdapter adapter = new OrderProductAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }

    public void btnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderProductActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnThemVaoDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // thêm hiệu ứng loading
                    progressBar.setVisibility(View.VISIBLE);

                    insertOrder("11168851", "60-dayfreetrial");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        bntChonLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.removeOrderAll();
                finish();
                startActivity(intent);
            }
        });

    }

    public void insertOrder(String username, String password) throws ParseException {
        Date currentDate = new Date();

        // Định dạng thời gian hiện tại thành chuỗi theo định dạng "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(currentDate);
        String code = generateRandomCode();
        int tableId = idTable;
        int userId = isTableUserId;

        OrderInsertService service = OrderInsertApiClient.createService(username, password);
        Call<String> call = service.insertOrder(formattedDateTime, code, tableId, userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // kiểm tra Retrofit đã hoàn thành
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        newOrderId = jsonObject.getInt("orderId");
                        Log.e("TAG", "thong: " + newOrderId);
                        insertOrder_items("11168851", "60-dayfreetrial");

                    } catch (JSONException e) {
                        e.printStackTrace();
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

    public void insertOrder_items(String username, String password) throws ParseException {

        SessionManager sessionManager = SessionManager.getInstance();
        ArrayList<Order> orders = sessionManager.getOrders();

        int numberOfOrders = orders.size(); // Số lượng đơn hàng cần thêm vào

        // Biến đếm số lượng Retrofit đã hoàn thành
        AtomicInteger retrofitCallCounter = new AtomicInteger(0);

        for (Order order : orders) {
            int quantity = order.getQuantity();
            String priceString = order.getPrice();
            priceString = priceString.replace(",", ""); // Loại bỏ dấu phẩy
            float price = Float.parseFloat(priceString) * order.getQuantity();
            int order_id = newOrderId;
            int product_id = order.getProductId();

            // tính tổng giá của bàn
            tableTotalPrice += price;


            OrderInsertItemsService service = OrderInsertItemsApiClient.createService(username, password);
            Call<String> call = service.insertOrderItem(quantity, price, order_id, product_id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        // Tăng biến đếm sau mỗi Retrofit thành công
                        int counter = retrofitCallCounter.incrementAndGet();

                        // Kiểm tra xem tất cả các cuộc gọi Retrofit đã hoàn thành chưa
                        if (counter == numberOfOrders) {

                            // Nếu tất cả các cuộc gọi Retrofit đã hoàn thành, chuyển màn hình mới
                            navigateToTableDetailActivity();
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

        //xoá tất cả product đã chọn khi nhấn thêm vào đơn
        sessionManager.removeOrderAll();
        updateStatusTable("11168851", "60-dayfreetrial");
    }

    private void updateStatusTable(String username, String password) {

        int id = idTable;
        double status = 1;
        float table_price = tableTotalPrice;

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

    private void navigateToTableDetailActivity() {
        Intent intent = new Intent(OrderProductActivity.this, TableDetailActivity.class);
        intent.putExtra("idTable", idTable);
        intent.putExtra("nameTable", nameTable);
        intent.putExtra("idOrder", newOrderId);
        startActivity(intent);

//        // ẩn hiệu ứng loading
//        progressBar.setVisibility(View.GONE);
    }


    public static String generateRandomCode() {
        Random rand = new Random();

        // Tạo ba số ngẫu nhiên từ 100 đến 999
        int num1 = rand.nextInt(900) + 100;
        int num2 = rand.nextInt(900) + 100;

        // Kết hợp các số và dấu "-" để tạo mã
        String code = num1 + "-" + num2;

        return code;
    }

}