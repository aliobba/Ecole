package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsulterCahierTexte extends Fragment {
    ArrayList<Exercice> exercices;
    AdapterConsultCahier adapter;
    SwipeRefreshLayout refresh;
    RecyclerView rv_liste;
    String url2 = "http://"+ UserLoged.url+"/findCahier/";
    String url = "http://"+ UserLoged.url+"/findExercice/";

    public ConsulterCahierTexte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_consulter_cahier_texte, container, false);

        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        rv_liste= (RecyclerView) root.findViewById(R.id.rv_liste);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_liste.setLayoutManager(layoutManager);


        exercices=new ArrayList<>();
        GetCahierReq(UserLoged.id_classe_eleve);
        adapter = new AdapterConsultCahier(getContext(),exercices);
        rv_liste.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetExerciceReq(UserLoged.userConnected.getId(),UserLoged.id_cahier);
                refresh.setRefreshing(false);
            }
        });



        return root;
    }

    private void GetCahierReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        UserLoged.id_cahier = o.getInt("id");
                        System.out.println("id cahier : "+UserLoged.id_cahier);
                        GetExerciceReq(UserLoged.userConnected.getId(),UserLoged.id_cahier);
                    }
                }  catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void GetExerciceReq(int idPersonne,int idcahier){
        RequestQueue queue= Volley.newRequestQueue(getContext());

        exercices.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+idPersonne+"/"+idcahier, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    System.out.println("ici : " +response.toString());
                    JSONArray array = new JSONArray(response.toString());

                        for (int i=0;i < array.length();i++) {
                            JSONObject o = array.getJSONObject(i);

                            System.out.println("hani hnééééé-----------------------------------> "+o);
                            Exercice e = new Exercice();

                            e.setId(o.getInt("id"));
                            e.setConsigne(o.getString("consigne"));
                            e.setDateRendu(o.getString("date_rendu"));


                            exercices.add(e);
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
