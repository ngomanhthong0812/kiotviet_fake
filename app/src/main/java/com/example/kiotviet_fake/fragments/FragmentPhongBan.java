package com.example.kiotviet_fake.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kiotviet_fake.R;

public class FragmentPhongBan extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phong_ban_end_of_day_report, container, false);

        return view;
    }
//    public void ganNoiDung(String str){
//        txtFragA.setText(str.toString());
//    }

}
