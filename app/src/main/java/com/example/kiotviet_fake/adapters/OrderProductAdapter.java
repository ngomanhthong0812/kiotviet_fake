package com.example.kiotviet_fake.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.kiotviet_fake.fragments.FragmentCategoriesCafe;
import com.example.kiotviet_fake.fragments.FragmentCategoriesDaXay;
import com.example.kiotviet_fake.fragments.FragmentCategoriesMonThem;
import com.example.kiotviet_fake.fragments.FragmentCategoriesNuocDongChai;
import com.example.kiotviet_fake.fragments.FragmentCategoriesNuocEp;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSinhTo;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSodaY;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSua;
import com.example.kiotviet_fake.fragments.FragmentCategoriesSuaChua;
import com.example.kiotviet_fake.fragments.FragmentCategoriesTatCa;
import com.example.kiotviet_fake.fragments.FragmentCategoriesThuocLa;

import java.lang.String;

public class OrderProductAdapter extends FragmentStatePagerAdapter {

    public OrderProductAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new FragmentCategoriesTatCa();
                break;
            case 1:
                frag = new FragmentCategoriesCafe();
                break;
            case 2:
                frag = new FragmentCategoriesDaXay();
                break;
            case 3:
                frag = new FragmentCategoriesNuocEp();
                break;
            case 4:
                frag = new FragmentCategoriesNuocDongChai();
                break;
            case 5:
                frag = new FragmentCategoriesSinhTo();
                break;
            case 6:
                frag = new FragmentCategoriesSodaY();
                break;
            case 7:
                frag = new FragmentCategoriesSuaChua();
                break;
            case 8:
                frag = new FragmentCategoriesSua();
                break;
            case 9:
                frag = new FragmentCategoriesThuocLa();
                break;
            case 10:
                frag = new FragmentCategoriesMonThem();
                break;


        }
        return frag;
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Tất cả";
                break;
            case 1:
                title = "Cà phê";
                break;
            case 2:
                title = "Đá xay";
                break;
            case 3:
                title = "Nước ép";
                break;
            case 4:
                title = "Nước đóng chai";
                break;
            case 5:
                title = "Sinh tố";
                break;
            case 6:
                title = "Soda ý";
                break;
            case 7:
                title = "Sữa chua";
                break;
            case 8:
                title = "Sữa";
                break;
            case 9:
                title = "Thuốc lá";
                break;
            case 10:
                title = "Món thêm";
                break;
        }
        return title;
    }
}