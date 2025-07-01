package com.myna.myna.userdata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myna.myna.R;

public class UserAccount extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView userEmail, addBankAccountLink, accountHolderName, accountNumber, ifscCode, bankName, branch, mobileNumber, upiId, panNumber, aadhaarNumber;
    private CardView cardBankInfo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Hide action bar for custom header look
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toast.makeText(UserAccount.this, "Loading account details...", Toast.LENGTH_SHORT).show(); // More user-friendly message
        // setTitle("Bank Details"); // Not needed if action bar is hidden and you have a custom one

        // Initialize Views
        userEmail = findViewById(R.id.userEmail);
        addBankAccountLink = findViewById(R.id.addBankAccountLink);
        accountHolderName = findViewById(R.id.accountHolderName);
        accountNumber = findViewById(R.id.accountNumber);
        ifscCode = findViewById(R.id.ifscCode);
        bankName = findViewById(R.id.bankName);
        branch = findViewById(R.id.branch);
        mobileNumber = findViewById(R.id.mobileNumber);
        upiId = findViewById(R.id.upiId);
        panNumber = findViewById(R.id.panNumber);
        aadhaarNumber = findViewById(R.id.aadhaarNumber);
        cardBankInfo = findViewById(R.id.cardBankInfo);

        // Initialize Firebase Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get current user's email as userId
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(UserAccount.this, "User is not logged in. Please log in.", Toast.LENGTH_LONG).show(); // More informative
            // Optionally, redirect to login activity
            // startActivity(new Intent(UserAccount.this, LoginActivity.class));
            finish(); // Close this activity
            return;
        }
        String userId = user.getEmail(); // Email used as userId
        if (userId != null && !userId.isEmpty()) {
            userEmail.setText(userId);
        } else {
            userEmail.setText("Email Not Available"); // Fallback if email is null/empty for some reason
        }


        // Fetch data from Firebase Firestore for the given userId
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Check if bankAccount exists and update UI accordingly
                if (documentSnapshot.contains("bankAccount")) {
                    cardBankInfo.setVisibility(View.VISIBLE);
                    addBankAccountLink.setVisibility(View.INVISIBLE); // Keep add/update visible for editing

                    // Retrieve and set bank account details
                    // Use ternary operator to handle potential null values gracefully
                    String bankHolderName = documentSnapshot.getString("bankAccount.accountHolderName");
                    String bankAccountNumber = documentSnapshot.getString("bankAccount.accountNumber");
                    String bankIfscCode = documentSnapshot.getString("bankAccount.ifscCode");
                    String bankNameText = documentSnapshot.getString("bankAccount.bankName");
                    String branchText = documentSnapshot.getString("bankAccount.branch");
                    String mobileNumberText = documentSnapshot.getString("bankAccount.mobileNumber");
                    String upiIdText = documentSnapshot.getString("bankAccount.upiId");
                    String panNumberText = documentSnapshot.getString("bankAccount.panNumber");
                    String aadhaarNumberText = documentSnapshot.getString("bankAccount.aadhaarNumber");

                    // Set bank details to TextViews, provide default if null
                    accountHolderName.setText("Account Holder: " + (bankHolderName != null ? bankHolderName : "N/A"));
                    accountNumber.setText("Account Number: " + (bankAccountNumber != null ? bankAccountNumber : "N/A"));
                    ifscCode.setText("IFSC Code: " + (bankIfscCode != null ? bankIfscCode : "N/A"));
                    bankName.setText("Bank Name: " + (bankNameText != null ? bankNameText : "N/A"));
                    branch.setText("Branch: " + (branchText != null ? branchText : "N/A"));
                    mobileNumber.setText("Mobile Number: " + (mobileNumberText != null ? mobileNumberText : "N/A"));
                    upiId.setText("UPI ID: " + (upiIdText != null ? upiIdText : "N/A"));
                    panNumber.setText("PAN Number: " + (panNumberText != null ? panNumberText : "N/A"));
                    aadhaarNumber.setText("Aadhaar Number: " + (aadhaarNumberText != null ? aadhaarNumberText : "N/A"));

                } else {
                    // If no bank account info exists, show the link to add a bank account
                    addBankAccountLink.setVisibility(View.VISIBLE);
                    cardBankInfo.setVisibility(View.GONE);
                }
            } else {
                // User data not found, likely first time login, show add bank account link
                addBankAccountLink.setVisibility(View.VISIBLE);
                cardBankInfo.setVisibility(View.GONE);
                Toast.makeText(UserAccount.this, "No bank details found. Please add your account.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(UserAccount.this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // In case of error, still allow adding bank details
            addBankAccountLink.setVisibility(View.VISIBLE);
            cardBankInfo.setVisibility(View.GONE);
        });

        // Set click listener for Add/Update Bank Account link
        addBankAccountLink.setOnClickListener(v -> {
            Intent intent = new Intent(UserAccount.this, UserAccountForm.class);
            startActivity(intent);
        });
    }
}