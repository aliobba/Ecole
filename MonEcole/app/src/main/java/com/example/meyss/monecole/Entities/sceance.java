package com.example.meyss.monecole.Entities;

public class sceance {
    private enum  jours{Lundi,Mardi,Mercredi,Jeudi,Vendredi,Samedi}
    private int id,idPersonne,idMatiere;
    private jours jour;
    private String heureDeb,heureFin;


    public sceance() {
    }

    public sceance(int idPersonne, int idMatiere, jours jour, String heureDeb, String heureFin) {
        this.idPersonne = idPersonne;
        this.idMatiere = idMatiere;
        this.jour = jour;
        this.heureDeb = heureDeb;
        this.heureFin = heureFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne = idPersonne;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public jours getJour() {
        return jour;
    }

    public void setJour(jours jour) {
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

    @Override
    public String toString() {
        return "sceance{" +
                "id=" + id +
                ", idPersonne=" + idPersonne +
                ", idMatiere=" + idMatiere +
                ", jour=" + jour +
                ", heureDeb='" + heureDeb + '\'' +
                ", heureFin='" + heureFin + '\'' +
                '}';
    }
}
