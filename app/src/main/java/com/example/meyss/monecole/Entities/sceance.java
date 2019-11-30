package com.example.meyss.monecole.Entities;

import java.util.Date;

public class sceance {
    //private enum  jours{Lundi,Mardi,Mercredi,Jeudi,Vendredi,Samedi}
    private int id;
    private String jour;
    private String heureDeb,heureFin;
    private Date date;
    private int idEnseignant, idEmploi,idMatiere;


    public sceance() {
    }
    public sceance(int id,  String jour, String heureDeb, String heureFin) {
        this.id = id;
        this.jour = jour;
        this.heureDeb = heureDeb;
        this.heureFin = heureFin;

    }

    public sceance(String heureDeb, String heureFin, String jour)
    { this.heureDeb = heureDeb;
        this.heureFin = heureFin;
        this.jour = jour;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getHeureDeb() {
        return heureDeb;
    }

    public void setHeureDeb(String heureDeb) {
        this.heureDeb = heureDeb;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public int getIdEmploi() {
        return idEmploi;
    }

    public void setIdEmploi(int idEmploi) {
        this.idEmploi = idEmploi;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    @Override
    public String toString() {
        return "sceance{" +
                "id=" + id +
                ", jour='" + jour + '\'' +
                ", heureDeb='" + heureDeb + '\'' +
                ", heureFin='" + heureFin + '\'' +
                ", date=" + date +
                '}';
    }
}
