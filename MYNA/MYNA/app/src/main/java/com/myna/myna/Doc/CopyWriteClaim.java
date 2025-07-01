package com.myna.myna.Doc;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.R;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class CopyWriteClaim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.copy_write_claim);
//
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(CopyWriteClaim.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(CopyWriteClaim.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(CopyWriteClaim.this, Support.class));
    }
}