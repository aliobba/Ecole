package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceParent.ModificationProfil;
import com.example.meyss.monecole.AesCrypt;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Matiere;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileEleve extends AppCompatActivity {
   Classe cl ;
    TextView nomE, prenomE, naisE, mailParent, classe;
    Button modifClasse,mail;
    Spinner spClasse;
    ArrayList<String> classes;
    public static Eleve eleve = UserLoged.selectedEleve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_eleve);

        nomE = (TextView) findViewById(R.id.nomElevetxt);
        prenomE= (TextView)findViewById(R.id.prenomElevetxt);
        naisE= (TextView)findViewById(R.id.eleveNaistxt);
        mailParent= (TextView)findViewById(R.id.ParentTxt);
        classe= (TextView)findViewById(R.id.ClasseTxt);
        mail = (Button) findViewById(R.id.mailToParent);
        modifClasse = (Button) findViewById(R.id.modifierClasseEleve);
        classes = new ArrayList<>();
        cl = new Classe();
       // System.out.println(getIntent().getIntExtra("nomEleve",0));
//        System.out.println(getIntent().getIntExtra("classeEleve",0));
        System.out.println("user logged:" +UserLoged.selectedEleve);
        findClasse();
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mailto = "mailto:"+mailParent.getText().toString() +
                        "?cc=" + "" +
                        "&subject=" + Uri.encode("Mon Ecole") ;

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                }

            }
        });

        modifClasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spClasse = (Spinner) findViewById(R.id.classSpinner);

                getClasses();
                spClasse.setVisibility(View.VISIBLE);
                modifClasse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = spClasse.getSelectedItem().toString();
                        String parts[] = str.split(" ");
                        int nivC = Integer.valueOf(parts[0]);
                        System.out.println(nivC);
                        String nomC = parts[1];
                        System.out.println(nomC);
                        findClasseByName(nivC,nomC);

                    }
                });


            }
        });

    }

/*
"idEleve",e
nomEleve",e
prenomEleve
dnEleve",el
classeEleve
parentMail"
*/

    private void findClasse() {

        final String findParent = "http://" + UserLoged.url + "/findClasseById/" +
                +UserLoged.selectedEleve.getIdClasse() + "/";
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {



                    JSONObject o = new JSONObject(response);
                    System.out.println(o);

                    cl.setId(o.getInt("id"));
                    cl.setNiveau(o.getInt("niveau"));
                    cl.setNom(o.getString("nom"));

                    System.out.println("nejbed fil classe classe");

                    findemailParent(nomE,prenomE,naisE,mailParent,classe,cl);

                } catch (JSONException e) {
                    e.printStackTrace();

                    System.out.println("defaul classe");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();



            }
        });

        queue.add(stringRequest);

    }

    private void findClasseByName(int niv, String nom) {
            final Classe cl = new Classe();
        final String findParent = "http://" + UserLoged.url + "/findClasseByNivNom/"+niv+"/"+nom;

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {



                    JSONArray array = new JSONArray(response);
                    int i =0;
                    for ( i =0; i < array.length();i++) {

                        JSONObject o = array.getJSONObject(i);
                        System.out.println(o);

                        cl.setId(o.getInt("id"));
                        cl.setNiveau(o.getInt("niveau"));
                        cl.setNom(o.getString("nom"));

                        System.out.println("nejbed fil classe classe");

                        updateClasseEleve(o.getInt("id"));
                        classe.setText(cl.getNiveau() + cl.getNom());
                        Intent redirection = new Intent(ProfileEleve.this, ClasseAdmin.class);
                        redirection.putExtra("id_classe", o.getInt("id"));
                        redirection.putExtra("nom_classe", o.getInt("niveau") + o.getString("nom"));
                        startActivity(redirection);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    System.out.println("defaul classe");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();



            }
        });

        queue.add(stringRequest);

    }


    private void findemailParent( final TextView nom, final TextView prenom , final TextView dn, final TextView mail,final TextView cla,final Classe cl){


        final String findParent = "http://"+ UserLoged.url+"/findUserById/"
                +UserLoged.selectedEleve.getIdParent()+"/";
        RequestQueue queue= Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    JSONObject o = array.getJSONObject(0);

                    nom.setText(UserLoged.selectedEleve.getNom());
                    prenom.setText( UserLoged.selectedEleve.getPrenom());
                    dn.setText( UserLoged.selectedEleve.getDateNaissance());
                    mail.setText(o.getString("mail"));
                    cla.setText(cl.getNiveau()+cl.getNom());
                    System.out.println("wilyeeeeeey");



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("erreuuuuuur");



                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"verifier votre connexion",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);

    }


    public void getClasses() {
        String url = "http://"+ UserLoged.url+"/classe";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                Classe c = new Classe();
                                JSONObject o = jsA.getJSONObject(i);
                                c.setId(o.getInt("id"));
                                c.setNom(o.getString("nom"));
                                c.setNiveau(o.getInt("niveau"));

                                System.out.println(c.toString());
                                classes.add(c.getNiveau()+" "+c.getNom());
                            }

                            spClasse.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, classes));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley error exception, it didn't work " + error.getMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

    }

    void updateClasseEleve(int idc)
    {
        String url = "http://"+ UserLoged.url+"/UpdateEleve/"+UserLoged.selectedEleve.getId()+"/"+idc;

        RequestQueue queue= Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<String, String>();

        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(ProfileEleve.this,"Modification Réussit",Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileEleve.this,"Modification Réussit",Toast.LENGTH_LONG).show();
                        //Toast.makeText(ProfileEleve.this,error+"",Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);

    }
}
