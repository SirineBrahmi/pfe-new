package com.example.pfe;

import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText etNom, etEmail, etPassword, etConfirmPassword, etAdresse, etNumTel;
    private Spinner spinnerRole, spinnerDynamic;
    private Button btnCreerCompte;
    private LinearLayout dynamicSpinnerContainer;
    private String selectedNiveau = "";
    private String selectedSpecialite = "";
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("utilisateurs");

        initializeViews();
        setupRoleSpinner();

        btnCreerCompte.setOnClickListener(v -> {
            if (validateForm()) {
                createAccount();
            }
        });
    }

    private void initializeViews() {
        etNom = findViewById(R.id.et_nom);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.signup_password);
        etConfirmPassword = findViewById(R.id.signup_confirm_password);
        etAdresse = findViewById(R.id.et_adresse);
        etNumTel = findViewById(R.id.et_telephone);
        spinnerRole = findViewById(R.id.role_spinner);
        btnCreerCompte = findViewById(R.id.btn_creer_compte);
        dynamicSpinnerContainer = findViewById(R.id.dynamic_spinner_container);

        // Création du spinner dynamique
        spinnerDynamic = new Spinner(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 16, 0, 0);
        spinnerDynamic.setLayoutParams(params);
        dynamicSpinnerContainer.addView(spinnerDynamic);
        dynamicSpinnerContainer.setVisibility(View.GONE);

        // Checkbox pour afficher le mot de passe
        CheckBox showPasswordCheckbox = findViewById(R.id.show_password_checkbox);
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etPassword.setSelection(etPassword.getText().length());
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });
    }

    private void setupRoleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String role = parent.getItemAtPosition(position).toString();
                updateDynamicSpinner(role);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dynamicSpinnerContainer.setVisibility(View.GONE);
            }
        });
    }

    private void updateDynamicSpinner(String role) {
        if (role.equalsIgnoreCase("Étudiant")) {
            setupNiveauSpinner();
            dynamicSpinnerContainer.setVisibility(View.VISIBLE);
        } else if (role.equalsIgnoreCase("Formateur")) {
            setupSpecialiteSpinner();
            dynamicSpinnerContainer.setVisibility(View.VISIBLE);
        } else {
            dynamicSpinnerContainer.setVisibility(View.GONE);
        }
    }

    private void setupNiveauSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.niveau_etudiant_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDynamic.setAdapter(adapter);

        spinnerDynamic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedNiveau = parent.getItemAtPosition(position).toString();
                selectedSpecialite = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSpecialiteSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialite_formateur_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDynamic.setAdapter(adapter);

        spinnerDynamic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpecialite = parent.getItemAtPosition(position).toString();
                selectedNiveau = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        if (etNom.getText().toString().trim().isEmpty()) {
            etNom.setError("Nom requis");
            valid = false;
        }

        String email = etEmail.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email valide requis");
            valid = false;
        }

        if (etNumTel.getText().toString().trim().isEmpty()) {
            etNumTel.setError("Téléphone requis");
            valid = false;
        }

        if (etAdresse.getText().toString().trim().isEmpty()) {
            etAdresse.setError("Adresse requise");
            valid = false;
        }

        String password = etPassword.getText().toString().trim();
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Mot de passe (min 6 caractères)");
            valid = false;
        }

        if (!etConfirmPassword.getText().toString().equals(password)) {
            etConfirmPassword.setError("Les mots de passe ne correspondent pas");
            valid = false;
        }

        String role = spinnerRole.getSelectedItem().toString();
        if (role.equalsIgnoreCase("Étudiant") && selectedNiveau.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner un niveau", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (role.equalsIgnoreCase("Formateur") && selectedSpecialite.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner une spécialité", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void createAccount() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserData(firebaseUser);
                        }
                    } else {
                        Toast.makeText(SignupActivity.this,
                                "Erreur d'inscription: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(FirebaseUser firebaseUser) {
        String id = firebaseUser.getUid();
        String nom = etNom.getText().toString().trim();
        String email = firebaseUser.getEmail();
        String motDePasse = etPassword.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();
        String numTel = etNumTel.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString().toLowerCase();

        Utilisateur utilisateur = new Utilisateur(id, nom, email, motDePasse, adresse, numTel, role, selectedNiveau, selectedSpecialite);

        if (role.equalsIgnoreCase("étudiant") || role.equalsIgnoreCase("etudiant")) {
            String idFormation = "FORMATION_001";
            Inscription inscription = new Inscription(id, idFormation);
            inscription.enregistrer();
        }

        usersRef.child(role).child(id).setValue(utilisateur)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "✅ Inscription réussie", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(SignupActivity.this,
                        "❌ Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
