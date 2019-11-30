package com.example.meyss.monecole.Activities.EspaceParent;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceEnseignant.AdapterAccueil;
import com.example.meyss.monecole.Activities.login;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Matiere;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;
import com.github.eunsiljo.timetablelib.data.TimeData;
import com.github.eunsiljo.timetablelib.data.TimeGridData;
import com.github.eunsiljo.timetablelib.data.TimeTableData;
import com.github.eunsiljo.timetablelib.view.TimeTableView;
import com.github.eunsiljo.timetablelib.viewholder.TimeTableItemViewHolder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Accueil extends Fragment {
    ArrayList<Eleve> eleves;
    ArrayList<sceance> seances;
    ArrayList<Matiere> matieres;
    AdapterAccueilParent adapter;
    SwipeRefreshLayout refresh;
    RecyclerView rv_liste;

    String url = "http://"+ UserLoged.url+"/MonEcole-web/rest/absence/findSeanceEleve/";
    String url2 = "http://"+ UserLoged.url+"/findEleveByParent/";

    public Accueil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_accueil, container, false);

        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        rv_liste= (RecyclerView) root.findViewById(R.id.rv_liste);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_liste.setLayoutManager(layoutManager);


        seances=new ArrayList<>();
        matieres=new ArrayList<>();
        eleves=new ArrayList<>();
        EleveGetReq(UserLoged.userConnected.getId());
        adapter = new AdapterAccueilParent(getContext(),seances,matieres,eleves);
        rv_liste.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EleveGetReq(UserLoged.userConnected.getId());
                refresh.setRefreshing(false);
            }
        });


        return root;
    }

    private void GetSeanceReq(int idClasse){
        RequestQueue queue= Volley.newRequestQueue(getContext());

        seances.clear();
        matieres.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+idClasse, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    System.out.println("ici : " +response.toString());
                    JSONArray array = new JSONArray(response.toString());

                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        JSONObject matiereObj = o.getJSONObject("matiere");

                        System.out.println("hani hnééééé-----------------------------------> "+o);
                        sceance s = new sceance();
                        Matiere m = new Matiere();

                        s.setHeureDeb(o.getString("heureDeb"));
                        s.setHeureFin(o.getString("heureFin"));
                        m.setNom(matiereObj.getString("nom"));

                        matieres.add(m);
                        seances.add(s);

                        adapter.notifyDataSetChanged();
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void EleveGetReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getActivity());

        eleves.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2 + id + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        JSONObject classeObj = o.getJSONObject("classe");
                        System.out.println("hani hnééééé-----------------------------------> "+o);
                        Eleve e = new Eleve();
                        e.setId(o.getInt("id"));
                        e.setNom(o.getString("nom"));
                        e.setPrenom(o.getString("prenom"));
                        e.setDateNaissance(o.getString("date_naissance"));
                        e.setImg(o.getString("photo"));
                        e.setIdClasse(classeObj.getInt("id"));
                        System.out.println("Mon Nom est : "+e.getNom());

                        GetSeanceReq(e.getIdClasse());
                        eleves.add(e);

                        adapter.notifyDataSetChanged();
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }



}
