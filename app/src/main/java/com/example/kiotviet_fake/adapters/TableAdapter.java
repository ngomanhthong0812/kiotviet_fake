package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.TableDetailActivity;
import com.example.kiotviet_fake.models.Table;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.viewHolder> {
    ArrayList<Table> tables;
    Context context;

    public TableAdapter(ArrayList<Table> tables, Context context) {
        this.tables = tables;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_table, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Table table = tables.get(position);
        holder.txtSoBan.setText(table.getTenBan());
        holder.txtThoiGian.setText(table.getThoiGian());
        String gia = table.getGia();
        if (gia != null) {
            holder.txtGia.setText(gia);
        } else {
            holder.txtGia.setText(""); // hoặc một giá trị mặc định khác thích hợp
        }

        if (table.getGia() != null && table.getThoiGian() != null) {
            // Đặt màu nền và border màu xanh
            holder.item.setBackgroundResource(R.drawable.bg_item_1);
        } else {
            // Đặt màu nền và border màu khác (ví dụ: màu trắng)
            holder.item.setBackgroundResource(R.drawable.bg_item);
        }

        if (table.getTenBan().toLowerCase().contains("mang")) {
            holder.imgCart.setVisibility(View.VISIBLE); // Hiển thị biểu tượng giỏ hàng
        } else {
            holder.imgCart.setVisibility(View.GONE); // Ẩn biểu tượng giỏ hàng
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TableDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return tables.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtSoBan, txtThoiGian, txtGia;
        LinearLayout item;
        ImageView imgCart;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            item = (LinearLayout) itemView.findViewById(R.id.item);
            txtSoBan = (TextView) itemView.findViewById(R.id.txtSoBan);
            txtThoiGian = (TextView) itemView.findViewById(R.id.txtThoiGian);
            txtGia = (TextView) itemView.findViewById(R.id.txtGia);
            imgCart = (ImageView) itemView.findViewById(R.id.imgCart);
        }
    }


}
