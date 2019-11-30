package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceAdmin.AllElevesAdapter;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ElevesAdminList  extends Fragment {
    ArrayList<Eleve> eleveList = new ArrayList<Eleve>();
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.activity_eleves_admin_list, container, false);

        final ListView list = (ListView) root.findViewById(R.id.elevesList) ;
        dbHelper = new DBHelper(this.getContext());
        String url = "http://"+ UserLoged.url+"/eleve";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                Eleve eleve = new Eleve();
                                JSONObject o = jsA.getJSONObject(i);
                                eleve.setId(o.getInt("id"));
                                eleve.setNom(o.getString("nom"));
                                eleve.setPrenom(o.getString("prenom"));
                                eleve.setDateNaissance(o.getString("date_naissance"));
                                JSONObject jo = o.getJSONObject("classe");
                                JSONObject jop = o.getJSONObject("personne");
                                eleve.setIdClasse(jo.getInt( "id"));
                                eleve.setIdParent(jop.getInt("id"));
                               // dbHelper.insertEleve(eleve);
                                eleveList.add(eleve);
                            }

                            AllElevesAdapter cAdapter = new AllElevesAdapter(getContext(),R.layout.eleve_item,eleveList);
                            list.setAdapter(cAdapter);

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


        return root;
    }
}
