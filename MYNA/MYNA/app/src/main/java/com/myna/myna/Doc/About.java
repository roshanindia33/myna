package com.myna.myna.Doc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.myna.myna.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.about_page);

    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(About.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(About.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(About.this, Support.class));
    }
}