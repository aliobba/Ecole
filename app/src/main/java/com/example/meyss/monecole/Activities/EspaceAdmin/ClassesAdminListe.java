package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Emploi;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassesAdminListe extends Fragment {
    Spinner niveau;
    EditText nomClasse;
    Button btnAdd;
    ArrayList<Classe> classeList = new ArrayList<Classe>();
    SwipeRefreshLayout refresh;
    ListView list;
    DBHelper dbHelper;
    public ClassesAdminListe() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.activity_classes_admin_liste, container, false);

     list = (ListView) root.findViewById(R.id.classList) ;

        refresh = (SwipeRefreshLayout) root.findViewById(R.id.refreshListClasses);
            dbHelper = new DBHelper(this.getContext());
        //affichages des enseignants
        getListData();




        FloatingActionButton btnadd = ( FloatingActionButton) root.findViewById(R.id.addClassbtn);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity( new Intent(root.getContext(),AjoutClasse.class));
                final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
                View view = inflater.inflate(R.layout.activity_ajout_classe, null);
                dialogBuilder.setTitle("Ajouter une classe");
                dialogBuilder.setMessage("veuillez remplir les champs");

                niveau = (Spinner) view.findViewById(R.id.nivSpinner);
                nomClasse = (EditText) view.findViewById(R.id.editClassName);
                btnAdd = (Button) view.findViewById(R.id.btnAddClass);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertReq();
                        findClasseByName(Integer.valueOf(niveau.getSelectedItem().toString()),nomClasse.getText().toString());
                        Toast.makeText(getContext(),"Classe ajoutée",Toast.LENGTH_LONG);
                        dialogBuilder.dismiss();


                    }
                });

                dialogBuilder.setView(view);

                dialogBuilder.show();
            }
        });

        return root;
    }
    void getListData()
    {
        String url = "http://"+ UserLoged.url+"/classe";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                Classe c = new Classe();
                                JSONObject o = jsA.getJSONObject(i);
                                c.setId(o.getInt("id"));
                                c.setNom(o.getString("nom"));
                                c.setNiveau(o.getInt("niveau"));

                                System.out.println(c.toString());
                               // dbHelper.insertClasse(c);
                                classeList.add(c);
                            }

                            ClasseAdapter cAdapter = new ClasseAdapter(getContext(),R.layout.class_item,classeList);
                            list.setAdapter(cAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley error exception, it didn't work " + error.getMessage());
               // classeList = dbHelper.getAllClasses();
               // ClasseAdapter cAdapter = new ClasseAdapter(getContext(),R.layout.class_item,classeList);

                //list.setAdapter(cAdapter);

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }
    private void insertReq(){
         String url="http://"+ UserLoged.url+"/ajout/classe";
        RequestQueue queue= Volley.newRequestQueue(getContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("nom", nomClasse.getText().toString());//trah runi
        params.put("niveau",niveau.getSelectedItem().toString());

        System.out.println(params);
        Log.d("Json","Json"+params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(getContext(),"Succès ajout classe",Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error+"----------------");
                        Toast.makeText(getContext(),error+"",Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }
    private void findClasseByName(int niv, String nom) {
        System.out.println(niv);
        System.out.println(nom);
        final Classe cl = new Classe();
        final String findParent = "http://" + UserLoged.url + "/findClasseByNivNom/"+niv+"/"+nom;

        RequestQueue queue = Volley.newRequestQueue(getContext());

        System.out.println("ici ici ici");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);
                    int i =0;
                    for ( i =0; i < array.length();i++) {

                        JSONObject o = array.getJSONObject(i);
                        System.out.println(o);

                        cl.setId(o.getInt("id"));
                        cl.setNiveau(o.getInt("niveau"));
                        cl.setNom(o.getString("nom"));

                        System.out.println("nejbed fil classe classe");

                        insertReqEmploi(o.getInt("id"));
                        insertReqCahier(o.getInt("id"));
                    }
                                    } catch (JSONException e) {
                    e.printStackTrace();

                    System.out.println("defaul classe");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();



            }
        });

        queue.add(stringRequest);

    }

    private void insertReqEmploi(int idC) {
        String urlAdd = "http://" + UserLoged.url + "/ajout/emploi";
        System.out.println("ici inserer Emploi");

        Emploi emp = new Emploi();
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        Map<String, Object> params = new HashMap<String, Object>();
        System.out.println(params);
        Log.d("Json", "Json" + params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlAdd +idC, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response + "++++++++++++++++++++");
                Toast.makeText(getContext(), "Succès d'ajout Emploi", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error + "----------------");
                        Toast.makeText(getContext(), error + "", Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }
    private void insertReqCahier(int idC) {
        String urlAdd = "http://" + UserLoged.url + "/ajout/cahier";
        System.out.println("ici inserer Cahier");
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        Map<String, Object> params = new HashMap<String, Object>();
        System.out.println(params);
        Log.d("Json", "Json" + params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlAdd +idC, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response + "++++++++++++++++++++");
                Toast.makeText(getContext(), "Succès d'ajout cahier", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error + "----------------");
                        Toast.makeText(getContext(), error + "", Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }


}
