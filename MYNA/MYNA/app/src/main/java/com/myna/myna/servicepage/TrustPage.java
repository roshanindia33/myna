package com.myna.myna.servicepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R;

public class TrustPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.trust_page);

        TextView tvHowWeWork = findViewById(R.id.tv_how_we_work_content);
        TextView tvRazorpaySecurity = findViewById(R.id.tv_razorpay_security_content);
        TextView tvCertifiedApps = findViewById(R.id.tv_certified_apps_content);
        TextView tvOurGoal = findViewById(R.id.tv_our_goal_content);
        TextView tvTransparency = findViewById(R.id.tv_transparency_content);
        TextView tvWhyTrustUs = findViewById(R.id.tv_why_trust_us_content);
        TextView tvConclusion = findViewById(R.id.tv_conclusion_content);

        String htmlHowWeWork = "Our investment approach fuses <b>cutting-edge technology</b> with <b>trustworthy financial strategies</b>. We seamlessly integrate with government-certified platforms like <b>Groww</b>, which has already garnered the unwavering trust of millions of investors. Every investment you make is meticulously distributed into <b>certified investment plans</b>, with returns directly credited to your account. Our stringent processes eliminate any scope for fraud, ensuring <b>complete security and control</b> at every step.";
        String htmlRazorpaySecurity = "We recognize the immense value of your hard-earned money. That’s why we’ve partnered with <b>Razorpay</b>, India’s most secure and trusted payment gateway, for all transactions. Every single transaction is fortified with <b>256-bit SSL encryption</b>, guaranteeing that your sensitive information and funds remain absolutely safe. Whether you invest ₹100 or ₹1 lakh, your capital enjoys the <b>highest level of protection</b>.";
        String htmlCertifiedApps = "Our application exclusively utilizes third-party services that are <b>fully approved by SEBI</b> (Securities and Exchange Board of India) and <b>RBI</b> (Reserve Bank of India). We deploy a robust combination of the following certified tools and platforms:\n\n<ul>\n<li><b>Groww:</b> For seamless stock and mutual fund investments.</li>\n<li><b>Razorpay:</b> Our secure and trusted payment gateway.</li>\n<li><b>Zerodha or Upstox APIs:</b> (If applicable for advanced trading functionalities).</li>\n<li><b>Firebase &amp; Other Cloud Platforms:</b> For impregnable data storage and reliability.</li>\n</ul>\nThis powerful synergy not only <b>safeguards your capital</b> but also ensures its strategic investment for <b>maximum, reliable returns</b>.";
        String htmlOurGoal = "Our dedicated team comprises <b>seasoned finance and technology professionals</b> with a profound understanding of investment strategies. Our ultimate objective is to ensure your invested funds experience <b>consistent, steady growth</b>, leading to your absolute satisfaction month after month, year after year. We meticulously analyze and rigorously review diverse investment plans, continuously seeking the most advantageous options. Our policies are unequivocally <b>user-centric</b>, driven by a commitment to deliver only what is unequivocally best for you.";
        String htmlTransparency = "Rest assured, our app operates with <b>absolute transparency</b>. There are <b>no hidden fees</b> or unwelcome deductions. Every investment, every transaction, and every return is <b>crystal clear and readily visible</b> within the app. All figures and intricate details are meticulously updated in <b>real-time</b>, empowering you with complete awareness of your money's status at every single moment.";
        String htmlWhyTrustUs = "<ul>\n<li><b>We have proudly earned the trust of thousands of users</b> who consistently invest through our platform.</li>\n<li>We strictly adhere to <b>all government-approved rules and financial regulations</b>, ensuring utmost compliance.</li>\n<li>Our dedicated technical team is relentless in its pursuit of <b>enhancing app security and performance</b>.</li>\n<li>Our responsive user support team is <b>always ready to assist you</b> whenever you need guidance or help.</li>\n</ul>";
        String htmlConclusion = "Seeking an investment platform that blends impenetrable security with unwavering transparency and delivers consistent, reliable returns? Your search ends here. Our app is precisely what you need: <b>Investment simplified, secured, and profitable.</b>";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvHowWeWork.setText(Html.fromHtml(htmlHowWeWork, Html.FROM_HTML_MODE_COMPACT));
            tvRazorpaySecurity.setText(Html.fromHtml(htmlRazorpaySecurity, Html.FROM_HTML_MODE_COMPACT));
            tvCertifiedApps.setText(Html.fromHtml(htmlCertifiedApps, Html.FROM_HTML_MODE_COMPACT));
            tvOurGoal.setText(Html.fromHtml(htmlOurGoal, Html.FROM_HTML_MODE_COMPACT));
            tvTransparency.setText(Html.fromHtml(htmlTransparency, Html.FROM_HTML_MODE_COMPACT));
            tvWhyTrustUs.setText(Html.fromHtml(htmlWhyTrustUs, Html.FROM_HTML_MODE_COMPACT));
            tvConclusion.setText(Html.fromHtml(htmlConclusion, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvHowWeWork.setText(Html.fromHtml(htmlHowWeWork));
            tvRazorpaySecurity.setText(Html.fromHtml(htmlRazorpaySecurity));
            tvCertifiedApps.setText(Html.fromHtml(htmlCertifiedApps));
            tvOurGoal.setText(Html.fromHtml(htmlOurGoal));
            tvTransparency.setText(Html.fromHtml(htmlTransparency));
            tvWhyTrustUs.setText(Html.fromHtml(htmlWhyTrustUs));
            tvConclusion.setText(Html.fromHtml(htmlConclusion));
        }

        findViewById(R.id.footer_privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacyPolicy();
            }
        });

        findViewById(R.id.footer_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTermsAndConditions();
            }
        });
    }

    /**
     * This method is called when the "Privacy Policy" TextView in the footer is clicked.
     * It opens the Privacy Policy URL in a web browser.
     * @param view The clicked TextView (optional, but good practice for onClick XML attributes)
     */
    public void openPrivacyFromFooter(View view) {
        openPrivacyPolicy();
    }

    /**
     * This method is called when the "Terms" TextView in the footer is clicked.
     * It opens the Terms and Conditions URL in a web browser.
     * @param view The clicked TextView (optional, but good practice for onClick XML attributes)
     */
    public void openTermsFromFooter(View view) {
        openTermsAndConditions();
    }
    private void openPrivacyPolicy() {
        Intent intent = new Intent(this, PrivacyPolicy.class);
        startActivity(intent);
    }
    private void openTermsAndConditions() {
        Intent intent = new Intent(this, TermsConditions.class);
        startActivity(intent);
    }

}