package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.EndOfDayReportActivity;
import com.example.kiotviet_fake.activities.LoginActivity;
import com.example.kiotviet_fake.activities.MainActivity;

public class FragmentAdminNhieuHon extends Fragment {
    TextView txtNameAdmin, txtAvatar;
    LinearLayout btnDangXuat,btnBanHang,btnBaoCao;
    private View view;

    String shopName;
    int userId;
    String infoUserName;

    public FragmentAdminNhieuHon() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_fragment_admin_nhieu_hon, container, false);
        txtAvatar = view.findViewById(R.id.tv_avatar);
        txtNameAdmin = view.findViewById(R.id.txtNameAdmin);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        btnBanHang = view.findViewById(R.id.ln_banHang);
        btnBaoCao = view.findViewById(R.id.ln_baoCao);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        shopName = sharedPreferences.getString("shopName", "");
        userId = sharedPreferences.getInt("userId", 0);
        infoUserName = sharedPreferences.getString("infoUserName", "");

        updateUI();
        btnClick();
    }

    private void btnClick() {
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userId", 0);
                editor.putString("shopName", "");
                editor.putString("role", "");
                editor.apply();

                //gửi dữ liệu để lưu trữ tên đăng nhập và tên shop
                SharedPreferences sharedPreferences_infoUser = getActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_infoUser = sharedPreferences_infoUser.edit();
                editor_infoUser.putString("shopName", shopName);
                editor_infoUser.putString("infoUserName", infoUserName);
                editor_infoUser.apply();
            }
        });
        btnBanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EndOfDayReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String shopName = sharedPreferences.getString("shopName", "");
        String arrShopName[] = shopName.split("");

        txtNameAdmin.setText(shopName);
        txtAvatar.setText(arrShopName[0]);
    }

}