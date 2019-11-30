 package com.example.meyss.monecole.Activities.EspaceParent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceEnseignant.ProfilEnseignant;
import com.example.meyss.monecole.Activities.login;
import com.example.meyss.monecole.AesCrypt;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ModificationProfil extends AppCompatActivity {
    SharedPreferences sharePref;
    SharedPreferences.Editor shEdit;
    Button modifier;
    EditText nom,prenom,tel,email,password;
    private String pwdCrypt;
    String url = "http://"+ UserLoged.url+"/UpdatePersonne";
    String url2 = "http://"+ UserLoged.url+"/findUserById/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_profil);
        sharePref=getSharedPreferences("log",this.MODE_PRIVATE);

        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        tel = (EditText) findViewById(R.id.tel);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.pwd);

        nom.setText(sharePref.getString("nom",""));
        prenom.setText(sharePref.getString("prenom",""));
            if(UserLoged.userConnected.getTel()!="null"){
                tel.setVisibility(View.VISIBLE);
                tel.setText(sharePref.getString("tel",""));
            }
        email.setText(sharePref.getString("email",""));
        try {
            pwdCrypt = AesCrypt.decrypt(sharePref.getString("password",""));
        } catch (Exception e) {
            System.out.println("Erreur décryptage : "+e);
        }
        password.setText(pwdCrypt);
        /*if(!pwdCrypt.equals(password.getText())){

        }*/
        modifier = (Button) findViewById(R.id.modif);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateReq();

            }
        });


    }

    private void UpdateReq(){

        RequestQueue queue= Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(UserLoged.userConnected.getId()));
        params.put("nom", nom.getText().toString());
        params.put("prenom",prenom.getText().toString());
        params.put("mail", email.getText().toString());
        params.put("tel", tel.getText().toString());
        params.put("password",AesCrypt.encrypt(password.getText().toString()));
        System.out.println(params);
        Log.d("Json","Json"+params);
        JSONObject json = new JSONObject(params);



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(ModificationProfil.this,"Modification Réussit",Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        UserGetReq(UserLoged.userConnected.getId());
                        Toast.makeText(ModificationProfil.this,"Modification Réussit",Toast.LENGTH_LONG).show();


                    }
                }
        );

        queue.add(postRequest);
    }


    private void UserGetReq(int id){

        RequestQueue queue= Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2 + id + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {


                        JSONObject o = array.getJSONObject(i);
                            Personne p = new Personne();

                            p.setId(o.getInt("id"));
                            p.setNom(o.getString("nom"));
                            p.setPrenom(o.getString("prenom"));
                            p.setPassword(o.getString("password"));
                            p.setMail(o.getString("mail"));
                            p.setTel(o.getString("tel"));
                            p.setImg(o.getString("image"));
                            p.setAdresse(o.getString("adresse"));
                            p.setRole(o.getString("role"));
                            System.out.println("ici mail : "+p.getMail());
                            UserLoged.userConnected = p;
                            sharePref.edit().clear().commit();
                            shEdit = sharePref.edit();
                            shEdit.putString("id", String.valueOf(p.getId()));
                            shEdit.putString("email", p.getMail());
                            shEdit.putString("password", p.getPassword());
                            shEdit.putString("role", p.getRole());
                            shEdit.putString("nom", p.getNom());
                            shEdit.putString("prenom", p.getPrenom());
                            shEdit.putString("image", p.getImg());
                            shEdit.putString("tel", p.getTel());
                            shEdit.commit();
                        System.out.println("nom sharedPref : "+sharePref.getString("nom",""));
                        System.out.println("nom UL : "+UserLoged.userConnected.getNom());

                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }
}
