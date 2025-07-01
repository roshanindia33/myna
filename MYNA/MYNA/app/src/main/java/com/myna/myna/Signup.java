package com.myna.myna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button signupBtn, resendEmailButton, verifyNowButton;
    TextView loginLink;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    SessionManager sessionManager;
    SharedPreferences prefs;
    boolean accepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI elements
        editTextName = findViewById(R.id.signupName);
        editTextEmail = findViewById(R.id.signupEmail);
        editTextPassword = findViewById(R.id.signupPassword);
        editTextConfirmPassword = findViewById(R.id.signupConfirmPassword);
        signupBtn = findViewById(R.id.signupBtn);
        loginLink = findViewById(R.id.loginLink);
        resendEmailButton = findViewById(R.id.resendEmailButton);
        verifyNowButton = findViewById(R.id.verifyNowButton);


        resendEmailButton.setVisibility(View.GONE);
        verifyNowButton.setVisibility(View.GONE);
        signupBtn.setVisibility(View.VISIBLE);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        sessionManager = new SessionManager(this);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accepted = prefs.getBoolean("termsAccepted", false);

        signupBtn.setOnClickListener(v -> registerUser());

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, Login.class));
            finish();
        });
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please Fill All the Blanks ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please Enter valid Email ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password does not match ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be greater than 6 letter ", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

            if (!isNewUser) {
                Toast.makeText(this, "Email Already Register , Please Login ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Signup.this, Login.class));
                finish();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(createTask -> {
                            if (createTask.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                new android.os.Handler().postDelayed(() -> {
                                    if (user != null && !user.isEmailVerified()) {
                                        user.sendEmailVerification()
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(Signup.this, "Verification mail has been sent ", Toast.LENGTH_SHORT).show();
                                                    resendEmailButton.setVisibility(View.VISIBLE); // ✅ Show this
                                                    verifyNowButton.setVisibility(View.VISIBLE);   // ✅ And this too
                                                    signupBtn.setVisibility(View.GONE);
                                                    resendClarification();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(this, "There was a problem sending the verification code: " + e.getMessage(), Toast.LENGTH_LONG).show());
                                    }
                                }, 1000); // Send email after 1 second

                                // Auto check verification and go to dashboard
                                verifyNowButton.setOnClickListener(view -> {
                                    assert user != null;
                                    user.reload().addOnSuccessListener(unused -> {
                                        if (user.isEmailVerified()) {
                                            sessionManager.saveSession(name, email);
                                            if (!accepted) {
                                                // Show warning screen
                                                startActivity(new Intent(Signup.this, WarningTC.class));
                                                finish();
                                            } else {
                                                // Go to Dashboard
                                                startActivity(new Intent(Signup.this, Dashboard.class));
                                                finish();
                                            }
                                        } else {
                                            Toast.makeText(Signup.this, "Please Verify Your Email ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                });
                            } else {
                                Exception e = createTask.getException();
                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(this, "Email Already Register, Please Login ", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Signup.this, Login.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    public void resendClarification(){

        resendEmailButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && !user.isEmailVerified()) {
                user.sendEmailVerification()
                        .addOnSuccessListener(aVoid -> Toast.makeText(Signup.this, "Email Verification Link Send Again...", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(Signup.this, "Failed to sending a verification link: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }



}
