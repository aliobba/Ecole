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
import com.example.meyss.monecole.Activities.EspaceParent.Adapter;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClasseEnseignant extends Fragment {

    AdapterClasse adapter;
    SwipeRefreshLayout refresh;
    RecyclerView rv_liste;
    ArrayList<Classe> classe;
    String url = "https://"+ UserLoged.url+"/MonEcole-web/rest/classe/findClasseByEnseignant/";

    public ClasseEnseignant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.classe_enseignant, container, false);

        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        rv_liste= (RecyclerView) root.findViewById(R.id.rv_liste);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_liste.setLayoutManager(layoutManager);

        classe=new ArrayList<>();
        adapter = new AdapterClasse(getActivity(),classe);
        rv_liste.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ClasseGetReq(UserLoged.userConnected.getId());
                refresh.setRefreshing(false);
            }
        });
        ClasseGetReq(UserLoged.userConnected.getId());
        return root;
    }

    private void ClasseGetReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getContext());

        classe.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);

                        Classe c = new Classe();

                        c.setId(o.getInt("id"));
                        c.setNiveau(o.getInt("niveau"));
                        c.setNom(o.getString("nom"));

                        classe.add(c);
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
