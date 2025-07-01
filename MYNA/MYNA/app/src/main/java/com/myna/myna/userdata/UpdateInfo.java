package com.myna.myna.userdata;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myna.myna.R;

public class UpdateInfo extends AppCompatActivity {

    private static final String TAG = "NewsActivity";
    private WebView webViewNews;
    private ProgressBar progressBar;
    private DatabaseReference newsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_info);

        webViewNews = findViewById(R.id.webview_news);
        progressBar = findViewById(R.id.progress_bar);

        // Firebase
        newsRef = FirebaseDatabase.getInstance().getReference("update");

        setupWebView();
        fetchNewsContent();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webViewNews.canGoBack()) {
                    webViewNews.goBack();
                } else {
                    finishAffinity();
                }
            }
        });
    }

    private void setupWebView() {
        // WebView सेटिंग्स
        webViewNews.getSettings().setJavaScriptEnabled(false);
        webViewNews.getSettings().setDomStorageEnabled(true);
        webViewNews.getSettings().setLoadsImagesAutomatically(true);
        webViewNews.getSettings().setDefaultTextEncodingName("utf-8");
        webViewNews.getSettings().setBuiltInZoomControls(false);
        webViewNews.getSettings().setDisplayZoomControls(false);
        webViewNews.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewNews.setVerticalScrollBarEnabled(true);
        webViewNews.setHorizontalScrollBarEnabled(false);
        
        webViewNews.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                webViewNews.setVisibility(View.GONE); //
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webViewNews.setVisibility(View.VISIBLE);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateInfo.this, "Error loading content: " + description, Toast.LENGTH_LONG).show();
                Log.e(TAG, "WebView error (old): " + description + " URL: " + failingUrl);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (request.isForMainFrame()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateInfo.this, "Error loading content: " + error.getDescription(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "WebView error (new): " + error.getDescription() + " URL: " + request.getUrl());
                }
            }

        });
    }

    private void fetchNewsContent() {
        progressBar.setVisibility(View.VISIBLE);
        webViewNews.setVisibility(View.GONE);

        newsRef.child("update_info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    String htmlContent = dataSnapshot.getValue(String.class);
                    if (htmlContent != null && !htmlContent.isEmpty()) {
                        webViewNews.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
                    } else {
                        Toast.makeText(UpdateInfo.this, "No content found.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        webViewNews.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(UpdateInfo.this, "Internet connection too slow..", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    webViewNews.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read content: " + databaseError.getMessage());
                Toast.makeText(UpdateInfo.this, "Failed to load content: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                webViewNews.setVisibility(View.VISIBLE);
            }
        });
    }
}