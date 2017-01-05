package com.dinamic.ivan.receipts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;

import com.dinamic.ivan.entities.ExpenseCategory;
import com.dinamic.ivan.entities.GoodUnit;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.entities.ReceiptGood;

import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class ReceiptDataActivity extends AppCompatActivity {
    private String TIME_FORMAT = "HH:mm";
    private DateTimeFormatter VIEW_DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy");
    private DateTimeFormatter VIEW_TIME_FORMATTER = DateTimeFormat.forPattern(TIME_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_data);

        Receipt receipt = (Receipt) getIntent().getSerializableExtra(MainActivity.EXTRA_RECEIPT);

        setReceipInView(receipt);

        ((Spinner) findViewById(R.id.category)).setAdapter(new ArrayAdapter<ExpenseCategory>(this, R.layout.support_simple_spinner_dropdown_item, ExpenseCategory.values()));

        ((TextClock) findViewById(R.id.time)).setFormat12Hour(null);
        ((TextClock) findViewById(R.id.time)).setFormat24Hour(TIME_FORMAT);

        ((Button) findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Receipt receipt = viewToReceipt();
            }
        });

        ((Button) findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setReceipInView(Receipt receipt) {
        ((EditText)  findViewById(R.id.store   )).setText(receipt.storeName);
        ((EditText)  findViewById(R.id.date    )).setText(VIEW_DATE_FORMATTER.print(receipt.datetime));
        ((TextClock) findViewById(R.id.time    )).setText(VIEW_TIME_FORMATTER.print(receipt.datetime));
        ((Spinner)   findViewById(R.id.category)).setSelection((new ArrayList<ExpenseCategory>(Arrays.asList(ExpenseCategory.values()))).indexOf(receipt.category));
        ((EditText)  findViewById(R.id.currency)).setText(receipt.currency);

        TableLayout goodsTableView = (TableLayout) findViewById(R.id.goods);

        for (ReceiptGood good: receipt.goods)
            goodsTableView.addView(createGoodView(good));
    }

    private View createGoodView(ReceiptGood good) {
        TableRow view = new TableRow(this);

        EditText name = new EditText(this);
        name.setText(good.name);

        EditText count = new EditText(this);
        count.setText(String.valueOf(good.count));

        Spinner unit = new Spinner(this);
        unit.setAdapter(new ArrayAdapter<GoodUnit>(this, R.layout.support_simple_spinner_dropdown_item, GoodUnit.values()));
        unit.setSelection((new ArrayList<GoodUnit>(Arrays.asList(GoodUnit.values()))).indexOf(good.unit));

        EditText price = new EditText(this);
        price.setText(String.valueOf(good.price));

        view.addView(name);
        view.addView(count);
        view.addView(unit);
        view.addView(price);

        return view;
    }

    private Receipt viewToReceipt() {
        String store    = ((EditText)  findViewById(R.id.store   )).getText().toString();
        String dateStr  = ((EditText)  findViewById(R.id.date    )).getText().toString();
        String timeStr  = ((TextClock) findViewById(R.id.time    )).getText().toString();
        int categoryId  = ((Spinner)   findViewById(R.id.category)).getSelectedItemPosition();
        String currency = ((EditText)  findViewById(R.id.currency)).getText().toString();

        ExpenseCategory category = ExpenseCategory.values()[categoryId];

        LocalTime time = DateTime.parse(timeStr, VIEW_TIME_FORMATTER).toLocalTime();
        DateTime datetime = DateTime.parse(dateStr, VIEW_DATE_FORMATTER);

        datetime = datetime.plusHours(time.getHourOfDay());
        datetime = datetime.plusMinutes(time.getMinuteOfHour());

        return new Receipt(new ArrayList<ReceiptGood>(), store, currency, category, datetime);
    }
}
