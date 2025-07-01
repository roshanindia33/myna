package com.myna.myna.Doc;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myna.myna.R;

public class DevloperInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.devloper_info);

        TextView tvIntro = findViewById(R.id.tv_dev_intro);
        TextView tvDevNameContent = findViewById(R.id.tv_dev_name_content);
        TextView tvTeamNameContent = findViewById(R.id.tv_team_name_content);
        TextView tvLocationContent = findViewById(R.id.tv_location_content);
        TextView tvContactInfoContent = findViewById(R.id.tv_contact_info_content);
        TextView tvExperienceExpertiseContent = findViewById(R.id.tv_experience_expertise_content);
        TextView tvOurApproachContent = findViewById(R.id.tv_our_approach_content);
        TextView tvOurCommitmentContent = findViewById(R.id.tv_our_commitment_content);
        TextView tvConclusionContent = findViewById(R.id.tv_conclusion_content);


        String introHtml = "Behind every reliable app stands a dedicated and honest developer. Our app is more than just technology; it's a <b>vision</b> – meticulously crafted to ensure your investments are <b>secure, transparent, and profitable</b>. Powering this vision is our dedicated team, constantly striving for excellence, backed by years of experience, unwavering hard work, and a deep understanding of user needs.";
        String devNameHtml = "Roshan Kumar";
        String teamNameHtml = "<b>MYNA Developers</b> – An experienced and trusted developer team specializing in FinTech and Android Security Systems.";
        String locationHtml = "<b>Hamirpur, Uttar Pradesh, India</b><br/><br/>We operate from the heart of India, Hamirpur, with the mission to provide a <b>simple, secure, and smart digital platform</b> for investors across the nation.";
        String contactInfoHtml = "Email: <b><a href=\"mailto:myna.helpdesk@gmail.com\">myna.helpdesk@gmail.com</a></b><br/> </b><br/>Working Hours: Monday to Friday (10:00 AM to 6:00 PM)";
        String experienceExpertiseHtml = "The developer boasts <b>5+ years of experience</b> in Android app development, FinTech systems, and secure digital payment platforms. Our team has successfully delivered numerous apps related to investment, digital payments, e-wallets, and financial management.<br/><br/>Our core expertise lies in:<br/><ul><li><b>FinTech Android App Development</b></li><li><b>Firebase, Razorpay, Google Pay API Integration</b></li><li><b>Data Security and User Protection Systems</b></li><li><b>Real-time Transaction and Return Tracking</b></li><li><b>User Login Session Management</b> (Auto Login, Secure Logout)</li><li><b>Investment API Automation with SEBI-Registered Platforms</b></li></ul>";
        String ourApproachHtml = "We believe that when users invest through our app, they entrust us not just with their money, but with their <b>faith and aspirations</b>. Our paramount objective is to uphold this trust with utmost dedication.<br/><br/>We are committed to <b>absolute transparency</b>, ensuring there are <b>no hidden fees</b> and all financial processes are clear and straightforward.";
        String ourCommitmentHtml = "<ul><li>To provide every user with a <b>secure and seamless experience</b></li><li>To offer <b>prompt assistance</b> for any technical concerns</li><li>To continuously <b>innovate and enhance the app</b> by embracing new technologies</li><li>To strictly <b>adhere to government guidelines</b> and financial regulations</li><li>To prioritize <b>user data security</b> above all else</li></ul>";
        String conclusionHtml = "<b>MYNA Plus Developers</b> is more than just a team; we are a <b>pillar of trust</b> upon which thousands of investors are confidently building their financial future.<br/><br/>Should you ever encounter any issues or have suggestions, please do not hesitate to contact us. We are always ready to assist you.";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvIntro.setText(Html.fromHtml(introHtml, Html.FROM_HTML_MODE_COMPACT));
            tvDevNameContent.setText(Html.fromHtml(devNameHtml, Html.FROM_HTML_MODE_COMPACT));
            tvTeamNameContent.setText(Html.fromHtml(teamNameHtml, Html.FROM_HTML_MODE_COMPACT));
            tvLocationContent.setText(Html.fromHtml(locationHtml, Html.FROM_HTML_MODE_COMPACT));
            tvContactInfoContent.setText(Html.fromHtml(contactInfoHtml, Html.FROM_HTML_MODE_COMPACT));
            tvExperienceExpertiseContent.setText(Html.fromHtml(experienceExpertiseHtml, Html.FROM_HTML_MODE_COMPACT));
            tvOurApproachContent.setText(Html.fromHtml(ourApproachHtml, Html.FROM_HTML_MODE_COMPACT));
            tvOurCommitmentContent.setText(Html.fromHtml(ourCommitmentHtml, Html.FROM_HTML_MODE_COMPACT));
            tvConclusionContent.setText(Html.fromHtml(conclusionHtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            // पुराने Android संस्करणों के लिए
            tvIntro.setText(Html.fromHtml(introHtml));
            tvDevNameContent.setText(Html.fromHtml(devNameHtml));
            tvTeamNameContent.setText(Html.fromHtml(teamNameHtml));
            tvLocationContent.setText(Html.fromHtml(locationHtml));
            tvContactInfoContent.setText(Html.fromHtml(contactInfoHtml));
            tvExperienceExpertiseContent.setText(Html.fromHtml(experienceExpertiseHtml));
            tvOurApproachContent.setText(Html.fromHtml(ourApproachHtml));
            tvOurCommitmentContent.setText(Html.fromHtml(ourCommitmentHtml));
            tvConclusionContent.setText(Html.fromHtml(conclusionHtml));
        }

        tvContactInfoContent.setMovementMethod(LinkMovementMethod.getInstance());
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
     * यह विधि फुटर में "Privacy Policy" TextView पर क्लिक करने पर कॉल की जाती है।
     * यह वेब ब्राउज़र में गोपनीयता नीति URL खोलता है।
     */
    public void openPrivacyFromFooter(View view) {
        openPrivacyPolicy();
    }

    /**
     * यह विधि फुटर में "Terms" TextView पर क्लिक करने पर कॉल की जाती है।
     * यह वेब ब्राउज़र में नियम और शर्तें URL खोलता है।
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