package com.myna.myna.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration; // For theme check
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler; // For auto-scrolling ViewPager (optional)
import android.os.Looper; // For auto-scrolling ViewPager (optional)
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout; // For quick action buttons
import android.widget.Switch; // For theme toggle
import android.widget.TextView;
import android.widget.Toast; // For quick messages

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate; // For day/night mode
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout; // For ViewPager dots
import com.google.android.material.tabs.TabLayoutMediator; // To link ViewPager and TabLayout

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myna.myna.Dashboard;
import com.myna.myna.Doc.About;
import com.myna.myna.Doc.CopyWriteClaim;
import com.myna.myna.Doc.DevloperInfo;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions; // Existing
import com.myna.myna.Login;
import com.myna.myna.R; // Ensure this is correctly imported
import com.myna.myna.SessionManager;
import com.myna.myna.databinding.FragmentHomeBinding;
import com.myna.myna.servicepage.InvestWithUs; // Existing (Smart Tips)
import com.myna.myna.servicepage.TrustPage;
import com.myna.myna.userdata.Cart2MonthlyInvestment; // Existing
import com.myna.myna.userdata.InvestCenter; // Existing
import com.myna.myna.userdata.News; // Existing
import com.myna.myna.userdata.ReturnHistory; // Existing (User History Pre)
import com.myna.myna.userdata.UserPortfolio; // Existing (User Portfolio)
import com.myna.myna.userdata.WithdrawActivity;

// New Activity Imports (You'll need to create these or map them to existing ones)
// import com.myna.myna.userdata.WithdrawFundsActivity; // Example for new withdraw
// import com.myna.myna.userdata.AddFundsActivity;     // Example for new add funds
// import com.myna.myna.Doc.PrivacyPolicy; // Assuming you have this
// import com.myna.myna.Doc.AboutUsActivity; // Assuming you have this
// import com.myna.myna.Doc.DeveloperInfoActivity; // Assuming you have this
// import com.myna.myna.Doc.InvestmentSuggestionActivity; // Assuming you have this
// import com.myna.myna.Doc.CopyrightClaimsActivity; // Assuming you have this
// import com.myna.myna.Doc.SupportActivity; // Assuming you have this
// import com.myna.myna.Doc.GovtCertifiedInfoActivity; // New for govt cert card


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ViewPager2 opportunityViewPager;
    private TabLayout tabLayoutDots;
    private OpportunityAdapter opportunityAdapter;
    private List<OpportunityAdapter.OpportunityItem> opportunityList;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch themeToggleSwitch;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;


    @SuppressLint("SetTextI18n") @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        ======================update message================================
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String Uid = currentUser.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                    .child(currentUser.getUid()).child("investment");

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("DefaultLocale") @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Double amount = snapshot.child("balance").getValue(Double.class);

                    if (amount != null) {
                        binding.tvPortfolioValue.setText(String.format("₹ %.2f", amount));
                    } else {
                        binding.tvPortfolioValue.setText("₹ 00.00");
                    }
                }

                @SuppressLint("SetTextI18n") @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.tvPortfolioValue.setText("₹ 00.00");
                    Toast.makeText(getContext(), "Please check your Internet connection !", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.tvPortfolioValue.setText("₹ 00.00");
            Toast.makeText(getContext(), "Please check your Internet connection !", Toast.LENGTH_SHORT).show();
        }
        binding.tvTodaysReturn.setText("8.00% ( 10 )");
        binding.tvOverallGrowth.setText("+₹ 2,50.67K (+101.07%)");

        // --- Quick Actions Setup ---
        binding.actionInvestNow.setOnClickListener(this);
        binding.actionWithdraw.setOnClickListener(this);
        binding.actionAddFunds.setOnClickListener(this);
        binding.actionViewHistory.setOnClickListener(this);


        // --- Dynamic Content ViewPager Setup ---
        opportunityViewPager = binding.viewPagerOpportunities;
        tabLayoutDots = binding.tabLayoutDots;
        setupOpportunityViewPager(); // Method to prepare ViewPager
        binding.actionInvestNow.setOnClickListener(this); // Button inside first card (Invest Center)
        binding.userHistoryPre.setOnClickListener(this); // Previous Month Returns Card
        binding.investingSecurities.setOnClickListener(this); // Smart Tips Card
        binding.cardGovCertified.setOnClickListener(this); // Government Certified Card
//      binding.monthlyInvestmentInsight.setOnClickListener(this); // Dashboard cart2 monthly investment insight
        binding.viewPagerOpportunities.setOnClickListener(this); // News Section
//      binding.userPortfolio.setOnClickListener(this); // User Portfolio

        // --- Footer Links Click Listeners ---
        binding.iconFacebook.setOnClickListener(this);
        binding.iconTwitter.setOnClickListener(this);
        binding.iconInstagram.setOnClickListener(this);
        binding.iconLinkedin.setOnClickListener(this);

        binding.linkTerms.setOnClickListener(this);
        binding.linkGovtCert.setOnClickListener(this);
        binding.linkPrivacy.setOnClickListener(this);
        binding.linkAbout.setOnClickListener(this);
        binding.linkDeveloper.setOnClickListener(this);
        binding.linkSuggestion.setOnClickListener(this);
        binding.linkCopyright.setOnClickListener(this);
        binding.linkSupport.setOnClickListener(this);

        return root;
    }

    /**
     * Sets up the ViewPager2 with sample data for opportunities/news.
     */
    private void setupOpportunityViewPager() {
        opportunityList = new ArrayList<>();

        opportunityList.add(new OpportunityAdapter.OpportunityItem(
                getString(R.string.opportunity_title_1),
                getString(R.string.opportunity_desc_1),
                R.drawable.dash_ic_news));
        opportunityList.add(new OpportunityAdapter.OpportunityItem(
                getString(R.string.opportunity_title_2),
                getString(R.string.opportunity_desc_2),
                R.drawable.dash_ic_news2));
        opportunityList.add(new OpportunityAdapter.OpportunityItem(
                getString(R.string.opportunity_title_3),
                getString(R.string.opportunity_desc_3),
                R.drawable.dash_ic_news3));
        opportunityList.add(new OpportunityAdapter.OpportunityItem(
                getString(R.string.opportunity_title_4),
                getString(R.string.opportunity_desc_4),
                R.drawable.dash_ic_news4));

        opportunityAdapter = new OpportunityAdapter(opportunityList);
        opportunityViewPager.setAdapter(opportunityAdapter);
        new TabLayoutMediator(tabLayoutDots, opportunityViewPager,
                (tab, position) -> {
                    // No text needed for the dots, just the indicator
                }
        ).attach();

        // Optional: Auto-scroll the ViewPager for a dynamic feel
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            int currentPosition = 0;
            @Override
            public void run() {
                if (opportunityViewPager == null || opportunityList.isEmpty()) return; // Avoid null pointer
                currentPosition = (currentPosition + 1) % opportunityList.size();
                opportunityViewPager.setCurrentItem(currentPosition, true);
                autoScrollHandler.postDelayed(this, 5000); // Scroll every 5 seconds
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 5000); // Start auto-scroll after 5 seconds
    }
    /**
     * Handles all click events for the fragment's interactive elements.
     * @param v The clicked View.
     */

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        // --- Quick Actions & Main Cards ---
        if (id == R.id.action_invest_now) {
            intent = new Intent(getActivity(), InvestCenter.class);
            startActivity(intent);
        } else if (id == R.id.action_withdraw) {
            Toast.makeText(getContext(), "Initiating Withdrawal Process..", Toast.LENGTH_SHORT).show();
             intent = new Intent(getActivity(), WithdrawActivity.class);
             startActivity(intent);
        } else if (id == R.id.action_add_funds) {
             intent = new Intent(getActivity(), UserPortfolio.class);
             startActivity(intent);
        } else if (id == R.id.action_view_history || id == R.id.user_history_pre) {
            intent = new Intent(getActivity(), ReturnHistory.class);
            startActivity(intent);
        } else if (id == R.id.investing_securities) {
            intent = new Intent(getActivity(), InvestWithUs.class);
            startActivity(intent);
        } else if (id == R.id.cardGovCertified || id == R.id.link_govt_cert) {
            intent = new Intent(getActivity(), TrustPage.class);
            startActivity(intent);
        }
        else if (id == R.id.view_pager_opportunities) {
            Toast.makeText(getContext(), "Checking Latest Investment News...", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), News.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            SessionManager sessionManager = new SessionManager(requireContext());
            sessionManager.clearSession();

            intent = new Intent(requireContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        }

        // --- Footer Links Handling ---
        else if (id == R.id.icon_facebook) {
            openUrl("https://www.facebook.com/myna_app_official"); // Replace with actual URL
        } else if (id == R.id.icon_twitter) {
            openUrl("https://www.twitter.com/myna_app_official"); // Replace with actual URL
        } else if (id == R.id.icon_instagram) {
            openUrl("https://www.instagram.com/myna_app_official"); // Replace with actual URL
        } else if (id == R.id.icon_linkedin) {
            openUrl("https://www.linkedin.com/company/myna_app_official"); // Replace with actual URL
        } else if (id == R.id.link_terms) {
            Toast.makeText(getContext(), "Opening Terms & Conditions...", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), TermsConditions.class);
            startActivity(intent);
        } else if (id == R.id.link_privacy) {
            Toast.makeText(getContext(), "Privacy Policy", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), PrivacyPolicy.class);
            startActivity(intent);
        } else if (id == R.id.link_about) {
            intent = new Intent(getActivity(), About.class);
            startActivity(intent);
        } else if (id == R.id.link_developer) {
            intent = new Intent(getActivity(), DevloperInfo.class);
            startActivity(intent);
        } else if (id == R.id.link_suggestion) {
            Toast.makeText(getContext(), "Investment Suggestions", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), InvestWithUs.class);
            startActivity(intent);
        } else if (id == R.id.link_copyright) {
            Toast.makeText(getContext(), "Copyright Claims", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), CopyWriteClaim.class);
            startActivity(intent);
        } else if (id == R.id.link_support) {
            Toast.makeText(getContext(), "Accessing Support", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), Support.class);
            startActivity(intent);
        }
    }

    /**
     * Helper method to open a URL in a browser.
     * @param url The URL to open.
     */
    private void openUrl(String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Could not open link. Please check your internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding to prevent memory leaks

        // Remove callbacks to prevent memory leaks if auto-scrolling was active
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }
}