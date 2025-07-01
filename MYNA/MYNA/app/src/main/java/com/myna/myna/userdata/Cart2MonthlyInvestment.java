package com.myna.myna.userdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html; // Import Html class
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // Import TextView class
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.Doc.About;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R;

public class Cart2MonthlyInvestment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.cart2_monthly_investment_insight);

        Button investNowButton = findViewById(R.id.fund_section_nws);

        investNowButton.setEnabled(false);

        TextView introOpportunities = findViewById(R.id.tv_intro_opportunities);
        TextView whatsNewContent = findViewById(R.id.tv_whats_new_content);
        TextView securePathContent = findViewById(R.id.tv_secure_path_content);
        TextView whyChooseUsContent = findViewById(R.id.tv_why_choose_us_content);
        TextView missionContent = findViewById(R.id.tv_mission_content);
        // No ID needed for the final contact TextView as it's directly in XML without a variable


        // Parse HTML content for each TextView
        // Check API level for Html.fromHtml compatibility
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            introOpportunities.setText(Html.fromHtml(introOpportunities.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            whatsNewContent.setText(Html.fromHtml(whatsNewContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            securePathContent.setText(Html.fromHtml(securePathContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            whyChooseUsContent.setText(Html.fromHtml(whyChooseUsContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            missionContent.setText(Html.fromHtml(missionContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            // For the final contact TextView, it's directly set in XML, no parsing needed here if it's the only one without an ID
        } else {
            introOpportunities.setText(Html.fromHtml(introOpportunities.getText().toString()));
            whatsNewContent.setText(Html.fromHtml(whatsNewContent.getText().toString()));
            securePathContent.setText(Html.fromHtml(securePathContent.getText().toString()));
            whyChooseUsContent.setText(Html.fromHtml(whyChooseUsContent.getText().toString()));
            missionContent.setText(Html.fromHtml(missionContent.getText().toString()));
        }

        // Optional: Set a click listener for the button (even if disabled initially)
        investNowButton.setOnClickListener(v -> {
        });
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(Cart2MonthlyInvestment.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(Cart2MonthlyInvestment.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(Cart2MonthlyInvestment.this, Support.class));
    }
}