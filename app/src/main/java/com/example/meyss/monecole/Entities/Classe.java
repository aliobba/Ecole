package com.example.meyss.monecole.Entities;

public class Classe {
    private int id,niveau;
    private String nom,annéesC;


    public Classe() {
    }

    public Classe(int niveau, String nom, String annéesC) {
        this.niveau = niveau;
        this.nom = nom;
        this.annéesC = annéesC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnéesC() {
        return annéesC;
    }

    public void setAnnéesC(String annéesC) {
        this.annéesC = annéesC;
    }

    @Override
    public String toString() {
        return "Classe{" +
                "id=" + id +
                ", niveau=" + niveau +
                ", nom='" + nom + '\'' +
                ", annéesC='" + annéesC + '\'' +
                '}';
    }
}
