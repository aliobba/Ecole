package com.example.meyss.monecole.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceAdmin.EspaceAdmine;
import com.example.meyss.monecole.Activities.EspaceEnseignant.EspaceEnseignant;
import com.example.meyss.monecole.Activities.EspaceParent.Accueil;
import com.example.meyss.monecole.Activities.EspaceParent.parents;
import com.example.meyss.monecole.AesCrypt;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    int id;
    String nom,prenom,email,password,tel,role,image;
    SharedPreferences sharePref;
    SharedPreferences.Editor shEdit;
    String url = "http://"+UserLoged.url+"/findUser/";
    EditText userTxt ;
    EditText passTxt;
    AesCrypt cryptage;
    private String  passwordCrypt;
    Personne p=new Personne();
    int MODE_PRIVATE=0;

    Dialog epicDialog;
    ImageView closePopup;
    EditText EditMailPopUp;
    TextView msgPopUp;
    Button recherche;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        epicDialog = new Dialog(this);

        userTxt = (EditText) findViewById(R.id.username);
        passTxt = (EditText) findViewById(R.id.password);
        sharePref = PreferenceManager.getDefaultSharedPreferences(getApplication());


        sharePref=getSharedPreferences("log",0);

        final CardView login = (CardView) findViewById(R.id.connection);


        if(!sharePref.getString("email","").equals(userTxt.getText().toString().trim())
                && !sharePref.getString("password","").equals(AesCrypt.encrypt(passTxt.getText().toString().trim()))
                && sharePref.getString("role","").equals("Parent")){

            p.setId(Integer.valueOf(sharePref.getString("id","")));
            p.setMail(sharePref.getString("email",""));
            p.setPassword(sharePref.getString("password",""));
            p.setNom(sharePref.getString("nom",""));
            p.setPrenom(sharePref.getString("prenom",""));
            p.setImg(sharePref.getString("image",""));
            p.setTel(sharePref.getString("tel",""));
            p.setRole(sharePref.getString("role",""));
            UserLoged.userConnected=p;
            Intent intent = new Intent(login.this,parents.class);
            startActivity(intent);
            finish();
        }else if(!sharePref.getString("email","").equals(userTxt.getText().toString().trim())
                && !sharePref.getString("password","").equals(AesCrypt.encrypt(passTxt.getText().toString().trim()))
                && sharePref.getString("role","").equals("Enseignant")){
            p.setId(Integer.valueOf(sharePref.getString("id","")));
            p.setMail(sharePref.getString("email",""));
            p.setPassword(sharePref.getString("password",""));
            p.setNom(sharePref.getString("nom",""));
            p.setPrenom(sharePref.getString("prenom",""));
            p.setImg(sharePref.getString("image",""));
            p.setTel(sharePref.getString("tel",""));
            p.setRole(sharePref.getString("role",""));
            UserLoged.userConnected=p;
                Intent intent = new Intent(login.this,EspaceEnseignant.class);
                startActivity(intent);
                finish();
            }else if(!sharePref.getString("email","").equals(userTxt.getText().toString().trim())
                && !sharePref.getString("password","").equals(AesCrypt.encrypt(passTxt.getText().toString().trim()))
                && sharePref.getString("role","").equals("Admin")){
            p.setId(Integer.valueOf(sharePref.getString("id","")));
            p.setMail(sharePref.getString("email",""));
            p.setPassword(sharePref.getString("password",""));
            p.setNom(sharePref.getString("nom",""));
            p.setPrenom(sharePref.getString("prenom",""));
            p.setImg(sharePref.getString("image",""));
            p.setTel(sharePref.getString("tel",""));
            p.setRole(sharePref.getString("role",""));
            UserLoged.userConnected=p;
            Intent intent = new Intent(login.this,EspaceAdmine.class);
            startActivity(intent);
            finish();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    passwordCrypt = AesCrypt.encrypt(passTxt.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(userTxt.getText()!=null && passTxt.getText() != null)
                {
                    System.out.println("------->  "+userTxt.getText());
                    loginReq(userTxt.getText().toString().trim());


                }else{
                    Toast.makeText(login.this,"Format mail invalide",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button inscrit = (Button) findViewById(R.id.signup);

        inscrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(login.this,inscrit.class);
                startActivity(intent);*/
               ShowPopUp();
            }
        });
    }

    private void loginReq(String mail){

        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + mail + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i=0;i < array.length();i++) {
                        sharePref=getSharedPreferences("log",0);

                        JSONObject o = array.getJSONObject(i);
                        System.out.println("objet password  :  "+o.getString("password")+"password crypt :  "+passwordCrypt);

                        if (passwordCrypt.equals(o.getString("password"))) {
                            p.setId(o.getInt("id"));
                            p.setNom(o.getString("nom"));
                            p.setPrenom(o.getString("prenom"));
                            p.setPassword(o.getString("password"));
                            p.setMail(o.getString("mail"));
                            p.setTel(o.getString("tel"));
                            p.setImg(o.getString("image"));
                            p.setAdresse(o.getString("adresse"));
                            p.setRole(o.getString("role"));
                            System.out.println("ici Role : "+p.getRole());
                            UserLoged.userConnected = p;
                            id = UserLoged.userConnected.getId();
                            nom = UserLoged.userConnected.getNom();
                            prenom = UserLoged.userConnected.getPrenom();
                            email = UserLoged.userConnected.getMail();
                            password = passwordCrypt;
                            tel = UserLoged.userConnected.getTel();
                            role = UserLoged.userConnected.getRole();
                            image = UserLoged.userConnected.getImg();
                           /* intent.putExtra("nom",p.getNom().toString());
                            intent.putExtra("prenom",p.getPrenom().toString());*/
                           if(p.getRole().equals("Parent")){
                               if(sharePref.getString("id","")!=null||sharePref.getString("nom","")!=null||sharePref.getString("prenom","")!=null||sharePref.getString("image","")!=null||sharePref.getString("tel","")!=null||sharePref.getString("email","")!=null||sharePref.getString("password","")!=null||sharePref.getString("role","")!=null){
                                   shEdit = sharePref.edit();
                                   shEdit.putString("id", String.valueOf(id));
                                   shEdit.putString("email", email);
                                   shEdit.putString("password", password);
                                   shEdit.putString("role", role);
                                   shEdit.putString("nom", nom);
                                   shEdit.putString("prenom", prenom);
                                   shEdit.putString("image", image);
                                   shEdit.putString("tel", tel);
                                   shEdit.commit();
                               Intent intent = new Intent(login.this,parents.class);
                               startActivity(intent);
                               finish();}
                           }else if(p.getRole().equals("Enseignant")){
                               if(sharePref.getString("id","")!=null||sharePref.getString("nom","")!=null||sharePref.getString("prenom","")!=null||sharePref.getString("image","")!=null||sharePref.getString("tel","")!=null||sharePref.getString("email","")!=null||sharePref.getString("password","")!=null||sharePref.getString("role","")!=null) {
                                   shEdit = sharePref.edit();
                                   shEdit.putString("id", String.valueOf(id));
                                   shEdit.putString("email", email);
                                   shEdit.putString("password", password);
                                   shEdit.putString("role", role);
                                   shEdit.putString("nom", nom);
                                   shEdit.putString("prenom", prenom);
                                   shEdit.putString("image", image);
                                   shEdit.putString("tel", tel);
                                   shEdit.commit();
                                   System.out.println("nom sharedPref : "+sharePref.getString("role",""));
                                   System.out.println("nom UL : "+UserLoged.userConnected.getNom());
                                   Intent intent = new Intent(login.this, EspaceEnseignant.class);
                                   startActivity(intent);
                                   finish();
                               }
                           }else if(p.getRole().equals("Admin")){
                                if(sharePref.getString("id","")!=null||sharePref.getString("nom","")!=null||sharePref.getString("prenom","")!=null||sharePref.getString("image","")!=null||sharePref.getString("tel","")!=null||sharePref.getString("email","")!=null||sharePref.getString("password","")!=null||sharePref.getString("role","")!=null){
                                    shEdit = sharePref.edit();
                                    shEdit.putString("id", String.valueOf(id));
                                    shEdit.putString("email", email);
                                    shEdit.putString("password", password);
                                    shEdit.putString("role", role);
                                    shEdit.putString("nom", nom);
                                    shEdit.putString("prenom", prenom);
                                    shEdit.putString("image", image);
                                    shEdit.putString("tel", tel);
                                    shEdit.commit();
                                    Intent intent=new Intent(login.this,EspaceAdmine.class);
                                    startActivity(intent);
                                    finish();}
                            }
                        } else {
                            Toast.makeText(login.this, "verifier votre Password ",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(login.this,"Verifier votre connection",Toast.LENGTH_LONG).show();
                }
        });

        queue.add(stringRequest);
    }

    public void ShowPopUp(){
        epicDialog.setContentView(R.layout.activity_recherche_mail);
        closePopup = (ImageView) epicDialog.findViewById(R.id.close);
        EditMailPopUp = (EditText) epicDialog.findViewById(R.id.mail);
        EditMailPopUp = (EditText) epicDialog.findViewById(R.id.mail);
        msgPopUp = (TextView) epicDialog.findViewById(R.id.info);
        recherche = (Button) epicDialog.findViewById(R.id.recherche);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });

        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechercheMail(EditMailPopUp.getText().toString().trim());
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    private void rechercheMail(String mail){
        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + mail + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);
                        System.out.println("ici : "+o.getString("role"));

                        if((o.getString("password").equals("null")||o.getString("password").equals("")) && (o.getString("role").equals("Parent") || o.getString("role").equals("Enseignant"))){
                            p.setId(o.getInt("id"));
                            p.setMail(o.getString("mail"));
                            p.setRole(o.getString("role"));
                            UserLoged.userConnected = p;
                            Intent intent = new Intent(login.this,inscrit.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(login.this,"Vous êtes déja inscrit",Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(login.this,"Mail introuvable",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(login.this,"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);

    }

}
