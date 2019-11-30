package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Abscence;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListeElevesAbsents extends Fragment {

    AdapterEleveAbsent adapter;
    ArrayList<Eleve> eleves;
    String url = "http://"+ UserLoged.url+"/findEleveByClasse/";
    String url2 = "http://"+ UserLoged.url+"/ajout/absence";
    String url3 = "http://"+ UserLoged.url+"/findSeance/";
    Button submit;
    ListView listView;
    static int id_seance;

    Date todayDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String todayString = formatter.format(todayDate);
    JSONObject json;

    public ListeElevesAbsents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.listview_eleve_absence, container, false);
         listView = (ListView) root.findViewById(R.id.listview);
        System.out.println("id classe : "+UserLoged.id_classe_eleve);
        System.out.println(todayString);

        eleves=new ArrayList<>();
        EleveReq();

        adapter = new AdapterEleveAbsent(getContext(),R.layout.item_eleve_absence,eleves);
        listView.setAdapter(adapter);
        GetSeanceReq(UserLoged.userConnected.getId());
        System.out.println("hani hné eleveeeeeeeee : "+adapter.eleves.size());
        for(int i=0;i<adapter.eleves.size();i++){
            System.out.println("hné id : "+adapter.eleves.get(i).getId());
        }
        submit=(Button) root.findViewById(R.id.envoyer);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = new HashMap<>();
                for (int i=0;i<adapter.eleves.size();i++){
                    if(adapter.eleves.get(i).isAbsent()==true){
                        params.put("etat", "0");
                        params.put("date",todayString);
                        Log.d("Json","Json"+params);
                        json = new JSONObject(params);
                        insertAbsent(adapter.eleves.get(i).getId());

                    }else{
                        params.put("etat", "1");
                        params.put("date",todayString);
                        Log.d("Json","Json"+params);
                        json = new JSONObject(params);
                        insertAbsent(adapter.eleves.get(i).getId());
                    }

                }
                Toast.makeText(getActivity(),"Ajout Terminer",Toast.LENGTH_LONG).show();
                submit.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }


    public void EleveReq(){
        RequestQueue queue= Volley.newRequestQueue(getContext());
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
                        e.setAbsent(false);
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

    private void GetSeanceReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url3 + id + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);



                       id_seance=o.getInt("id");
                        System.out.println("id seance  : "+id_seance);


                    }
                }  catch (Exception e) {
                    System.out.println(e);
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

    private void insertAbsent(int id){
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url2+id_seance+"/"+id,json , new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("erreur");

                    }
                }
        );

        queue.add(postRequest);
    }
}
