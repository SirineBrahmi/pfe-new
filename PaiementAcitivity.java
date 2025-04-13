package com.example.pfe;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaiementAcitivity extends AppCompatActivity {

    private EditText cardNumber, cardHolderName, securityCode, emailAddress;
    private Spinner monthSpinner, yearSpinner;
    private TextView totalAmount, annuler;
    private Button btnPayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement_acitivity);

        // Initialisation des vues
        cardNumber = findViewById(R.id.cardNumber);
        cardHolderName = findViewById(R.id.cardHolderName);
        securityCode = findViewById(R.id.securityCode);
        emailAddress = findViewById(R.id.emailAddress);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        totalAmount = findViewById(R.id.totalAmount);
        annuler = findViewById(R.id.annuler);
        btnPayer = findViewById(R.id.btnPayer);

        // Exemple de montant
        totalAmount.setText("150.000 TND");

        // Remplissage des spinners
        setupMonthSpinner();
        setupYearSpinner();

        // Action bouton annuler
        annuler.setOnClickListener(view -> {
            Toast.makeText(this, "Paiement annulé", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité
        });

        // Action bouton payer
        btnPayer.setOnClickListener(view -> processPayment());
    }

    private void setupMonthSpinner() {
        String[] months = {
                "01 - Janvier", "02 - Février", "03 - Mars", "04 - Avril", "05 - Mai", "06 - Juin",
                "07 - Juillet", "08 - Août", "09 - Septembre", "10 - Octobre", "11 - Novembre", "12 - Décembre"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthSpinner.setAdapter(adapter);
    }

    private void setupYearSpinner() {
        String[] years = new String[10];
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        yearSpinner.setAdapter(adapter);
    }

    private void processPayment() {
        String cardNum = cardNumber.getText().toString().trim();
        String holderName = cardHolderName.getText().toString().trim();
        String code = securityCode.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String month = monthSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();

        if (cardNum.isEmpty() || holderName.isEmpty() || code.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardNum.length() < 16 || code.length() != 3) {
            Toast.makeText(this, "Numéro de carte ou code de sécurité invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simuler un paiement réussi
        Toast.makeText(this, "Paiement effectué avec succès", Toast.LENGTH_LONG).show();
        // Tu peux ajouter ici l'envoi des données à un serveur ou passer à une autre activité
    }
}
