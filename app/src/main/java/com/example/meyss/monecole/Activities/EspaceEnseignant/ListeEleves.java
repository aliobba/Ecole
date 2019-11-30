package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListeEleves extends Fragment {

    List<Eleve> eleves=new ArrayList<Eleve>();
    String url = "http://"+ UserLoged.url+"/findEleveByClasse/";

    public ListeEleves() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.listview_eleve_classe, container, false);
        final ListView listView = (ListView) root.findViewById(R.id.listview);
        System.out.println("id classe : "+UserLoged.id_classe_eleve);


        RequestQueue queue= Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + UserLoged.id_classe_eleve + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);


                        Eleve e = new Eleve();

                        e.setId(o.getInt("id"));
                        e.setNom(o.getString("nom"));
                        e.setPrenom(o.getString("prenom"));
                        e.setDateNaissance(o.getString("date_naissance"));


                        eleves.add(e);
                        AdapterEleve adapter = new AdapterEleve(root.getContext(),R.layout.item_eleve_classe,eleves);
                        listView.setAdapter(adapter);

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

        System.out.println("eleve 9bal l adapter : "+eleves);



        return root;
    }



}
