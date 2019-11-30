package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



public class ClasseAdapter extends ArrayAdapter <Classe>{
    Context context;
    List<Classe> classeList;
    TextView nb;


    public ClasseAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.classeList = objects;

    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Classe cl = (Classe) getItem(position);


        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.class_item, null);

        TextView niveau = (TextView) convertView.findViewById(R.id.niveau);
        TextView classe = (TextView) convertView.findViewById(R.id.nomClassse);
        nb = (TextView) convertView.findViewById(R.id.nbEleve);
       // listeEleves(cl.getId());
        niveau.setText(String.valueOf(cl.getNiveau()));
        classe.setText(cl.getNom());

        classe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent redirection = new Intent(getContext(),ClasseAdmin.class);
                                    redirection.putExtra("id_classe",classeList.get(position).getId());
                                    redirection.putExtra("nom_classe",classeList.get(position).getNiveau()+classeList.get(position).getNom());
                                    getContext().startActivity(redirection);
            }
        });

        return convertView  ;


    }

}
