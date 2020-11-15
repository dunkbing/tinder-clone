package com.dangbinh.moneymanagement.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.models.Transaction;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        TextView textViewIncome = findViewById(R.id.text_view_income);
        TextView textViewExpense = findViewById(R.id.text_view_expense);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        Toolbar toolbar = findViewById(R.id.trans_view_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BarChart barChart = findViewById(R.id.bar_chart);
        List<Transaction> transactions = TransactionsViewActivity.transactions;
        AtomicReference<Double> income = new AtomicReference<>((double) 0);
        AtomicReference<Double> expense = new AtomicReference<>((double) 0);
        List<BarEntry> entries = new ArrayList<>();
        transactions.forEach(transaction -> {
            // entries.add(new BarEntry(2014, (float) transaction.getAmount()));
            if (transaction.getType() == Transaction.Type.INCOME) {
                income.updateAndGet(v -> (double) (v + transaction.getAmount()));
            }
            if (transaction.getType() == Transaction.Type.EXPENSE) {
                expense.updateAndGet(v -> (double) (v + transaction.getAmount()));
            }
        });
        entries.add(new BarEntry(1, (float)((double)income.get())));
        entries.add(new BarEntry(2, (float)((double)expense.get())));
        textViewIncome.setText(String.valueOf(income.get()));
        textViewExpense.setText(String.valueOf(expense.get()));
        BarDataSet barDataSet = new BarDataSet(entries, "Transactions");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        // barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Report");
        barChart.animateY(2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(this, TransactionsViewActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, TaxEstimatorActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_report) {
            Intent i = new Intent(getApplicationContext(), ReportActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}