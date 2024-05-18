package com.example.kiotviet_fake.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.adapters.BillAdminAdapter;
import com.example.kiotviet_fake.database.RetrofitClient;
import com.example.kiotviet_fake.database.select.Bills_Admin;
import com.example.kiotviet_fake.database.select.HistoryService;
import com.example.kiotviet_fake.models.Bill_Admin;
import com.example.kiotviet_fake.models.History;
import com.example.kiotviet_fake.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminHoaDon extends Fragment {
    private ArrayList<Bill_Admin> Bill_AdminArayList;
    private BillAdminAdapter billAdminAdapter;
    private RecyclerView recyclerView_bill;
    private TextView totalAmountTextView,dayMonthYearr,dayMonthYearr2;
    LinearLayout xemTheoLich;
    String id_shop;

    public FragmentAdminHoaDon() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_admin_hoa_don, container, false);
        recyclerView_bill = view.findViewById(R.id.listView_bill);

        totalAmountTextView = view.findViewById(R.id.total_all_bills);

        xemTheoLich = view.findViewById(R.id.xemTheoLich);
        dayMonthYearr =view.findViewById(R.id.dayMonthYearr);
        dayMonthYearr2 = view.findViewById(R.id.dayMonthYearr2);

        Bill_AdminArayList = new ArrayList<>();
        billAdminAdapter = new BillAdminAdapter(Bill_AdminArayList, getContext());
        recyclerView_bill.setAdapter(billAdminAdapter);

        // Thiết lập LayoutManager cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_bill.setLayoutManager(layoutManager);


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id_shop = sharedPreferences.getString("shop_id","");
        System.out.println("test id_shop : "+ id_shop);

        xemTheoLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoadDataHoaDon(0,0,0); // Di chuyển hàm gọi LoadDataHoaDon() sau khi adapter được thiết lập
        addDividerToRecyclerView();

    }
    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn kiểu xem");

        String[] options = {"Xem theo ngày", "Xem từ ngày đến ngày", "Xem tất cả bill"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDayPickerDialog();
                        break;
                    case 1:
//                        showDayToDayPickerDialog();
                        break;
                    case 2:
                        dayMonthYearr.setText("Xem tất cả");
                        dayMonthYearr2.setText("Xem tất cả");
                        LoadDataHoaDon(0,0,0);

                        break;
                }
            }




        });

        builder.show();
    }
    private void showDayPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Không cần thực hiện gì ở đây vì bạn chỉ muốn chọn tháng và năm
                        dayMonthYearr.setText(String.format("%02d/%02d/%d", dayOfMonth,month + 1, year));
                        dayMonthYearr2.setText(String.format("%02d/%02d/%d", dayOfMonth,month + 1, year));
                        LoadDataHoaDon(year,  month + 1, dayOfMonth);

                    }
                }, year, month, 1); // 1: ngày mặc định sẽ hiển thị, có thể là bất kỳ ngày nào vì nó sẽ bị ẩn đi

        // Ẩn bảng chọn ngày và hiển thị spinner cho tháng và năm
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.show();
    }
    private void addDividerToRecyclerView() {
        // Thêm dường viền vào RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_bill.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(recyclerView_bill.getContext(), R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView_bill.addItemDecoration(dividerItemDecoration);
    }

    public void LoadDataHoaDon(int year, int month, int dayOfMonth) {
        Bills_Admin billAdmin =  RetrofitClient.getRetrofitInstance("11168851", "60-dayfreetrial").create(Bills_Admin.class);

        Call<String> call = billAdmin.getBills_Admin(id_shop);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        Bill_AdminArayList.clear();
                        double totalAmount = 0;
                        // Định dạng của chuỗi datetime
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy");
                        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MM");
                        SimpleDateFormat outputDatFormat = new SimpleDateFormat("dd");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id_bill = jsonObject.getInt("id_bill");

                            String dateTime = jsonObject.getString("dateTime");
                            String dateTime_end = jsonObject.getString("dateTimeEnd");
                            String code = jsonObject.getString("code");
                            int table_id = jsonObject.getInt("tableId");
                            int user_id = jsonObject.getInt("userId");
                            double total_price = jsonObject.getDouble("total_price");
                            String name_user = jsonObject.getString("name_user");
                            String shop_id = jsonObject.getString("id_shop");

                            // Chuyển đổi chuỗi datetime thành đối tượng Date
                            Date datetime = inputFormat.parse(dateTime_end);
                            // Lấy năm và tháng từ đối tượng Date
                            String Year = outputYearFormat.format(datetime);
                            String Month = outputMonthFormat.format(datetime);
                            String Day = outputDatFormat.format(datetime);


                            int billYear = Integer.parseInt(Year);
                            int billMonth = Integer.parseInt(Month);
                            int billDay = Integer.parseInt(Day);

                            if(id_shop.equals(shop_id )){
                                if(year != 0 && month != 0 && dayOfMonth != 0){

                                    if (billYear == year && billMonth == month && billDay == dayOfMonth){
                                        totalAmount += total_price;
                                        Bill_Admin billsAdmin = new  Bill_Admin(id_bill, dateTime, dateTime_end, code, table_id, user_id, total_price, name_user,shop_id);
                                        Bill_AdminArayList.add(billsAdmin);
                                    }
                                }
                                else{
                                    totalAmount += total_price;
                                    Bill_Admin billsAdmin = new  Bill_Admin(id_bill, dateTime, dateTime_end, code, table_id, user_id, total_price, name_user,shop_id);
                                    Bill_AdminArayList.add(billsAdmin);
                                }

                            }


                        }
                        String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalAmount);
                        totalAmountTextView.setText(formattedTotalAmount + " VND");
                        // Cập nhật dữ liệu mới cho adapter
                        billAdminAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}