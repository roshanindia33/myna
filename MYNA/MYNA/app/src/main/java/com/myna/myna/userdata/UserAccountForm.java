package com.myna.myna.userdata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog; // Import for AlertDialog

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater; // Import for LayoutInflater
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myna.myna.R;

import java.util.HashMap;

public class UserAccountForm extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText accountHolderName, accountNumber, ifscCode, bankName, branch, mobileNumber, upiId, panNumber, aadhaarNumber;
    private Button saveButton;
    private TextView userEmailTextView; // Renamed for clarity

    @SuppressLint("SetTextI18n") // Added for string formatting in TextViews
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Hide action bar for custom header
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_account_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        userEmailTextView = findViewById(R.id.userEmail); // Initialize the TextView
        accountHolderName = findViewById(R.id.accountHolderName);
        accountNumber = findViewById(R.id.accountNumber);
        ifscCode = findViewById(R.id.ifscCode);
        bankName = findViewById(R.id.bankName);
        branch = findViewById(R.id.branch);
        mobileNumber = findViewById(R.id.mobileNumber);
        upiId = findViewById(R.id.upiId);
        panNumber = findViewById(R.id.panNumber);
        aadhaarNumber = findViewById(R.id.aadhaarNumber);
        saveButton = findViewById(R.id.save_bank_detail);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set user email on the form
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
            userEmailTextView.setText(currentUser.getEmail());
            // Pre-fill fields if data exists in Firestore
            loadBankDetailsForEditing(currentUser.getEmail());
        } else {
            userEmailTextView.setText("Email Not Available");
            Toast.makeText(this, "User not logged in or email not found.", Toast.LENGTH_LONG).show();
            // Optionally, redirect to login if no user
            // startActivity(new Intent(this, LoginActivity.class));
            // finish();
        }

        saveButton.setOnClickListener(v -> {
            // Get data from EditTexts
            String holderName = accountHolderName.getText().toString().trim();
            String accountNo = accountNumber.getText().toString().trim();
            String ifsc = ifscCode.getText().toString().trim();
            String bank = bankName.getText().toString().trim();
            String branchName = branch.getText().toString().trim();
            String mobile = mobileNumber.getText().toString().trim();
            String upi = upiId.getText().toString().trim();
            String pan = panNumber.getText().toString().trim();
            String aadhaar = aadhaarNumber.getText().toString().trim();

            // Basic validation
            if (holderName.isEmpty() || accountNo.isEmpty() || ifsc.isEmpty() || bank.isEmpty() || branchName.isEmpty() || mobile.isEmpty() || upi.isEmpty() || pan.isEmpty() || aadhaar.isEmpty()) {
                Toast.makeText(UserAccountForm.this, "Please fill in all the required fields.", Toast.LENGTH_LONG).show();
                return;
            }

            // Show confirmation dialog
            showConfirmationDialog(holderName, accountNo, ifsc, bank, branchName, mobile, upi, pan, aadhaar);
        });
    }

    private void loadBankDetailsForEditing(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("bankAccount")) {
                        // Pre-fill fields if data exists
                        accountHolderName.setText(documentSnapshot.getString("bankAccount.accountHolderName"));
                        accountNumber.setText(documentSnapshot.getString("bankAccount.accountNumber"));
                        ifscCode.setText(documentSnapshot.getString("bankAccount.ifscCode"));
                        bankName.setText(documentSnapshot.getString("bankAccount.bankName"));
                        branch.setText(documentSnapshot.getString("bankAccount.branch"));
                        mobileNumber.setText(documentSnapshot.getString("bankAccount.mobileNumber"));
                        upiId.setText(documentSnapshot.getString("bankAccount.upiId"));
                        panNumber.setText(documentSnapshot.getString("bankAccount.panNumber"));
                        aadhaarNumber.setText(documentSnapshot.getString("bankAccount.aadhaarNumber"));
                        Toast.makeText(UserAccountForm.this, "Existing details loaded.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserAccountForm.this, "No existing details found. Please enter.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserAccountForm.this, "Error loading details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("SetTextI18n")
    private void showConfirmationDialog(String holderName, String accountNo, String ifsc, String bank, String branchName, String mobile, String upi, String pan, String aadhaar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountForm.this, R.style.CustomAlertDialogTheme); // Apply custom theme
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_bank_details_confirm, null); // Custom dialog layout
        builder.setView(dialogView);

        TextView tvAccountHolderName = dialogView.findViewById(R.id.dialogAccountHolderName);
        TextView tvAccountNumber = dialogView.findViewById(R.id.dialogAccountNumber);
        TextView tvIfscCode = dialogView.findViewById(R.id.dialogIfscCode);
        TextView tvBankName = dialogView.findViewById(R.id.dialogBankName);
        TextView tvBranch = dialogView.findViewById(R.id.dialogBranch);
        TextView tvMobileNumber = dialogView.findViewById(R.id.dialogMobileNumber);
        TextView tvUpiId = dialogView.findViewById(R.id.dialogUpiId);
        TextView tvPanNumber = dialogView.findViewById(R.id.dialogPanNumber);
        TextView tvAadhaarNumber = dialogView.findViewById(R.id.dialogAadhaarNumber);

        Button confirmButton = dialogView.findViewById(R.id.dialogConfirmButton);
        Button editButton = dialogView.findViewById(R.id.dialogEditButton);

        // Set the text for confirmation
        tvAccountHolderName.setText("Account Holder: " + holderName);
        tvAccountNumber.setText("Account Number: " + accountNo);
        tvIfscCode.setText("IFSC Code: " + ifsc);
        tvBankName.setText("Bank Name: " + bank);
        tvBranch.setText("Branch: " + branchName);
        tvMobileNumber.setText("Mobile Number: " + mobile);
        tvUpiId.setText("UPI ID: " + upi);
        tvPanNumber.setText("PAN Number: " + pan);
        tvAadhaarNumber.setText("Aadhaar Number: " + aadhaar);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Make dialog non-cancelable by touch outside

        confirmButton.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss dialog
            saveBankDetailsToFirestore(holderName, accountNo, ifsc, bank, branchName, mobile, upi, pan, aadhaar);
        });

        editButton.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss dialog
            Toast.makeText(UserAccountForm.this, "Please review your details.", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void saveBankDetailsToFirestore(String holderName, String accountNo, String ifsc, String bank, String branchName, String mobile, String upi, String pan, String aadhaar) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(UserAccountForm.this, "User not logged in. Cannot save data.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getEmail(); // Using email as user ID for Firestore document

        BankAccount bankAccount = new BankAccount(holderName, accountNo, ifsc, bank, branchName, mobile, upi, pan, aadhaar);

        db.collection("users").document(userId)
                .set(new HashMap<String, Object>() {{
                    put("bankAccount", bankAccount);
                }})
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserAccountForm.this, "Bank Details Saved Successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserAccountForm.this, UserAccount.class);
                    // Clear the back stack to prevent returning to the form with back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Finish current activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserAccountForm.this, "Failed to save bank details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}