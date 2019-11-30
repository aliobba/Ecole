package com.example.meyss.monecole.Entities;

public class Exercice {
    private int id,idMatiere,idEnseignant;
    private String consigne,dateRendu;

    public Exercice() {
    }

    public Exercice(int idMatiere, int idEnseignant, String consigne, String dateRendu) {
        this.idMatiere = idMatiere;
        this.idEnseignant = idEnseignant;
        this.consigne = consigne;
        this.dateRendu = dateRendu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public String getConsigne() {
        return consigne;
    }

    public void setConsigne(String consigne) {
        this.consigne = consigne;
    }

    public String getDateRendu() {
        return dateRendu;
    }

    public void setDateRendu(String dateRendu) {
        this.dateRendu = dateRendu;
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "id=" + id +
                ", idMatiere=" + idMatiere +
                ", idEnseignant=" + idEnseignant +
                ", consigne='" + consigne + '\'' +
                ", dateRendu='" + dateRendu + '\'' +
                '}';
    }
}
