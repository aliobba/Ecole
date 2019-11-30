package com.example.meyss.monecole.Activities.EspaceParent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class eleve extends Fragment {
    private FragmentTransaction ft ;
    Adapter adapter;
    ArrayList<Eleve> eleve;
    SwipeRefreshLayout refresh;
    RecyclerView rv_liste;
    String url = "http://"+ UserLoged.url+"/findEleveByParent/";
    public eleve() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_eleve, container, false);
        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        rv_liste= (RecyclerView) root.findViewById(R.id.rv_liste);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_liste.setLayoutManager(layoutManager);
        eleve=new ArrayList<>();
        adapter = new Adapter(getActivity(),eleve);
        rv_liste.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EleveGetReq(UserLoged.userConnected.getId());
                refresh.setRefreshing(false);
            }
        });

        EleveGetReq(UserLoged.userConnected.getId());

        return root;
    }


    private void EleveGetReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getActivity());

        eleve.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id + "/", new Response.Listener<String>() {
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
                        eleve.add(e);
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
