package com.example.meyss.monecole.Entities;

import java.util.Date;

public class Abscence {
    private int id,idEleve,idSceance;
    private String etat;
    private Date date;

    public Abscence() {
    }

    public Abscence(int id, int idEleve, int idSceance, String etat, Date date) {
        this.id = id;
        this.idEleve = idEleve;
        this.idSceance = idSceance;
        this.etat = etat;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public int getIdSceance() {
        return idSceance;
    }

    public void setIdSceance(int idSceance) {
        this.idSceance = idSceance;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
