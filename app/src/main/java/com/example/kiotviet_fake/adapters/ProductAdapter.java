package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.LoginActivity;
import com.example.kiotviet_fake.activities.MainActivity;
import com.example.kiotviet_fake.activities.ProductDetailActivity;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
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
        SessionManager sessionManager = SessionManager.getInstance();
        if (product.getQuantityOrder() == 0) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isExpanded[0]) {
                        holder.countQuanity.setVisibility(View.GONE);
                        holder.imgCheck.setVisibility(View.GONE);
                        // gán số lượng 0 khi ko chọn vào sản phẩm
                        product.setQuantityOrder(0);
                    } else {
                        holder.countQuanity.setVisibility(View.VISIBLE);
                        holder.imgCheck.setVisibility(View.VISIBLE);
                        // gán số lượng 1 khi chọn vào sản phẩm
                        product.setQuantityOrder(1);
                    }
                    // Sau khi thay đổi trạng thái, cập nhật biến boolean
                    isExpanded[0] = !isExpanded[0];

                    if (!isExpanded[0]) {
                        // Nếu trạng thái là false, xóa đơn hàng
                        SessionManager sessionManager = SessionManager.getInstance();
                        sessionManager.removeOrderByProductId(product.getId());
                    } else {
                        // Nếu trạng thái là true, thêm đơn hàng mới
                        Order order1 = new Order(product.getQuantityOrder(), product.getPrice(), 1, product.getId());
                        sessionManager.addOrder(order1);

                    }
                }

            });

        }

        final int[] count = {1};
        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txtCount.setText(String.valueOf(count[0] += 1));
                product.setQuantityOrder(count[0]);
//                holder.txtQuantity.setText(String.valueOf(count[0]));
                sessionManager.updateQuantityProduct(product.getId(), count[0]);
            }
        });

        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txtCount.setText(String.valueOf(count[0] -= 1));
                product.setQuantityOrder(count[0]);
//                holder.txtQuantity.setText(String.valueOf(count[0]));
                sessionManager.updateQuantityProduct(product.getId(), count[0]);
            }
        });

        holder.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_more) {
                            // Xử lý khi người dùng chọn sửa
                            Intent intent = new Intent(context, ProductDetailActivity.class);

                            intent.putExtra("product_id", product.getId());
                            intent.putExtra("product_name", product.getName());
                            intent.putExtra("product_price", product.getPrice());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent); // Sử dụng context để khởi chạy Intent
                        }
                        if (item.getItemId() == R.id.action_delete) {
                            // Xử lý khi người dùng chọn sửa
                            Log.e("TAG", "onMenuItemClick: " + product.getId());
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
    }

    public int getItemCount() {
        return products.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity, btnTang, btnGiam, txtCount;
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
            btnTang = (TextView) itemView.findViewById(R.id.btnTang);
            btnGiam = (TextView) itemView.findViewById(R.id.btnGiam);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);

        }
    }


}
