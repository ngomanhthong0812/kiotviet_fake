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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        float total = Float.valueOf((float) history.getTotal_price());

        // Định dạng mới cho ngày
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Định dạng mới cho giờ
        SimpleDateFormat newTimeFormat = new SimpleDateFormat("HH:mm");

        holder.totalPriceTextView.setText(formatPrice(total));
        holder.txtCode.setText(history.getCode());
        holder.nameTable.setText(history.getNameTable());

        // Chuyển đổi chuỗi ngày thành đối tượng Date
        try {
            // Đối tượng Date từ chuỗi datetime
            Date datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(history.getDateTime_end());

            // Định dạng ngày mới sử dụng đối tượng newDateFormat
            String formattedDate = newDateFormat.format(datetime);
            holder.dateDay.setText(formattedDate);

            // Định dạng giờ mới sử dụng đối tượng newTimeFormat
            String formattedTime = newTimeFormat.format(datetime);
            holder.timeDay.setText(formattedTime);
            holder.itemChidell.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String formatPrice(float price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price);
    }

    private String getFormattedTime(String datetime) {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newTimeFormat = new SimpleDateFormat("HH:mm");

        try {
            Date date = oldDateFormat.parse(datetime);
            return newTimeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Trả về chuỗi rỗng nếu có lỗi xảy ra
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView totalPriceTextView;
        TextView txtCode, dateDay, timeDay,nameTable,itemChidell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            totalPriceTextView = itemView.findViewById(R.id.totalHistory);
            txtCode = itemView.findViewById(R.id.txtCode);
            dateDay = itemView.findViewById(R.id.dateDay);
            timeDay = itemView.findViewById(R.id.timeDay);
            itemChidell = itemView.findViewById(R.id.itemChidell);
            nameTable = itemView.findViewById(R.id.nameTable);
        }
    }
}
