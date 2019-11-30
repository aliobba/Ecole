package com.example.meyss.monecole.Activities.EspaceEnseignant;

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
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Exercice;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccueilEnseignant extends Fragment {
    ArrayList<sceance> seances;
    ArrayList<Classe> classes;
    AdapterAccueil adapter;
    SwipeRefreshLayout refresh;
    RecyclerView rv_liste;

    String url = "http://"+ UserLoged.url+"/findSeance/";


    public AccueilEnseignant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.accueil_enseignant, container, false);

        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        rv_liste= (RecyclerView) root.findViewById(R.id.rv_liste);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_liste.setLayoutManager(layoutManager);


        seances=new ArrayList<>();
        classes=new ArrayList<>();
        GetSeanceReq(UserLoged.userConnected.getId());
        adapter = new AdapterAccueil(getContext(),seances,classes);
        rv_liste.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetSeanceReq(UserLoged.userConnected.getId());
                refresh.setRefreshing(false);
            }
        });

        return root;
    }


    private void GetSeanceReq(int idPersonne){
        RequestQueue queue= Volley.newRequestQueue(getContext());

        seances.clear();
        classes.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+idPersonne, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    System.out.println("ici : " +response.toString());
                    JSONArray array = new JSONArray(response.toString());

                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);

                        System.out.println("hani hnééééé-----------------------------------> "+o);
                        sceance s = new sceance();
                        Classe c = new Classe();


                        s.setHeureDeb(o.getString("heureDeb"));
                        s.setHeureFin(o.getString("heureFin"));
                        JSONObject emploiObj = o.getJSONObject("emploi");
                        JSONObject classeObj = emploiObj.getJSONObject("classe");
                        c.setNom(classeObj.getString("nom"));
                        c.setNiveau(classeObj.getInt("niveau"));


                        seances.add(s);
                        classes.add(c);
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
