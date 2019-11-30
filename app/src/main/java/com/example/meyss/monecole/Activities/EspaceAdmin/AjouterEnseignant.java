package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceParent.Profil;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AjouterEnseignant extends AppCompatActivity {
    String url = "http://" + UserLoged.url + "/inscription";

    EditText getGetEditNom, getEditPrenom, getEditMail, getEditTel;
    Button addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_enseignant);

        getGetEditNom = (EditText) findViewById(R.id.nomProfEdit);
        getEditPrenom = (EditText) findViewById(R.id.prenomProfEdit);
        getEditMail = (EditText) findViewById(R.id.mailProfEdit);
        getEditTel = (EditText) findViewById(R.id.telProfEdit);
        addbtn = (Button) findViewById(R.id.addEnseignant);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!getGetEditNom.getText().toString().equals(""))&&(!getEditPrenom.getText().toString().equals(""))
                        &&(!getEditMail.getText().toString().equals("")) &&(!getEditTel.getText().toString().equals(""))) {
                    insertReq();
                    startActivityForResult(new Intent(AjouterEnseignant.this,EspaceAdmine.class),1);
                }else
                {
                    Toast.makeText(getApplicationContext(),"veuillez remplir tous les champs",Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void insertReq() {

        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("nom", getGetEditNom.getText().toString());
        params.put("prenom", getEditPrenom.getText().toString());
        params.put("mail", getEditMail.getText().toString());
        params.put("role", "Enseignant");
        params.put("tel", getEditTel.getText().toString());
        System.out.println(params);
        Log.d("Json", "Json" + params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response + "++++++++++++++++++++");
                Toast.makeText(getApplication(), "Succès inscrit", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error + "----------------");
                        Toast.makeText(getApplication(), error + "", Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                EspaceAdmine.f.add(R.id.fragment1,new ListeEnseignantAdmin());
                EspaceAdmine.f.commit();
            }
        }
    }
}
