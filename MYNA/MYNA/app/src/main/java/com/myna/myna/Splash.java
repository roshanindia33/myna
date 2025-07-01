package com.myna.myna;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class Splash extends AppCompatActivity {
    private SessionManager sessionManager;
    private static final int SPLASH_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        sessionManager = new SessionManager(getApplicationContext());

        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);


        new Handler().postDelayed(() -> {
//            check if use already login or not
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(Splash.this, Dashboard.class));
            } else {
                // LoginActivity
                startActivity(new Intent(Splash.this, Login.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}


