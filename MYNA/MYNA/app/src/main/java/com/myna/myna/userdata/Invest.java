package com.myna.myna.userdata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.myna.myna.BuildConfig;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.myna.myna.Dashboard;
import com.myna.myna.R;

// Razorpay imports
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Invest extends AppCompatActivity implements PaymentResultListener {

    private EditText amountInput;
    private Button proceedBtn;
    private TextView emailTextView;
    private TextView validationMessage;
    private TextView infoText;
    private TextView footerTrustText;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference usersRef;
    FirebaseUser currentUser; // To store current Firebase user
    String userUId; // To store current user's UID

    private final int MIN_AMOUNT = 500;
    private final int MAX_AMOUNT = 500000;
    private static final String RAZORPAY_KEY_ID = BuildConfig.RAZORPAY_KEY_ID;
//    String keyId = BuildConfig.RAZORPAY_KEY_ID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.invest);

        // --- Razorpay Checkout pre-initialization (Important) ---
        // This pre-initializes the SDK for faster loading on payment initiation.
        Checkout.preload(getApplicationContext());

        // Hide action bar for custom header look
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
//        back button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Invest.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        try {
            FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("email").setValue(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        } catch (Exception e) {
            Toast.makeText(Invest.this, "Internet Connection error!", Toast.LENGTH_LONG).show();
        }

        // Initialize UI elements
        amountInput = findViewById(R.id.amountInput);
        proceedBtn = findViewById(R.id.proceedBtn);
        emailTextView = findViewById(R.id.emailTextView);
        validationMessage = findViewById(R.id.validationMessage);
        infoText = findViewById(R.id.infoText);
        footerTrustText = findViewById(R.id.footer_trust_text);


        // --- HTML Parsing for TextViews ---
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            infoText.setText(Html.fromHtml(infoText.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
            footerTrustText.setText(Html.fromHtml(footerTrustText.getText().toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            infoText.setText(Html.fromHtml(infoText.getText().toString()));
            footerTrustText.setText(Html.fromHtml(footerTrustText.getText().toString()));
        }


        // --- Set User Email / ID ---
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(); // Initialize Firebase Database
        usersRef = mDatabase.getReference("users"); // Reference to your "users" node

        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userUId = currentUser.getUid();// Get current user's UID
            if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                emailTextView.setText(currentUser.getEmail());
            } else {
                emailTextView.setText("User ID: " + userUId); // Fallback if email is null
            }
        } else {
            emailTextView.setText("Guest User (Login for full features)");
            proceedBtn.setEnabled(false); // Disable button for guests
            Toast.makeText(this, "Please log in to proceed with investment.", Toast.LENGTH_LONG).show();
            // Optionally, redirect to login:
            // finish();
            // startActivity(new Intent(this, LoginActivity.class));
        }


        // --- Amount Validation Logic ---
        proceedBtn.setEnabled(false); // Initially disable the button
        validationMessage.setText("Amount must be between ₹" + MIN_AMOUNT + " and ₹" + MAX_AMOUNT + ".");
        validationMessage.setTextColor(ContextCompat.getColor(this, R.color.text_info)); // Default info color

        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int  before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = editable.toString();
                if (inputStr.isEmpty()) {
                    proceedBtn.setEnabled(false);
                    validationMessage.setText("Please enter an amount.");
                    validationMessage.setTextColor(ContextCompat.getColor(Invest.this, R.color.text_info));
                    return;
                }

                try {
                    int amount = Integer.parseInt(inputStr);
                    if (amount < MIN_AMOUNT) {
                        proceedBtn.setEnabled(false);
                        validationMessage.setText("Amount is too low. Minimum: ₹" + MIN_AMOUNT);
                        validationMessage.setTextColor(ContextCompat.getColor(Invest.this, R.color.red_negative)); // Error color
                    } else if (amount > MAX_AMOUNT) {
                        proceedBtn.setEnabled(false);
                        validationMessage.setText("Amount is too high. Maximum: ₹" + MAX_AMOUNT);
                        validationMessage.setTextColor(ContextCompat.getColor(Invest.this, R.color.red_negative)); // Error color
                    } else {
                        // Only enable if user is logged in
                        if (currentUser != null) {
                            proceedBtn.setEnabled(true);
                            validationMessage.setText("Amount looks good! Ready to proceed.");
                            validationMessage.setTextColor(ContextCompat.getColor(Invest.this, R.color.green_positive)); // Success color
                        } else {
                            proceedBtn.setEnabled(false);
                            validationMessage.setText("Please log in to proceed.");
                            validationMessage.setTextColor(ContextCompat.getColor(Invest.this, R.color.red_negative));
                        }
                    }
                } catch (NumberFormatException e) {
                    proceedBtn.setEnabled(false);
                    validationMessage.setText("Invalid amount. Please enter a valid number.");
                    validationMessage.setTextColor(ContextCompat.getColor(Invest.this, R.color.red_negative)); // Error color
                }
            }
        });

        // --- Proceed Button Click Listener (Initiate Razorpay Payment) ---
        proceedBtn.setOnClickListener(view -> {
            // Ensure user is logged in before proceeding
            if (currentUser == null || userUId == null) {
                Toast.makeText(Invest.this, "Please log in to make an investment.", Toast.LENGTH_LONG).show();
                return;
            }

            String amountStr = amountInput.getText().toString();
            if (amountStr.isEmpty()) {
                Toast.makeText(Invest.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                return;
            }
            int amount = Integer.parseInt(amountStr);

            // Basic validation check before initiating payment
            if (amount < MIN_AMOUNT || amount > MAX_AMOUNT) {
                Toast.makeText(Invest.this, "Please enter a valid amount as per limits.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(Invest.this, "Initiating payment for ₹" + amount + "...", Toast.LENGTH_SHORT).show();
            startPayment(amount);
        });
    }

    /**
     * Initiates the Razorpay payment process.
     * @param amount The amount to be paid in actual currency (e.g., 500 for ₹500)
     */
    private void startPayment(int amount) {
        // Amount needs to be in Paisa (Indian currency)
        // E.g., for ₹500, it should be 50000
        int amountInPaisa = amount * 100;

        try {
            Checkout checkout = new Checkout();
            checkout.setKeyID(RAZORPAY_KEY_ID); // Set your Razorpay API Key

            JSONObject options = new JSONObject();
            options.put("name", "MYNA"); // Your app/company name
            options.put("description", "Investment in Myna App");
            options.put("image", R.drawable.ic_insta); // Replace with your app logo URL
            options.put("currency", "INR");
            options.put("amount", amountInPaisa); // Amount in Paisa
            options.put("theme.color", "#3399FF"); // Optional: Customize checkout theme color

            // Pre-fill user's email and contact for faster checkout
            if (currentUser.getEmail() != null) {
                options.put("prefill.email", currentUser.getEmail());
            } else {
                options.put("prefill.email", "guest@example.com"); // Placeholder if email is not available
            }
            // User Phone number - IMPORTANT: You need a way to get user's phone number.
            // For now, I'm using a placeholder. You might get this from Firebase user profile
            // or an EditText on your payment page (which is not in your current XML).
            // If you have a separate EditText for phone number:
            // options.put("prefill.contact", phoneNumberEditText.getText().toString());
            options.put("prefill.contact", "Not Available"); // <<< REPLACE WITH ACTUAL USER PHONE NUMBER or get from input

            // --- Payment Method Specificity (UPI only) ---

            // ✅ Only allow UPI
            JSONObject method = new JSONObject();
            method.put("upi", true);          // ✅ allow UPI
            method.put("card", false);        // ❌ disable cards
            method.put("netbanking", false);  // ❌ disable netbanking
            method.put("wallet", false);      // ❌ disable wallets
            method.put("emi", false);// ❌ disable EMI
            method.put("paylater",false);

            options.put("method", method);


            checkout.open(Invest.this, options);

        } catch (Exception e) {
            Toast.makeText(Invest.this, "Error: Could not start payment. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Callback for successful Razorpay payment.
     * This is where you update Firebase and show success messages.
     */
    @Override
    public void onPaymentSuccess(String s) {
        // 's' is the payment_id (Razorpay order ID)
        Toast.makeText(Invest.this, "Transaction Complete!", Toast.LENGTH_LONG).show();
        Toast.makeText(Invest.this, "Amount will be added to your wallet within 48 to 72 hours.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, Dashboard.class));

        // Get the amount that was paid
        String amountStr = amountInput.getText().toString();
        final double paidAmount = Double.parseDouble(amountStr); // Use double for financial calculations

        // Check if current user and UID are available
        if (currentUser == null || userUId == null) {
            Toast.makeText(Invest.this, "Error: User not logged in, cannot update balance.", Toast.LENGTH_LONG).show();
            return;
        }

        // --- Firebase Update Logic ---
        // 1. Get current balance and add the new amount
        final DatabaseReference userBalanceRef = usersRef.child(userUId).child("investment").child("balance");
        userBalanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double currentBalance = 0;
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        currentBalance = dataSnapshot.getValue(Double.class);
                    } catch (Exception e) {
                        Log.e("Firebase", "Error parsing current balance, defaulting to 0", e);
                    }
                }
                double newBalance = currentBalance + paidAmount;

                // Update balance in Firebase
                userBalanceRef.setValue(newBalance)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Balance updated" + newBalance))
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to update balance: " + e.getMessage()));

                // 2. Add transaction to history
                DatabaseReference transactionHistoryRef = usersRef.child(userUId).child("investment").child("transactionHistory");

                // Get current date and time for transaction history
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());

                // Create a new unique transaction entry
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("amount", paidAmount);
                transaction.put("date", currentDateTime);
                transaction.put("type", "Deposit"); // Or "Investment"
                transaction.put("paymentId", s); // Razorpay Payment ID
                // Add server timestamp for better ordering and accuracy
                transaction.put("timestamp", ServerValue.TIMESTAMP);

                // Use push() to create a unique ID for each transaction
                transactionHistoryRef.push().setValue(transaction)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Transaction history updated successfully."))
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to update transaction history: " + e.getMessage()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read balance: " + databaseError.getMessage());
                Toast.makeText(Invest.this, "Error: Could not update balance. Please contact support.", Toast.LENGTH_LONG).show();
            }
        });

        // Optionally, navigate to Dashboard or show a success screen
        // Intent intent = new Intent(Invest.this, Dashboard.class);
        // startActivity(intent);
        // finish();
    }

    /**
     * Callback for failed Razorpay payment.
     * This is where you show failure messages and do not update Firebase.
     */
    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(Invest.this, "Transaction Failed. Please try again.", Toast.LENGTH_LONG).show();

        // You might want to log the error to Firebase Crashlytics or your own logging system
        // for debugging payment issues.
    }
}