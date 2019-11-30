package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ListeEnseignantAdmin extends Fragment  {
    private FragmentTransaction ft;
    //afichage
    String urlGet = "http://" + UserLoged.url + "/AllEnseignant";
    RecyclerView recyclerView;
    FloatingActionButton ajout;
    ArrayList<Personne> enseignantList = new ArrayList<>();
        EnseignantAdapter myAdapter;
    SwipeRefreshLayout refresh;
    DBHelper dbHelper;

    public ListeEnseignantAdmin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.activity_liste_enseignant_admin, container, false);
        dbHelper = new DBHelper(this.getContext());
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ajout = (FloatingActionButton) root.findViewById(R.id.btnImportProf);

        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refreshListEns);

        //affichages des enseignants
        getListData();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListData();
                refresh.setRefreshing(false);
            }
        });
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(),AjouterEnseignant.class));
            }
        });

        return root;
    }


    private void getListData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGet,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                Personne p = new Personne();
                                JSONObject o = jsA.getJSONObject(i);
                                p.setId(o.getInt("id"));
                                p.setNom(o.getString("nom"));
                                p.setPrenom(o.getString("prenom"));
                                p.setPassword(o.getString("password"));
                                p.setMail(o.getString("mail"));
                                p.setTel(o.getString("tel"));
                                p.setImg(o.getString("image"));
                                p.setAdresse(o.getString("adresse"));
                                p.setRole(o.getString("role"));
                                System.out.println("ici mail : " + p.getMail());
								
                                enseignantList.add(p);
                            }

                            EnseignantAdapter EAdapter = new EnseignantAdapter(getContext(), enseignantList);
                            recyclerView.setAdapter(EAdapter);

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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }



}
