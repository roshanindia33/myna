package com.myna.myna.userdata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.Doc.About;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R;
import androidx.core.content.ContextCompat; // For setting text color programmatically

public class ReturnHistory extends AppCompatActivity {

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.return_history);

        TextView currentMonthDisplay = findViewById(R.id.current_month_display);
        currentMonthDisplay.setText("JULY 2025"); // Current month is June 2025

        // --- Card 1: Latest Month (May 2025) ---
        TextView tvMonthYear1 = findViewById(R.id.tv_month_year_1);
        TextView tvReturnsValue1 = findViewById(R.id.tv_returns_value_1);
        TextView tvTag1_1 = findViewById(R.id.tv_tag1_1);
        TextView tvTag2_1 = findViewById(R.id.tv_tag2_1);

        tvMonthYear1.setText("June 2025");
        tvReturnsValue1.setText("+11.1%");
        tvReturnsValue1.setTextColor(ContextCompat.getColor(this, R.color.green_positive)); // Assuming you have a color resource named green_positive
        tvTag1_1.setText("Aggressive");
        tvTag2_1.setText("Equity Trade");

        // --- Card 2: Previous Month (April 2025) ---
        TextView tvMonthYear2 = findViewById(R.id.tv_month_year_2);
        TextView tvReturnsValue2 = findViewById(R.id.tv_returns_value_2);
        TextView tvTag1_2 = findViewById(R.id.tv_tag1_2);
        TextView tvTag2_2 = findViewById(R.id.tv_tag2_2);

        tvMonthYear2.setText("May 2025");
        tvReturnsValue2.setText("+9.5%");
        tvReturnsValue2.setTextColor(ContextCompat.getColor(this, R.color.green_positive));
        tvTag1_2.setText("Balanced");
        tvTag2_2.setText("Diversified");

        // --- Card 3: Month Before Previous (March 2025) ---
        TextView tvMonthYear3 = findViewById(R.id.tv_month_year_3);
        TextView tvReturnsValue3 = findViewById(R.id.tv_returns_value_3);
        TextView tvTag1_3 = findViewById(R.id.tv_tag1_3);
        TextView tvTag2_3 = findViewById(R.id.tv_tag2_3);

        tvMonthYear3.setText("April 2025");
        tvReturnsValue3.setText("-2.5%");
        tvReturnsValue3.setTextColor(ContextCompat.getColor(this, R.color.red_negative)); // Assuming you have a color resource named red_negative
        tvTag1_3.setText("High Risk");
        tvTag2_3.setText("Trade");
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(ReturnHistory.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(ReturnHistory.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(ReturnHistory.this, Support.class));
    }
}