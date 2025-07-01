package com.myna.myna.userdata;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable; // For dynamic gradient background
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myna.myna.Doc.About;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R;
import com.myna.myna.userdata.ReturnHistory;

import java.util.ArrayList;
import java.util.List;

public class UserPortfolio extends AppCompatActivity {

    LineChart lineChart;
    Button toggleChartButton, investmentHistoryButton, exploreReportsButton;
    TextView chartTitle, chartPlaceholderText;
    TextView investedValue, investmentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_porfolio);

        investedValue = findViewById(R.id.invest_amount_db);
        investmentDate = findViewById(R.id.invest_date_db);
        investmentHistoryButton = findViewById(R.id.investment_history);
        toggleChartButton = findViewById(R.id.returns_chart_show);
        lineChart = findViewById(R.id.lineChart);
        chartTitle = findViewById(R.id.chart_title);
        chartPlaceholderText = findViewById(R.id.chart_placeholder_text);
        exploreReportsButton = findViewById(R.id.explore_reports_btn);

        // --- Firebase Data Fetching ---
        loadInvestmentData();

        // --- Button Listeners ---
        investmentHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPortfolio.this, ReturnHistory.class);
                startActivity(intent);
            }
        });

        toggleChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineChart.getVisibility() == View.VISIBLE) {
                    lineChart.setVisibility(View.GONE);
                    chartTitle.setVisibility(View.GONE);
                    chartPlaceholderText.setVisibility(View.VISIBLE);
                    toggleChartButton.setText("Show Chart");
                } else {
                    lineChart.setVisibility(View.VISIBLE);
                    chartTitle.setVisibility(View.VISIBLE);
                    chartPlaceholderText.setVisibility(View.GONE);
                    toggleChartButton.setText("Hide Chart");
                }
            }
        });

        exploreReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserPortfolio.this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Initialize and Style Line Chart ---
        setupLineChart();
        loadLineChartData();
    }

    @SuppressLint("SetTextI18n")
    private void loadInvestmentData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String Uid = currentUser.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                    .child(currentUser.getUid()).child("investment");

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Double amount = snapshot.child("balance").getValue(Double.class);
//                    String date = snapshot.child("date").getValue(String.class);
                    if (amount != null) {
                        investedValue.setText(String.format("₹ %.2f", amount));
//                        investmentDate.setText("Last Updated: " + date);
                    } else {
                        investedValue.setText("₹0.00");
                        investmentDate.setText("Last Updated: N/A");
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    investedValue.setText("₹0.00");
                    investmentDate.setText("Last Updated: Error");
                    Toast.makeText(UserPortfolio.this, "Failed to load investment data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            investedValue.setText("₹0.00");
            investmentDate.setText("Last Updated: N/A");
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupLineChart() {
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }

    private void loadLineChartData() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 1200f)); // Sample data
        entries.add(new Entry(2f, 2500f));
        entries.add(new Entry(3f, 3800f));
        entries.add(new Entry(4f, 4900f));
        entries.add(new Entry(5f, 6100f));
        entries.add(new Entry(6f, 7300f));
        entries.add(new Entry(7f, 8500f));


        LineDataSet dataSet = new LineDataSet(entries, "Investment Growth");

        // Styling for the chart line
        dataSet.setColor(ContextCompat.getColor(this, R.color.chart_line_color));
        dataSet.setLineWidth(3f); // Thicker line
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.chart_line_color));
        dataSet.setCircleRadius(6f); // Larger circles
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.chart_fill_color)); // Gradient fill below line
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Smooth curve
        dataSet.setHighLightColor(Color.GRAY); // Optional: color for highlighting touch

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(UserPortfolio.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(UserPortfolio.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(UserPortfolio.this, Support.class));
    }
}
