package com.example.pfe;

public class Utilisateur {
    private String id;
    private String nom;
    private String email;
    private String motDePasse;
    private String adresse;
    private String numTel;
    private String role;
    private String niveau;
    private String specialite;

    public Utilisateur() {
        // Constructeur vide requis pour Firebase
    }

    public Utilisateur(String id, String nom, String email, String motDePasse,
                       String adresse, String numTel, String role,
                       String niveau, String specialite) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = role;
        this.niveau = niveau;
        this.specialite = specialite;
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getNumTel() { return numTel; }
    public void setNumTel(String numTel) { this.numTel = numTel; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}