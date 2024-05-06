package com.example.kiotviet_fake.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.HomePagerAdapter;
import com.example.kiotviet_fake.adapters.OrderProductAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentCategoriesOrder extends Fragment {
    private ViewPager pager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControl();
    }

    private void addControl() {
        pager = (ViewPager) getView().findViewById(R.id.view_pager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tab_layout);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        OrderProductAdapter adapter = new OrderProductAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }

}
