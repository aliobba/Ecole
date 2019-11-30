package com.example.meyss.monecole.Entities;

public class Matiere
{
    private int id, coefficient;
    private String nom;

    public Matiere() {
    }

    public Matiere(int coefficient, String nom) {
        this.coefficient = coefficient;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
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
