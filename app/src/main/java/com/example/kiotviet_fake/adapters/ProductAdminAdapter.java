package com.example.kiotviet_fake.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.AdminProductDetailActivity;
import com.example.kiotviet_fake.activities.ProductDetailActivity;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.interface_main.AdapterListener;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {
    ArrayList<Product> products;
    Context context;
    private AdapterListener adapterListener;
    private ArrayList<Boolean> selectedItems; // Danh sách để theo dõi trạng thái đã chọn của từng mục
    private ArrayList<Integer> arrIdDelete;

    public ProductAdminAdapter(ArrayList<Product> products, Context context, AdapterListener adapterListener) {
        this.products = products;
        this.context = context;
        this.adapterListener = adapterListener;
        selectedItems = new ArrayList<>(Collections.nCopies(products.size(), false)); // Khởi tạo danh sách với giá trị mặc định là false
        arrIdDelete = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_product_admin, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.valueOf(product.getPrice()));
        holder.txtMaSanPham.setText(String.valueOf(product.getProduct_code()));

        // Lấy trạng thái đã chọn của mục tại vị trí position
        boolean isSelected = selectedItems.get(position);
        int productId = product.getId();

        // Đặt lại trạng thái của các View để tránh việc tái chế View không mong muốn
        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(isSelected);
        holder.check.setVisibility(arrIdDelete.size() != 0 ? View.VISIBLE : View.GONE);
        holder.image.setVisibility(arrIdDelete.size() != 0 ? View.GONE : View.VISIBLE);

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (arrIdDelete.size() == 0) {
                    // Đảm bảo rằng chỉ có mục được nhấn giữ là có CheckBox được đánh dấu true
                    selectedItems.set(holder.getAdapterPosition(), true);
                    arrIdDelete.clear();
                    arrIdDelete.add(productId);

                    adapterListener.notification_arrIdDeleteSize(arrIdDelete);
                    notifyDataSetChanged();
                }
                return true;
            }
        });
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.check.isChecked()) {
                    if (!arrIdDelete.contains(productId)) {
                        arrIdDelete.add(productId);
                    }
                } else {
                    arrIdDelete.remove(Integer.valueOf(productId));
                }
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminProductDetailActivity.class);
                // thêm max sản pẩm vào product data
                intent.putExtra("id",productId);
                intent.putExtra("product_code",product.getProduct_code());
                intent.putExtra("name", product.getName());
                intent.putExtra("categories_name", product.getNameCategories());
                intent.putExtra("categories_id", product.getIdCategories());
                intent.putExtra("price", product.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void closeSelection() {
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedItems.set(i, false);
        }
//        Log.d("TAG", "clearSelection: " + arrIdDelete.toString());
        arrIdDelete.clear();

        notifyDataSetChanged();
    }

    public void clearSelection() {
        // thực hiện xoá các sản phẩm và chạy lại closeSelection() để reset
        Log.d("TAG", "clearSelection: " + "đã xoá các sản phẩm" + arrIdDelete.toString());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtCount, txtMaSanPham;
        ImageView image;
        RelativeLayout item;
        CheckBox check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            item = itemView.findViewById(R.id.item);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtMaSanPham = itemView.findViewById(R.id.txtMaSanPham);
            check = itemView.findViewById(R.id.check);
            image = itemView.findViewById(R.id.image);
        }
    }
}
