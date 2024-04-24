package com.example.kiotviet_fake.activities;

import static android.telephony.PhoneNumberUtils.formatNumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiotviet_fake.R;

import java.text.DecimalFormat;

public class SingleGraftActivity extends AppCompatActivity {
    ImageView btnCancel;
    TextView quantity_item,TotalPrice,name_Table;
    String nameTable,idTable,orderId;
    LinearLayout chonBan;
    int quantityItem ;
    float priceTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_graft);
        addControl();
        RenderView();
        BtnOnClick();
    }

    private void RenderView() {
        quantity_item.setText(String.valueOf(quantityItem));
        System.out.println("test : " + priceTotal);
        TotalPrice.setText(formatNumber(priceTotal));
        name_Table.setText(nameTable);
    }
    public static String formatNumber(float number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
    private void BtnOnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chonBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(SingleGraftActivity.this, TableListActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void addControl() {
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        quantity_item = (TextView) findViewById(R.id.quantity_item);
        TotalPrice = (TextView) findViewById(R.id.TotalPrice);
        chonBan = (LinearLayout) findViewById(R.id.btn_chon_ban);
        name_Table = (TextView) findViewById(R.id.nameTable) ;



        Intent intent = getIntent();
         quantityItem = intent.getIntExtra("quantity_product",0);
         nameTable = intent.getStringExtra("nameTable");
         idTable = intent.getStringExtra("id_table");
        priceTotal = intent.getFloatExtra("price",0);
        System.out.println("test value single: " + priceTotal + idTable + nameTable + quantityItem);

    }
}