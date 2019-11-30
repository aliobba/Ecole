package com.example.meyss.monecole.Entities;

public class Personne {
    private int id, img;
    private String nom, prenom;
    private String tel, adresse;
    private String mail, password;
    private enum roles {Admin, Enseignant, Parent}
    private roles role;

    public Personne() {
    }

    public Personne(String nom, String prenom, String tel, String adresse, String mail, String password,roles role) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public Personne(int img, String nom, String prenom, String tel, String adresse, String mail, String password,roles role) {
        this.img = img;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.mail = mail;
        this.password = password;
        this.role = role;
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

    public roles getRole() {
        return role;
    }

    public void setRole(roles role) {
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
