package com.example.pfe;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utilisateur {
    private String id;
    private String nom;
    private String email;
    private String motDePasse;
    private String adresse;
    private String numTel;
    private String role;  // "etudiant", "formateur" ou "admin"

    // Constructeur
    public Utilisateur(String id, String nom, String email, String motDePasse, String adresse, String numTel, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = role;
    }

    // Méthode pour enregistrer un utilisateur dans Firebase
    public void saveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference utilisateursRef = database.getReference("utilisateurs");

        // Enregistre dans la sous-table correspondant au rôle
        utilisateursRef.child(this.role).child(this.id).setValue(this)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Utilisateur ajouté avec succès !");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Erreur d'ajout de l'utilisateur : " + e.getMessage());
                });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
