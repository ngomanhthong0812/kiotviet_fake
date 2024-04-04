package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.models.Table;
import com.example.kiotviet_fake.session.SessionManager;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {
    ArrayList<Product> products;
    Context context;


    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = products.get(position);

//        Log.d("ProductClicked", "Id: " + product.getId() + ",Product: " + product.getName() + ",Quantity: " + product.getName() + ", Price: " + product.getPrice());


        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.valueOf(product.getPrice()));
        holder.txtQuantity.setText(String.valueOf(product.getQuantityOrder()));
        if (product.getQuantityOrder() == 0) {
            holder.txtQuantity.setVisibility(View.GONE);
            holder.imgSelect.setVisibility(View.GONE);
        } else {
            holder.txtQuantity.setVisibility(View.VISIBLE);
            holder.imgSelect.setVisibility(View.VISIBLE);
        }

        final boolean[] isExpanded = {false};
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getQuantityOrder() == 0) {
                    if (isExpanded[0]) {
                        holder.countQuanity.setVisibility(View.GONE);
                        holder.imgCheck.setVisibility(View.GONE);
                        SessionManager.getInstance().setCount(SessionManager.getInstance().getCount() - 1);
                    } else {
                        holder.countQuanity.setVisibility(View.VISIBLE);
                        holder.imgCheck.setVisibility(View.VISIBLE);
                        SessionManager.getInstance().setCount(SessionManager.getInstance().getCount() + 1);
                    }
                    // Sau khi thay đổi trạng thái, cập nhật biến boolean
                    isExpanded[0] = !isExpanded[0];

                }

            }
        });
    }

    public int getItemCount() {
        return products.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity;
        ImageView imgSelect, imgCheck;
        LinearLayout countQuanity;
        RelativeLayout item;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);
            imgSelect = (ImageView) itemView.findViewById(R.id.imgSelect);
            imgCheck = (ImageView) itemView.findViewById(R.id.imgCheck);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            countQuanity = (LinearLayout) itemView.findViewById(R.id.countQuanity);

        }
    }


}
