package com.myna.myna.userdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html; // Import Html class
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // Import TextView class

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

public class InvestCenter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.invest_center);

        setTitle("Investment Centre");

        // Initialize TextViews for HTML parsing
        TextView taglineCard = findViewById(R.id.tagline_card);
        TextView descriptionCard = findViewById(R.id.description_card);
        TextView riskPhilosophyText = findViewById(R.id.risk_philosophy_text);
        TextView personalizedPortfolioText = findViewById(R.id.personalized_portfolio_text);
        TextView footerNoteCard = findViewById(R.id.footer_note_card);


        // Parse HTML content for each TextView
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            taglineCard.setText(Html.fromHtml(taglineCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            descriptionCard.setText(Html.fromHtml(descriptionCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            riskPhilosophyText.setText(Html.fromHtml(riskPhilosophyText.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            personalizedPortfolioText.setText(Html.fromHtml(personalizedPortfolioText.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            footerNoteCard.setText(Html.fromHtml(footerNoteCard.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            taglineCard.setText(Html.fromHtml(taglineCard.getText().toString()));
            descriptionCard.setText(Html.fromHtml(descriptionCard.getText().toString()));
            riskPhilosophyText.setText(Html.fromHtml(riskPhilosophyText.getText().toString()));
            personalizedPortfolioText.setText(Html.fromHtml(personalizedPortfolioText.getText().toString()));
            footerNoteCard.setText(Html.fromHtml(footerNoteCard.getText().toString()));
        }

        // Setup the button click listener
        Button startInvestmentProceed = findViewById(R.id.start_investment_proceed);
        startInvestmentProceed.setOnClickListener(v -> {
            Intent intent = new Intent(InvestCenter.this, Invest.class);
            startActivity(intent);
        });
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(InvestCenter.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(InvestCenter.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(InvestCenter.this, Support.class));
    }
}