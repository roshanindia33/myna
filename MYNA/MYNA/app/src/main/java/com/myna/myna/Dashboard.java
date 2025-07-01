package com.myna.myna;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myna.myna.Doc.About;
import com.myna.myna.Doc.CopyWriteClaim;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.databinding.ActivityMainBinding;
import com.myna.myna.userdata.Cart2MonthlyInvestment;
import com.myna.myna.userdata.News;
import com.myna.myna.userdata.UpdateInfo;
import com.myna.myna.userdata.UserAccount;
import com.myna.myna.userdata.UserPortfolio;
import com.myna.myna.BuildConfig;

public class Dashboard extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    private ActivityMainBinding binding;
    FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigation_View;
    TextView navName, navEmail;
    private DatabaseReference update_db;
    int latestVersion = 0;
    Handler handler = new Handler();
    Runnable checkInternetRunnable;
    boolean isChecking = false;

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        check for update version=================================================================
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        update_db = database.getReference("update");
        update_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot versionSnapshot = snapshot.child("latest_version_code");
                    if (versionSnapshot.exists() && versionSnapshot.getValue() != null) {
                        Long versionCodeLong = versionSnapshot.getValue(Long.class);
                        if (versionCodeLong != null) {
                            int serverVersionCode = versionCodeLong.intValue();
                            if (BuildConfig.VERSION_CODE < serverVersionCode) {
                                // Show update screen
                                Intent intent = new Intent(Dashboard.this, UpdateInfo.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Please check your Internet connection ! ", Toast.LENGTH_SHORT).show();
            }
        });


//  check for update version=================================================================
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
//        check internet protocall=================================================

        checkInternetRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isNetworkAvailable(Dashboard.this)) {
                    if (!isChecking) {
                        isChecking = true;
                        Toast.makeText(Dashboard.this, "Please provide Internet connection!", Toast.LENGTH_SHORT).show();
                    }
                    handler.postDelayed(this, 2000); // फिर से चेक 8 सेकंड में
                } else {
                    isChecking = false;
                    handler.removeCallbacks(this); // इंटरनेट है, तो बंद करो
                }
            }
        };

        handler.post(checkInternetRunnable); // पहली बार रन

//  check internet protocol end===================================================

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Support.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_smart_advisor,R.id.nav_ai_assistant,R.id.nav_virtual_portfolio
        ,R.id.nav_learning_hub,R.id.nav_help_center,R.id.nav_feedback,R.id.nav_invite_friends,R.id.nav_about_app,R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        ================user data load in dashboard==========================
        drawerLayout = findViewById(R.id.drawer_layout);
        navigation_View = findViewById(R.id.nav_view);

        View headerView = navigation_View.getHeaderView(0);
        navName = headerView.findViewById(R.id.user_name);
        navEmail = headerView.findViewById(R.id.user_email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            navEmail.setText(user.getEmail());
        }
        //        ================user data load in dashboard==========================
//        left side menu items====================================================================================
        navigationView = findViewById(R.id.nav_view);

        // Handle menu item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_logout) {
                    // Handle logout logic
                    SessionManager sessionManager = new SessionManager(Dashboard.this);
                    sessionManager.clearSession();

                    Intent intent = new Intent(Dashboard.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (id == R.id.nav_gallery) {
                    Toast.makeText(Dashboard.this, "Empty", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id == R.id.nav_slideshow) {
                    Toast.makeText(Dashboard.this, "0 videos Available", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_about_app) {
                    Intent intent = new Intent(Dashboard.this, About.class);
                    startActivity(intent);
                    return true;
                }
                else if (id == R.id.nav_help_center) {
                    Intent intent = new Intent(Dashboard.this, Support.class);
                    startActivity(intent);
                    return true;
                }

                // handle other items if needed...

                return true;
            }
        });
//        left side menu items========================================================================================================
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Using if-else if structure for clarity, or a switch statement if preferred
        int id = item.getItemId(); // Get the ID of the selected menu item

        if (id == R.id.user_profile) {
            // Navigate to User Profile screen
            Intent intent = new Intent(Dashboard.this, UserPortfolio.class); // Assuming UserProfile is your profile activity
            startActivity(intent);
            return true;
        } else if (id == R.id.user_account) {
            // Navigate to Bank Account screen
            Intent intent = new Intent(Dashboard.this, UserAccount.class); // Assuming UserAccount is your bank details activity
            startActivity(intent);
            return true;
        } else if (id == R.id.investment_history_menu_item) {
             Intent intent = new Intent(Dashboard.this, UserPortfolio.class);
             startActivity(intent);
            return true;
        } else if (id == R.id.news_updates_menu_item) {
            // Navigate to News & Updates screen
            Toast.makeText(this, "Fetching News & Updates...", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(Dashboard.this, News.class);
             startActivity(intent);
            return true;
        } else if (id == R.id.monthly_insights_menu_item) {
             Intent intent = new Intent(Dashboard.this, Cart2MonthlyInvestment.class);
             startActivity(intent);
            return true;
        }else if (id == R.id.contact_info) {
             Intent intent = new Intent(Dashboard.this, Support.class);
             startActivity(intent);
            return true;
        } else if (id == R.id.legal_terms_menu_item) {
             Intent intent = new Intent(Dashboard.this, TermsConditions.class);
             startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
//=======================for footer links ===========================================
    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    public void openFacebook(View view) {
    openWebPage("https://www.facebook.com/");
}
    public void openTwitter(View view) {
        openWebPage("https://www.twitter.com/");
    }
    public void openInstagram(View view) {
        openWebPage("https://www.instagram.com/");
    }
    public void openLinkedin(View view) {
        openWebPage("https://www.linkedin.com/");
    }
    //=======================for footer links =======================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkInternetRunnable);
    }
}