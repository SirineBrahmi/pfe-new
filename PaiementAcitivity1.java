package com.example.pfe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class PaiementAcitivity extends AppCompatActivity {

    Spinner monthSpinner, yearSpinner;
    TextView totalAmount;
    Button btnPayer;
    EditText cardNumber, cardHolderName, securityCode, emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement_acitivity);

        // Initialisation des vues
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        totalAmount = findViewById(R.id.totalAmount);
        btnPayer = findViewById(R.id.btnPayer);
        cardNumber = findViewById(R.id.cardNumber);
        cardHolderName = findViewById(R.id.cardHolderName);
        securityCode = findViewById(R.id.securityCode);
        emailAddress = findViewById(R.id.emailAddress);

        // Remplir les mois
        String[] months = {
                "01 - Janvier", "02 - Février", "03 - Mars", "04 - Avril",
                "05 - Mai", "06 - Juin", "07 - Juillet", "08 - Août",
                "09 - Septembre", "10 - Octobre", "11 - Novembre", "12 - Décembre"
        };
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Remplir les années (à partir de l'année courante)
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(currentYear + i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Affichage total
        totalAmount.setText("300.000 TND");

        // Bouton payer
        btnPayer.setOnClickListener(v -> {
            // Tu peux ici ajouter la logique de validation ou d'envoi des données
        });
    }
}
