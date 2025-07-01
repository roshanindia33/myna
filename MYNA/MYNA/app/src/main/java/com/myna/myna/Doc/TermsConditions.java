package com.myna.myna.Doc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.R; // R.layout.terms_conditions और R.id.main के लिए

public class TermsConditions extends AppCompatActivity {
    TextView termsText;
    Button translateButton, acceptBtn, declineBtn;
    boolean isHindiVisible = true;
    CardView tcart1, tcart2, tcart3, tcart4, tcart5, tcart6, tcart7, tcart8, tcart9,
            tcart1h, tcart2h, tcart3h, tcart4h, tcart5h, tcart6h, tcart7h, tcart8h, tcart9h;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.terms_conditions);

        translateButton = findViewById(R.id.translateButton);


        tcart1h = findViewById(R.id.tcart1h);
        tcart2h = findViewById(R.id.tcart2h);
        tcart3h = findViewById(R.id.tcart3h);
        tcart4h = findViewById(R.id.tcart4h);
        tcart5h = findViewById(R.id.tcart5h);
        tcart6h = findViewById(R.id.tcart6h);
        tcart7h = findViewById(R.id.tcart7h);
        tcart8h = findViewById(R.id.tcart8h);
        tcart9h = findViewById(R.id.tcart9h);


        tcart1 = findViewById(R.id.tcart1);
        tcart2 = findViewById(R.id.tcart2);
        tcart3 = findViewById(R.id.tcart3);
        tcart4 = findViewById(R.id.tcart4);
        tcart5 = findViewById(R.id.tcart5);
        tcart6 = findViewById(R.id.tcart6);
        tcart7 = findViewById(R.id.tcart7);
        tcart8 = findViewById(R.id.tcart8);
        tcart9 = findViewById(R.id.tcart9);


        if (tcart1h != null) tcart1h.setVisibility(View.GONE);
        if (tcart2h != null) tcart2h.setVisibility(View.GONE);
        if (tcart3h != null) tcart3h.setVisibility(View.GONE);
        if (tcart4h != null) tcart4h.setVisibility(View.GONE);
        if (tcart5h != null) tcart5h.setVisibility(View.GONE);
        if (tcart6h != null) tcart6h.setVisibility(View.GONE);
        if (tcart7h != null) tcart7h.setVisibility(View.GONE);
        if (tcart8h != null) tcart8h.setVisibility(View.GONE);
        if (tcart9h != null) tcart9h.setVisibility(View.GONE);


        if (tcart1 != null) tcart1.setVisibility(View.VISIBLE);
        if (tcart2 != null) tcart2.setVisibility(View.VISIBLE);
        if (tcart3 != null) tcart3.setVisibility(View.VISIBLE);
        if (tcart4 != null) tcart4.setVisibility(View.VISIBLE);
        if (tcart5 != null) tcart5.setVisibility(View.VISIBLE);
        if (tcart6 != null) tcart6.setVisibility(View.VISIBLE);
        if (tcart7 != null) tcart7.setVisibility(View.VISIBLE);
        if (tcart8 != null) tcart8.setVisibility(View.VISIBLE);
        if (tcart9 != null) tcart9.setVisibility(View.VISIBLE);


        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHindiVisible) {

                    if (tcart1 != null) tcart1.setVisibility(View.VISIBLE);
                    if (tcart2 != null) tcart2.setVisibility(View.VISIBLE);
                    if (tcart3 != null) tcart3.setVisibility(View.VISIBLE);
                    if (tcart4 != null) tcart4.setVisibility(View.VISIBLE);
                    if (tcart5 != null) tcart5.setVisibility(View.VISIBLE);
                    if (tcart6 != null) tcart6.setVisibility(View.VISIBLE);
                    if (tcart7 != null) tcart7.setVisibility(View.VISIBLE);
                    if (tcart8 != null) tcart8.setVisibility(View.VISIBLE);
                    if (tcart9 != null) tcart9.setVisibility(View.VISIBLE);


                    if (tcart1h != null) tcart1h.setVisibility(View.GONE);
                    if (tcart2h != null) tcart2h.setVisibility(View.GONE);
                    if (tcart3h != null) tcart3h.setVisibility(View.GONE);
                    if (tcart4h != null) tcart4h.setVisibility(View.GONE);
                    if (tcart5h != null) tcart5h.setVisibility(View.GONE);
                    if (tcart6h != null) tcart6h.setVisibility(View.GONE);
                    if (tcart7h != null) tcart7h.setVisibility(View.GONE);
                    if (tcart8h != null) tcart8h.setVisibility(View.GONE);
                    if (tcart9h != null) tcart9h.setVisibility(View.GONE);

                    translateButton.setText("H/E");
                    isHindiVisible = false;
                } else {

                    if (tcart1h != null) tcart1h.setVisibility(View.VISIBLE);
                    if (tcart2h != null) tcart2h.setVisibility(View.VISIBLE);
                    if (tcart3h != null) tcart3h.setVisibility(View.VISIBLE);
                    if (tcart4h != null) tcart4h.setVisibility(View.VISIBLE);
                    if (tcart5h != null) tcart5h.setVisibility(View.VISIBLE);
                    if (tcart6h != null) tcart6h.setVisibility(View.VISIBLE);
                    if (tcart7h != null) tcart7h.setVisibility(View.VISIBLE);
                    if (tcart8h != null) tcart8h.setVisibility(View.VISIBLE);
                    if (tcart9h != null) tcart9h.setVisibility(View.VISIBLE);


                    if (tcart1 != null) tcart1.setVisibility(View.GONE);
                    if (tcart2 != null) tcart2.setVisibility(View.GONE);
                    if (tcart3 != null) tcart3.setVisibility(View.GONE);
                    if (tcart4 != null) tcart4.setVisibility(View.GONE);
                    if (tcart5 != null) tcart5.setVisibility(View.GONE);
                    if (tcart6 != null) tcart6.setVisibility(View.GONE);
                    if (tcart7 != null) tcart7.setVisibility(View.GONE);
                    if (tcart8 != null) tcart8.setVisibility(View.GONE);
                    if (tcart9 != null) tcart9.setVisibility(View.GONE);

                    translateButton.setText("E/H");
                    isHindiVisible = true;
                }
            }
        });

        if (acceptBtn != null) {
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }
}