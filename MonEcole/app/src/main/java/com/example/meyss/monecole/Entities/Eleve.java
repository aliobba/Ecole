package com.example.meyss.monecole.Entities;

public class Eleve {
    private int id, img;
    private String nom, prenom;
    private String dateNaissance;
    private int idParent, idClasse;



    public Eleve() {
    }

    public Eleve(int img, String nom, String prenom, String dateNaissance, int idParent,int  idClasse) {
        this.img = img;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.idParent = idParent;
        this.idClasse = idClasse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    @Override
    public String toString() {
        return "Eleve{" +
                "id=" + id +
                ", img=" + img +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", idParent=" + idParent +
                ", idClasse=" + idClasse +
                '}';
    }
}
