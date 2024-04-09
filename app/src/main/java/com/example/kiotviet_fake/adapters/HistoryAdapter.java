package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.models.History;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<History> historyList;
    private Context context;

    public HistoryAdapter(ArrayList<History> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_oder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.totalPriceTextView.setText(String.valueOf(history.getTotal_price()));

        // Thêm các thành phần giao diện khác nếu cần
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView totalPriceTextView;

        // Thêm các thành phần giao diện khác nếu cần

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            totalPriceTextView = itemView.findViewById(R.id.totalHistory);
            // Khởi tạo các thành phần giao diện khác nếu cần
        }
    }
}
