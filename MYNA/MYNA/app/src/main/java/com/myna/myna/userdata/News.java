package com.myna.myna.userdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView; // Needed for TextView references

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.myna.myna.Doc.About;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R;

public class News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.news);

        // Set activity title (optional, if action bar is visible)
        // setTitle("News & Updates");

        TextView newsItem1 = findViewById(R.id.news_item_1);
        TextView newsItem2 = findViewById(R.id.news_item_2);
        TextView newsItem3 = findViewById(R.id.news_item_3);
        TextView newsItem4 = findViewById(R.id.news_item_4);
        TextView newsItem5 = findViewById(R.id.news_item_5);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            newsItem1.setText(Html.fromHtml(newsItem1.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            newsItem2.setText(Html.fromHtml(newsItem2.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            newsItem3.setText(Html.fromHtml(newsItem3.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            newsItem4.setText(Html.fromHtml(newsItem4.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            newsItem5.setText(Html.fromHtml(newsItem5.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            newsItem1.setText(Html.fromHtml(newsItem1.getText().toString()));
            newsItem2.setText(Html.fromHtml(newsItem2.getText().toString()));
            newsItem3.setText(Html.fromHtml(newsItem3.getText().toString()));
            newsItem4.setText(Html.fromHtml(newsItem4.getText().toString()));
            newsItem5.setText(Html.fromHtml(newsItem5.getText().toString()));
        }
    }
    public void openPrivacyFromFooter(View view) {
        startActivity(new Intent(News.this, PrivacyPolicy.class));
    }
    public void openTermsFromFooter(View view) {
        startActivity(new Intent(News.this, TermsConditions.class));
    }
    public void openSupportFromFooter(View view) {
        startActivity(new Intent(News.this, Support.class));
    }
}