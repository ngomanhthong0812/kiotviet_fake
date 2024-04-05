package com.example.kiotviet_fake.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.UserService;
import com.example.kiotviet_fake.database.inserUser.UserInsert;
import com.example.kiotviet_fake.database.inserUser.UserInsertAPIClient;
import com.example.kiotviet_fake.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ImageView GoBack;
    EditText tenGianHang, tenDangNhap, matKhau;
    Button btnDangKy;
    String checkNameShop, checkUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Đăng ký");
        GoBack = (ImageView) findViewById(R.id.GoBack);
        tenGianHang = (EditText) findViewById(R.id.TenGianHang);
        tenDangNhap = (EditText) findViewById(R.id.TenDangNhap);
        matKhau = (EditText) findViewById(R.id.MatKhau);
        btnDangKy = (Button) findViewById(R.id.buttonDangKy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String tenGianHangText = tenGianHang.getText().toString().trim();
        String tenDangNhapText = tenDangNhap.getText().toString().trim();
        String matKhauText = matKhau.getText().toString().trim();

        if (!tenGianHangText.isEmpty() && !tenDangNhapText.isEmpty() && !matKhauText.isEmpty()) {
            UserInsert service = UserInsertAPIClient.createService("11168851", "60-dayfreetrial");
            Call<String> call = service.insertUser(tenGianHangText, tenDangNhapText, matKhauText);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        // Đăng ký thành công, chuyển hướng đến màn hình đăng nhập
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Xử lý lỗi khi đăng ký không thành công
                        Toast.makeText(RegisterActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Xử lý lỗi khi gọi API thất bại
                    Toast.makeText(RegisterActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Hiển thị thông báo cho người dùng nếu có trường nào đó không được điền
            Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUser() {
        UserService apiService = RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(UserService.class);
        Call<String> call = apiService.getUsers();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        boolean isNameShopExist = false;
                        boolean isUserNameExist = false;
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObject = jsonArray.getJSONObject(i);
                            String nameShop = userObject.getString("name");
                            String userName = userObject.getString("user_name");

                            // Kiểm tra xem tên gian hàng đã tồn tại chưa
                            if (nameShop.equals(tenGianHang.getText().toString().trim())) {
                                isNameShopExist = true;
                            }

                            // Kiểm tra xem tên tài khoản đã tồn tại chưa
                            if (userName.equals(tenDangNhap.getText().toString().trim())) {
                                isUserNameExist = true;
                            }
                        }

                        // Kiểm tra kết quả
                        if (isNameShopExist || isUserNameExist) {
                            Toast.makeText(RegisterActivity.this, "Tên gian hàng hoặc tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            // Tiến hành đăng ký
                            registerUser();
                        }

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
    }
}
