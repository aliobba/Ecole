package com.example.meyss.monecole.Entities;

public class Matiere
{
    private int id;
    private Double coefficient;
    private String nom;

    public Matiere() {
    }

    public Matiere(Double coefficient, String nom) {
        this.coefficient = coefficient;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Matiere{" +
                "id=" + id +
                ", coefficient=" + coefficient +
                ", nom='" + nom + '\'' +
                '}';
    }
}
