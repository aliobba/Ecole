package com.example.meyss.monecole.Entities;

public class Personne {
    private int id;
    private String nom, prenom ,img;
    private String tel, adresse;
    private String mail, password;
    //private enum roles {Admin, Enseignant, Parent}
    private String role;

    public Personne() {
    }

    public Personne(String nom, String prenom, String tel, String adresse, String mail, String password,String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public Personne(String img, String nom, String prenom, String tel, String adresse, String mail, String password,String role) {
        this.img = img;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }




    public Personne(String mail, String role) {
        this.mail = mail;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", img=" + img +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", adresse='" + adresse + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
