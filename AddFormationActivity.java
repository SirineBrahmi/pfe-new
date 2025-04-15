package com.example.pfe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddFormationActivity extends AppCompatActivity {

    private EditText etNom, etDescription, etDuree, etPrix, etDateDebut, etDateFin;
    private Button btnAjouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_formation);

        // Initialisation des vues
        etNom = findViewById(R.id.et_nom_formation);
        etDescription = findViewById(R.id.et_description);
        etDuree = findViewById(R.id.et_duree);
        etPrix = findViewById(R.id.et_prix);
        etDateDebut = findViewById(R.id.et_date_debut);
        etDateFin = findViewById(R.id.et_date_fin);
        btnAjouter = findViewById(R.id.btn_ajouter_formation);

        // Définir la date du jour par défaut
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        etDateDebut.setText(today);

        btnAjouter.setOnClickListener(v -> ajouterFormation());
    }

    private void ajouterFormation() {
        // Validation des champs
        String nom = etNom.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dureeStr = etDuree.getText().toString().trim();
        String prixStr = etPrix.getText().toString().trim();
        String dateDebut = etDateDebut.getText().toString().trim();
        String dateFin = etDateFin.getText().toString().trim();

        if (nom.isEmpty()) {
            etNom.setError("Nom requis");
            return;
        }

        if (description.isEmpty()) {
            etDescription.setError("Description requise");
            return;
        }

        int duree;
        try {
            duree = Integer.parseInt(dureeStr);
            if (duree <= 0) {
                etDuree.setError("Durée invalide");
                return;
            }
        } catch (NumberFormatException e) {
            etDuree.setError("Nombre requis");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                etPrix.setError("Prix invalide");
                return;
            }
        } catch (NumberFormatException e) {
            etPrix.setError("Nombre requis");
            return;
        }

        if (dateDebut.isEmpty() || dateFin.isEmpty()) {
            Toast.makeText(this, "Les dates sont requises", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de la formation
        String id = UUID.randomUUID().toString();
        Formation formation = new Formation(id, nom, description, duree, prix, dateDebut, dateFin);

        // Enregistrement
        formation.saveToFirebase();
        Toast.makeText(this, "Formation ajoutée!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
