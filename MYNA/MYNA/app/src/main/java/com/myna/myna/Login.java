package com.myna.myna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton, resendEmailButton_log;
    TextView signupRedirect;
    FirebaseAuth auth;
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
        setContentView(R.layout.login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialization
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupRedirect = findViewById(R.id.signup_redirect);
        resendEmailButton_log = findViewById(R.id.resendEmailButton_log);


        resendEmailButton_log.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accepted = prefs.getBoolean("termsAccepted", false);

        // session file checked
        if (sessionManager.isLoggedIn()) {
            goToDashboard();
            return;
        }

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // खाली फ़ील्ड चेक
            if (email.isEmpty()) {
                emailInput.setError("Empty Email ");
                emailInput.requestFocus();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please Enter valid Email ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                passwordInput.setError("Empty Password");
                passwordInput.requestFocus();
                return;
            }

            // Login Attempt
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.reload().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (user.isEmailVerified()) {
                                        sessionManager.saveSession(user.getUid(), user.getEmail());
                                        goToDashboard();
                                    } else {
                                        Toast.makeText(this, "Please Verify Your Email ", Toast.LENGTH_SHORT).show();
                                        loginButton.setVisibility(View.GONE);
                                        resendEmailButton_log.setVisibility(View.VISIBLE);
                                        resendEmailButton_log.setOnClickListener(view -> {
                                            user.sendEmailVerification();
                                            Toast.makeText(Login.this, "Verification mail has been sent ", Toast.LENGTH_SHORT).show();


                                            user.reload().addOnSuccessListener(unused -> {
                                                if (user.isEmailVerified()) {
                                                    sessionManager.saveSession(user.getUid(), user.getEmail());
                                                    goToDashboard();
                                                } else {
                                                    Toast.makeText(Login.this, "Please Verify Your Email ", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });
                                        auth.signOut();
                                    }
                                } else {
                                    Toast.makeText(this, "Failed to loading user data ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Wrong User ID or Password ", Toast.LENGTH_SHORT).show();
                    });
        });

        signupRedirect.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Signup.class));
        });
    }

    private void goToDashboard() {
        if (!accepted) {
            // Show warning screen
            startActivity(new Intent(Login.this, WarningTC.class));
            finish();
        } else {
            // Go to Dashboard
            startActivity(new Intent(Login.this, Dashboard.class));
            finish();
        }
    }
}
