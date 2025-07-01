package com.myna.myna.userdata; // अपना सही पैकेज नाम दें

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color; // सीधे कलर के लिए, अगर आपके पास color resource नहीं है
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView; // MaterialButton की जगह TextView इस्तेमाल होगा
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// MaterialButton अब इस्तेमाल नहीं होगा, लेकिन TextInputEditText अभी भी MaterialComponents से है
// import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.myna.myna.Doc.About;
import com.myna.myna.Doc.PrivacyPolicy;
import com.myna.myna.Doc.Support;
import com.myna.myna.Doc.TermsConditions;
import com.myna.myna.R; // सुनिश्चित करें कि यह आपके प्रोजेक्ट के R क्लास को संदर्भित करता है

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WithdrawActivity extends AppCompatActivity {

    private static final String TAG = "WithdrawActivity";

    // UI elements
    private TextView tvInvestedAmount;
    private TextView btnWithdrawAmount; // TextView क्योंकि XML में यही है
    private TextView tvWithdrawalHistoryHeading;
    private RecyclerView rvWithdrawalHistory;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference withdrawRef;

    // User's current invested amount (fetched from Firebase)
    private double investedAmount = 0.0;
    private ValueEventListener investedAmountListener;
    private ValueEventListener withdrawalHistoryListener;

    private WithdrawalHistoryAdapter withdrawalHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.withdraw_request);
        // Initialize UI elements
        ImageButton btnBack = findViewById(R.id.btn_back);
        tvInvestedAmount = findViewById(R.id.tv_invested_amount);
        btnWithdrawAmount = findViewById(R.id.btn_withdraw_amount); // TextView के रूप में इनिशियलाइज़ करें
        tvWithdrawalHistoryHeading = findViewById(R.id.tv_withdrawal_history_heading);
        rvWithdrawalHistory = findViewById(R.id.rv_withdrawal_history);

        // Setup RecyclerView
        rvWithdrawalHistory.setLayoutManager(new LinearLayoutManager(this));
        withdrawalHistoryAdapter = new WithdrawalHistoryAdapter();
        rvWithdrawalHistory.setAdapter(withdrawalHistoryAdapter);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please log in to withdraw funds.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String uid = currentUser.getEmail();
        String realUid = currentUser.getUid();
        String safeEmail = uid.replace("@", "_").replace(".", "_");

        userRef = FirebaseDatabase.getInstance().getReference("users").child(realUid)
                .child("investment");
        withdrawRef = FirebaseDatabase.getInstance().getReference("withdrawals").child(safeEmail);

        // Setup listeners to fetch data
        fetchInvestedAmount();
        fetchWithdrawalHistory();

        // Set click listener for back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Set click listener for the withdraw button
        btnWithdrawAmount.setOnClickListener(v -> {
            if (btnWithdrawAmount.isEnabled()) {
                showWithdrawalDialog();
            } else {
                Toast.makeText(WithdrawActivity.this, "No funds available for withdrawal.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Fetches the user's invested amount from Firebase.
     * Assumes invested amount is stored under "users/{uid}/investedAmount".
     */
    private void fetchInvestedAmount() {
        investedAmountListener = new ValueEventListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Double amount = dataSnapshot.getValue(Double.class);
                    if (amount != null) {
                        investedAmount = amount;
                        tvInvestedAmount.setText(String.format("₹ %.2f", investedAmount));
                        btnWithdrawAmount.setEnabled(investedAmount > 0);
                        if (investedAmount > 0) {
                            btnWithdrawAmount.setAlpha(1.0f);
                        } else {
                            btnWithdrawAmount.setAlpha(0.5f);
                            Toast.makeText(WithdrawActivity.this, "No funds available for withdrawal.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        investedAmount = 0.0;
                        tvInvestedAmount.setText("₹ 0.00");
                        btnWithdrawAmount.setEnabled(false);
                        btnWithdrawAmount.setAlpha(0.5f);
                    }
                } else {
                    investedAmount = 0.0;
                    tvInvestedAmount.setText("₹ 0.00");
                    btnWithdrawAmount.setEnabled(false);
                    btnWithdrawAmount.setAlpha(0.5f);
                }
            }

            @SuppressLint("SetTextI18n") @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read invested amount: " + databaseError.getMessage());
                Toast.makeText(WithdrawActivity.this, "Failed to load invested amount.", Toast.LENGTH_SHORT).show();
                investedAmount = 0.0;
                tvInvestedAmount.setText("₹ 0.00");
                btnWithdrawAmount.setEnabled(false);
                btnWithdrawAmount.setAlpha(0.5f);
            }
        };
        userRef.child("balance").addValueEventListener(investedAmountListener);
    }


    /**
     * Fetches the user's withdrawal history from Firebase.
     * Assumes history is stored under "withdrawals/{uid}/history".
     */
    private void fetchWithdrawalHistory() {
        withdrawalHistoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WithdrawalRequest> requests = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        WithdrawalRequest request = snapshot.getValue(WithdrawalRequest.class);
                        if (request != null) {
                            requests.add(request);
                        }
                    }
                    // Sort requests by timestamp in descending order (newest first)
                    Collections.sort(requests, (r1, r2) -> Long.compare(r2.getTimestamp(), r1.getTimestamp()));
                    withdrawalHistoryAdapter.setWithdrawalList(requests);
                    tvWithdrawalHistoryHeading.setVisibility(View.VISIBLE);
                    rvWithdrawalHistory.setVisibility(View.VISIBLE);
                } else {
                    withdrawalHistoryAdapter.setWithdrawalList(Collections.emptyList());
                    tvWithdrawalHistoryHeading.setVisibility(View.GONE);
                    rvWithdrawalHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WithdrawActivity.this, "Failed to load withdrawal history.", Toast.LENGTH_SHORT).show();
                tvWithdrawalHistoryHeading.setVisibility(View.GONE);
                rvWithdrawalHistory.setVisibility(View.GONE);
            }
        };
        withdrawRef.child("history").addValueEventListener(withdrawalHistoryListener);
    }


    /**
     * Shows a custom dialog to the user for entering the withdrawal amount.
     */
    private void showWithdrawalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_withdraw_amount, null);
        builder.setView(dialogView);

        TextView tvWarning = dialogView.findViewById(R.id.tv_dialog_warning);
        TextInputEditText etWithdrawalAmount = dialogView.findViewById(R.id.et_withdrawal_amount_dialog);

        tvWarning.setText("Important: Please verify your bank account details are up-to-date. You can only withdraw the amount shown in your Invested Wallet (₹ " + String.format("%.2f", investedAmount) + ").");
        etWithdrawalAmount.setHint("Enter amount (e.g., 1000.00)");

        builder.setPositiveButton("Withdraw", (dialog, which) -> {
            String amountStr = etWithdrawalAmount.getText().toString().trim();
            if (amountStr.isEmpty()) {
                Toast.makeText(WithdrawActivity.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            double requestedAmount;
            try {
                requestedAmount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(WithdrawActivity.this, "Invalid amount entered.", Toast.LENGTH_SHORT).show();
                return;
            }

            processWithdrawalRequest(requestedAmount);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Processes the withdrawal request after user enters amount.
     * Performs authentication, validation, and sends data to Firebase.
     * @param requestedAmount The amount user wants to withdraw.
     */
    @SuppressLint("DefaultLocale")
    private void processWithdrawalRequest(double requestedAmount) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "You are not logged in or your session is invalid. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        } else {
            currentUser.getUid();
        }

        if (requestedAmount <= 0) {
            Toast.makeText(this, "Withdrawal amount must be greater than zero.", Toast.LENGTH_SHORT).show();
            sendWithdrawalStatusToFirebase(requestedAmount, "Failed - Zero/Negative Amount");
            return;
        }

        if (requestedAmount > investedAmount) {
            Toast.makeText(this, "Requested amount exceeds your invested balance (₹" + String.format("%.2f", investedAmount) + ").", Toast.LENGTH_LONG).show();
            sendWithdrawalStatusToFirebase(requestedAmount, "Failed - Insufficient Funds");
            return;
        }

        sendWithdrawalStatusToFirebase(requestedAmount, "Pending");
        Toast.makeText(this, "Withdrawal request submitted for review. Status: Pending.", Toast.LENGTH_LONG).show();
    }

    /**
     * Sends withdrawal request details (amount and status) to Firebase.
     * Pushes a new entry for historical tracking.
     * @param amount The amount requested.
     * @param status The status (Pending, Failed, Completed).
     */
    @SuppressLint("DefaultLocale")
    private void sendWithdrawalStatusToFirebase(double amount, String status) {
        Map<String, Object> withdrawalData = new HashMap<>();
        withdrawalData.put("amount", String.format("%.2f", amount));
        withdrawalData.put("status", status);
        withdrawalData.put("timestamp", ServerValue.TIMESTAMP);

        // Push a new entry for historical records
        withdrawRef.child("history").push().setValue(withdrawalData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Withdrawal history entry added successfully."))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add withdrawal history entry.", e));

        // If the withdrawal is successful and pending, decrement the invested amount
        if (status.equals("Pending")) {
            double newInvestedAmount = investedAmount - amount;
            if (newInvestedAmount < 0) newInvestedAmount = 0; // Prevent negative balance

            userRef.child("balance").setValue(newInvestedAmount)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Invested amount decremented."))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to decrement invested amount.", e));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove Firebase listeners to prevent memory leaks
        if (userRef != null && investedAmountListener != null) {
            userRef.child("balance").removeEventListener(investedAmountListener);
        }
        if (withdrawRef != null && withdrawalHistoryListener != null) {
            withdrawRef.child("history").removeEventListener(withdrawalHistoryListener);
        }
    }
}