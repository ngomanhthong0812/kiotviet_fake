package com.example.kiotviet_fake.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.kiotviet_fake.fragments.FragmentAdminHangHoa;
import com.example.kiotviet_fake.fragments.FragmentAdminHoaDon;
import com.example.kiotviet_fake.fragments.FragmentAdminNhieuHon;
import com.example.kiotviet_fake.fragments.FragmentAdminThongBao;
import com.example.kiotviet_fake.fragments.FragmentAdminTongQuan;

public class ViewPagerAdminAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdminAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAdminTongQuan();
            case 1:
                return new FragmentAdminHoaDon();
            case 2:
                return new FragmentAdminHangHoa();
            case 3:
                return new FragmentAdminThongBao();
            case 4:
                return new FragmentAdminNhieuHon();
            default:
                return new FragmentAdminTongQuan();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
