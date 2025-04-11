package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private DatabaseReference usersRef;
    private SessionManager sessionManager;
    private CheckBox showPasswordCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        showPasswordCheckbox = findViewById(R.id.show_password_checkbox);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            redirectUser(sessionManager.getRole());
        }

        loginButton.setOnClickListener(v -> {
            String username = loginUsername.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                loginUsername.setError("Nom d'utilisateur requis");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                loginPassword.setError("Mot de passe requis");
                return;
            }

            authenticateUser(username, password);
        });

        signupRedirectText.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class))
        );

        // Ajout d'un écouteur pour la CheckBox afin de gérer l'affichage du mot de passe
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Si la case est cochée, rendre le mot de passe visible
                loginPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            } else {
                // Si la case est décochée, masquer le mot de passe
                loginPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    private void authenticateUser(String username, String password) {
        Query query = usersRef.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    loginUsername.setError("Utilisateur introuvable");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && user.getPassword().equals(hashPassword(password))) {
                        sessionManager.setLogin(true);
                        sessionManager.setUserId(user.getId());
                        sessionManager.setUsername(user.getUsername());
                        sessionManager.setRole(user.getRole());
                        redirectUser(user.getRole());
                        return;
                    }
                }

                loginPassword.setError("Mot de passe incorrect");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Erreur de base de données", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectUser(String role) {
        Intent intent;
        switch (role) {
            case "admin":
                intent = new Intent(this, AdminActivity.class);
                break;
            case "formateur":
                intent = new Intent(this, ClassRoomFormateur.class);
                break;
            case "etudiant":
            default:
                intent = new Intent(this, EtudiantActivity.class);
                break;
        }
        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // 🔐 Fonction pour hasher le mot de passe
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
