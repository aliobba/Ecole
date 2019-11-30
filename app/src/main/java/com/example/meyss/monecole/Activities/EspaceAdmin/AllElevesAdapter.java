package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AllElevesAdapter extends ArrayAdapter<Eleve> {
    TextView parentMail,nomE,prenomE,naissance;
    Context context;
    List<Eleve> eleveList;

    public AllElevesAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.eleveList = objects;

    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Eleve el = (Eleve) getItem(position);


        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.eleve_item, null);

        nomE = (TextView) convertView.findViewById(R.id.nomE);
        prenomE = (TextView) convertView.findViewById(R.id.prenomE);
        naissance = (TextView) convertView.findViewById(R.id.naissance);
        parentMail = (TextView) convertView.findViewById(R.id.parent);


        findClasseName(el,nomE,prenomE,naissance,parentMail);

        return convertView  ;


    }

    private void findClasseName(final Eleve el, final TextView nom, final TextView prenom, final TextView dn, final TextView classenom) {

        final String findParent = "http://"+ UserLoged.url+"/findClasseById/"+el.getIdClasse()+"/";
        RequestQueue queue= Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    //  JSONArray array = new JSONArray(response);

                    JSONObject o = new JSONObject(response);

                    nom.setText(el.getNom());
                    prenom.setText(el.getPrenom());
                    dn.setText(el.getDateNaissance());
                    classenom.setText(o.getString("niveau")+o.getString("nom"));
                    System.out.println("nejbed fil mail " + classenom.getText());



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("erreuuuuuur");



                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),"verifier votre connexion",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);

    }



}