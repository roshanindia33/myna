package com.myna.myna;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.TermsConditions;

public class WarningTC extends AppCompatActivity {
    CheckBox checkBox;
    Button continueBtn, btnTerms, btnPrivacy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.warning_tc);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.warning_tc_main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        Warning Terms & Conditions
        checkBox = findViewById(R.id.checkbox_wtc);
        continueBtn = findViewById(R.id.agree_wtc);
        btnTerms = findViewById(R.id.terms_wtc);
        btnPrivacy = findViewById(R.id.privacy_wtc);

        // Disable button initially
        continueBtn.setEnabled(false);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            continueBtn.setEnabled(isChecked);
        });

        continueBtn.setOnClickListener(v -> {
            // Save in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("termsAccepted", true);
            editor.apply();

            // Go to Dashboard
            Intent intent = new Intent(WarningTC.this, Dashboard.class);
            startActivity(intent);
            finish();
        });



//        Terms & conditions pages
        btnTerms.setOnClickListener(v -> {
// Terms & Conditions page open
            Intent intent = new Intent(WarningTC.this, TermsConditions.class);
            startActivity(intent);
        });

        btnPrivacy.setOnClickListener(v -> {
// Privacy Policy page open
            Intent intent = new Intent(WarningTC.this, PrivacyPolicy.class);
            startActivity(intent);
        });
    }
}


