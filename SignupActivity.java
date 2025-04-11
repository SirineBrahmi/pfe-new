package com.example.pfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity {

    private EditText signupName, signupUsername, signupEmail, signupPassword, signupConfirmPassword;
    private DatabaseReference usersRef;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialisation
        sessionManager = new SessionManager(this);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Liaison des vues
        signupName = findViewById(R.id.signup_name);
        signupUsername = findViewById(R.id.signup_username);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupConfirmPassword = findViewById(R.id.signup_confirm_password);
        Spinner signupRoleSpinner = findViewById(R.id.role_spinner);
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginRedirectText = findViewById(R.id.loginRedirectText);
        CheckBox showPasswordCheckbox = findViewById(R.id.show_password_checkbox);

        // Configuration du spinner des rôles
        String[] roles = {"etudiant", "formateur", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signupRoleSpinner.setAdapter(adapter);

        // Gestion affichage mot de passe
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                signupConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                signupConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            signupPassword.setSelection(signupPassword.getText().length());
            signupConfirmPassword.setSelection(signupConfirmPassword.getText().length());
        });

        // Bouton d'inscription
        signupButton.setOnClickListener(v -> registerUser(signupRoleSpinner.getSelectedItem().toString()));

        // Lien vers login
        loginRedirectText.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }

    private void registerUser(String role) {
        // Récupération des valeurs
        String name = signupName.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String confirmPassword = signupConfirmPassword.getText().toString().trim();

        // Validation
        if (!validateInputs(name, username, email, password, confirmPassword)) {
            Toast.makeText(this, "Veuillez corriger les erreurs ci-dessus", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier si le username existe déjà
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    signupUsername.setError("Ce nom d'utilisateur est déjà pris");
                    return;
                }

                // Hachage du mot de passe
                String hashedPassword = hashPassword(password);

                // Créer l'utilisateur
                String id = usersRef.push().getKey();
                User user = new User(id, username, name, email, hashedPassword, role);

                // Enregistrement dans Firebase
                usersRef.child(id).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            // Sauvegarde session et redirection
                            sessionManager.setLogin(true);
                            sessionManager.setUserId(id);
                            sessionManager.setUsername(username);
                            sessionManager.setRole(role);

                            redirectUser(role);
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(SignupActivity.this, "Erreur d'inscription", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Erreur de base de données", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String name, String username, String email,
                                   String password, String confirmPassword) {
        boolean isValid = true;

        if (name.isEmpty()) {
            signupName.setError("Nom complet requis");
            isValid = false;
        }

        if (username.isEmpty()) {
            signupUsername.setError("Nom d'utilisateur requis");
            isValid = false;
        }

        if (email.isEmpty()) {
            signupEmail.setError("Email requis");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Email invalide");
            isValid = false;
        }

        if (password.isEmpty()) {
            signupPassword.setError("Mot de passe requis");
            isValid = false;
        } else if (password.length() < 6) {
            signupPassword.setError("6 caractères minimum");
            isValid = false;
        }

        if (confirmPassword.isEmpty()) {
            signupConfirmPassword.setError("Confirmation requise");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            signupConfirmPassword.setError("Les mots de passe ne correspondent pas");
            isValid = false;
        }

        return isValid;
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

        Toast.makeText(this, "Inscription réussie!", Toast.LENGTH_SHORT).show();
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
