package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.models.History;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    @RequiresApi(api = Build.VERSION_CODES.S)
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


        holder.itemChidell.setText(history.getDateTime() + " - " + history.getDateTime_end());

        // Lấy thời gian bắt đầu và kết thúc từ đối tượng history
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(history.getDateTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(history.getDateTime_end(), formatter);

        // Tính khoảng thời gian giữa hai thời điểm
        Duration duration = Duration.between(startTime, endTime);

        // Lấy số giờ, phút và giây từ khoảng thời gian
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        // Hiển thị thời gian
        String totalTime = hours + "g" + minutes + "p";
        // Hiển thị chuỗi thời gian đã tính được
        holder.totalTime.setText(totalTime);

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
        TextView txtCode, totalTime, nameTable, itemChidell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            totalPriceTextView = itemView.findViewById(R.id.totalHistory);
            txtCode = itemView.findViewById(R.id.txtCode);
            totalTime = itemView.findViewById(R.id.totalTime);
            itemChidell = itemView.findViewById(R.id.itemChidell);
            nameTable = itemView.findViewById(R.id.nameTable);
        }
    }
}
