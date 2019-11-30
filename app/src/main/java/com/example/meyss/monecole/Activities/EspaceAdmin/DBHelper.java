package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Personne;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context,"tournoi",null,1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists personne " + "(integer id,text nom, text prenom ,text img,text tel,text mail, text password, text role)");
        db.execSQL(
                "create table if not exists eleve " + "(integer id,text nom,text prenom,text img,text dateNaissance,integer idParent, integer idClasse)");

        db.execSQL(
                "create table if not exists classe " + "(integer id,text nom,integer niveau)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insertEnseignant (Personne p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", p.getId());
        contentValues.put("nom", p.getNom());
        contentValues.put("prenom", p.getPrenom());
        contentValues.put("img", p.getImg());
        contentValues.put("tel", p.getTel());
        contentValues.put("mail", p.getMail());
        contentValues.put("password",p.getPassword());
        contentValues.put("role", p.getRole());
        db.insert("personne", null, contentValues);
        System.out.println(p +" inséré en sqlLite avec succés");
        return true;
    }
    public boolean insertEleve(Eleve e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", e.getId());
        contentValues.put("nom", e.getNom());
        contentValues.put("prenom", e.getPrenom());
        contentValues.put("img", e.getImg());
        contentValues.put("dateNaissance", e.getDateNaissance());
        contentValues.put("idParent", e.getIdParent());
        contentValues.put("idClasse", e.getIdClasse());
        db.insert("eleve", null, contentValues);
        System.out.println(e +" inséré en sqlite  avec succés");
        return true;
    }

    public ArrayList<Personne> getAllEnseignant() {
        ArrayList<Personne> array_list = new ArrayList<Personne>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query( "personne",null,null,null,null,null,null);
        res.moveToFirst();

        do {
            Personne p = new Personne();
            p.setId(res.getInt(res.getColumnIndex("id")));
            p.setNom(res.getString(res.getColumnIndex("nom")));
            p.setPrenom(res.getString(res.getColumnIndex("prenom")));
            p.setImg(res.getString(res.getColumnIndex("img")));
            p.setTel(res.getString(res.getColumnIndex("tel")));
            p.setMail(res.getString(res.getColumnIndex("mail")));
            p.setPassword(res.getString(res.getColumnIndex("password")));
            p.setRole(res.getString(res.getColumnIndex("role")));

            array_list.add(p);
        } while (res.moveToNext());
        System.out.println(" liste récupérééééééééé");
        return array_list;
    }

    public ArrayList<Eleve> getAllEleves() {
        ArrayList<Eleve> array_list = new ArrayList<Eleve>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query( "eleve",null,null,null,null,null,null);
        res.moveToFirst();

        do {
            Eleve e = new Eleve();
            e.setId(res.getInt(res.getColumnIndex("id")));
            e.setNom(res.getString(res.getColumnIndex("nom")));
            e.setPrenom(res.getString(res.getColumnIndex("prenom")));
            e.setImg(res.getString(res.getColumnIndex("img")));
            e.setDateNaissance(res.getString(res.getColumnIndex("dateNaissance")));
            e.setIdParent(res.getInt(res.getColumnIndex("idParent")));
            e.setIdClasse(res.getInt(res.getColumnIndex("idClasse")));

            array_list.add(e);
        } while (res.moveToNext());
        System.out.println(" liste récupérééééééééé");
        return array_list;
    }

    public boolean insertClasse(Classe e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", e.getId());
        contentValues.put("nom", e.getNom());
        contentValues.put("niveau", e.getNiveau());

        db.insert("classe", null, contentValues);
        System.out.println(e +" inséré en sqlite  avec succés");
        return true;
    }

    public ArrayList<Classe> getAllClasses() {
        ArrayList<Classe> array_list = new ArrayList<Classe>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query( "classe",null,null,null,null,null,null);
        res.moveToFirst();

        do {
            Classe e = new Classe();
            e.setId(res.getInt(res.getColumnIndex("id")));
            e.setNom(res.getString(res.getColumnIndex("nom")));
            e.setNiveau(res.getInt(res.getColumnIndex("niveau")));
             array_list.add(e);
        } while (res.moveToNext());
        System.out.println(" liste récupérééééééééé");
        return array_list;
    }
}