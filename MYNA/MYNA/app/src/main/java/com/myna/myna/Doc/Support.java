package com.myna.myna.Doc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.R;
import android.text.Html;

public class Support extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.support);

        TextView generalInquiriesContent = findViewById(R.id.tv_general_inquiries_content);
        TextView accountTechContent = findViewById(R.id.tv_account_tech_content);
        TextView investmentWithdrawalContent = findViewById(R.id.tv_investment_withdrawal_content);
        TextView dataDeletionContent = findViewById(R.id.tv_data_deletion_content);
        TextView copyrightClaimsContent = findViewById(R.id.tv_copyright_claims_content);
        TextView commitmentContent = findViewById(R.id.tv_commitment_content);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            generalInquiriesContent.setText(Html.fromHtml(generalInquiriesContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            accountTechContent.setText(Html.fromHtml(accountTechContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            investmentWithdrawalContent.setText(Html.fromHtml(investmentWithdrawalContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            dataDeletionContent.setText(Html.fromHtml(dataDeletionContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            copyrightClaimsContent.setText(Html.fromHtml(copyrightClaimsContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            commitmentContent.setText(Html.fromHtml(commitmentContent.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            generalInquiriesContent.setText(Html.fromHtml(generalInquiriesContent.getText().toString()));
            accountTechContent.setText(Html.fromHtml(accountTechContent.getText().toString()));
            investmentWithdrawalContent.setText(Html.fromHtml(investmentWithdrawalContent.getText().toString()));
            dataDeletionContent.setText(Html.fromHtml(dataDeletionContent.getText().toString()));
            copyrightClaimsContent.setText(Html.fromHtml(copyrightClaimsContent.getText().toString()));
            commitmentContent.setText(Html.fromHtml(commitmentContent.getText().toString()));
        }
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(Support.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(Support.this, TermsConditions.class));
    }
}