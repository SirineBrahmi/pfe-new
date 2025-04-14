package com.example.pfe;

public class Classroom {
    private String classroomId;
    private String motDePasse;
    private String nom;
    private String createur;

    // Constructeur par défaut (nécessaire pour Firebase)
    public Classroom() {}

    // Constructeur avec paramètres
    public Classroom(String classroomId, String motDePasse, String nom, String createur) {
        this.classroomId = classroomId;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.createur = createur;
    }

    // Getters et Setters
    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }
}
