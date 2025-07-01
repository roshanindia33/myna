package com.myna.myna.servicepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.myna.myna.Doc.About;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R;
import com.myna.myna.Signup;
import com.myna.myna.userdata.Invest;
import com.myna.myna.userdata.InvestCenter;
import android.text.Html;

public class InvestWithUs extends AppCompatActivity {
    Button invest_with_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.invest_with_us);

        invest_with_us = findViewById(R.id.invest_page_transport);

        invest_with_us.setOnClickListener(v -> {
            startActivity(new Intent(InvestWithUs.this, InvestCenter.class));
        });
        TextView introTextCard = findViewById(R.id.tv_intro_text_card);
        TextView traditionalTextCard = findViewById(R.id.tv_traditional_text_card);
        TextView programTextCard = findViewById(R.id.tv_program_text_card);
        TextView approachTextCard = findViewById(R.id.tv_approach_text_card);
        TextView startSmallTextCard = findViewById(R.id.tv_start_small_text_card);
        TextView supportHeadingDesContent = findViewById(R.id.tv_support_heading_des_content);
        TextView supportTextCard = findViewById(R.id.tv_support_text_card);
        TextView contactUsFooter = findViewById(R.id.contact_us_footer_text_view);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            introTextCard.setText(Html.fromHtml(introTextCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            traditionalTextCard.setText(Html.fromHtml(traditionalTextCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            programTextCard.setText(Html.fromHtml(programTextCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            approachTextCard.setText(Html.fromHtml(approachTextCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            startSmallTextCard.setText(Html.fromHtml(startSmallTextCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            supportHeadingDesContent.setText(Html.fromHtml(supportHeadingDesContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            supportTextCard.setText(Html.fromHtml(supportTextCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            introTextCard.setText(Html.fromHtml(introTextCard.getText().toString()));
            traditionalTextCard.setText(Html.fromHtml(traditionalTextCard.getText().toString()));
            programTextCard.setText(Html.fromHtml(programTextCard.getText().toString()));
            approachTextCard.setText(Html.fromHtml(approachTextCard.getText().toString()));
            startSmallTextCard.setText(Html.fromHtml(startSmallTextCard.getText().toString()));
            supportHeadingDesContent.setText(Html.fromHtml(supportHeadingDesContent.getText().toString()));
            supportTextCard.setText(Html.fromHtml(supportTextCard.getText().toString()));
             contactUsFooter.setText(Html.fromHtml(contactUsFooter.getText().toString()));
        }
        View transportCard = findViewById(R.id.invest_page_transport);
        transportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvestWithUs.this, InvestCenter.class);
                startActivity(intent);
            }
        });
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(InvestWithUs.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(InvestWithUs.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(InvestWithUs.this, Support.class));
    }
}