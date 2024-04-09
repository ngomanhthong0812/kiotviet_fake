package com.example.kiotviet_fake.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;
import com.example.kiotviet_fake.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView DangKyTaiKhoan;
    EditText userName, password, nameRestaurant;
    Button btnDangNhap;
    String usersName, userspassword, restaurant_name, shop_id, user_id, role;
    ArrayList<Users> userList = new ArrayList<>();

    int userId;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DangKyTaiKhoan = findViewById(R.id.DangKy);
        userName = findViewById(R.id.TenDangNhap);
        password = findViewById(R.id.MatKhau);
        nameRestaurant = findViewById(R.id.TenGianHang);
        btnDangNhap = findViewById(R.id.DangNhap);

        UserService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.getUsers();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObject = jsonArray.getJSONObject(i);
                            shop_id = userObject.getString("shop_id");
                            user_id = userObject.getString("user_id");
                            restaurant_name = userObject.getString("name");
                            usersName = userObject.getString("user_name");
                            userspassword = userObject.getString("password");
                            role = userObject.getString("role");
                            Users user = new Users(shop_id, user_id, restaurant_name, usersName, userspassword, role);
                            userList.add(user);
                        }

                        // Sau khi đã thêm dữ liệu vào userList, thực hiện kiểm tra đăng nhập
                        performLogin();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "Failed to fetch data: " + t.getMessage());
            }
        });

        DangKyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUserName = userName.getText().toString();
                String inputPassword = password.getText().toString();
                String inputRestaurant = nameRestaurant.getText().toString();
                boolean isValidUser = false;
                for (Users user : userList) {
                    System.out.println("user list test name " + user.getUser_name());
                    System.out.println("user list test pass " + user.getPassword());
                    System.out.println("user list test restau " + user.getShop_name());
                    if (user.getUser_name().equals(inputUserName) && user.getPassword().equals(inputPassword) && user.getShop_name().equals(inputRestaurant)) {
                        isValidUser = true;
                        userId = Integer.parseInt(user.getUser_id());
                        shopName = user.getShop_name();
                        break;
                    }
                }

                if (isValidUser) {
                    // gửi userId khi đăng nhập thành công
                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", userId);
                    editor.putString("shopName", shopName);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // lấy ra tên shop vừa dc truyền khi login thành công
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", 0);
        if (userId == 0) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
}
