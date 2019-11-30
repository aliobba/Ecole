package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.meyss.monecole.R;

import com.example.meyss.monecole.Entities.Eleve;

import java.util.List;

public class AdapterEleve extends ArrayAdapter<Eleve>{

    List<Eleve> eleves;
    Context context ;
    public AdapterEleve(@NonNull Context context, int resource, @NonNull List<Eleve> objects) {
        super(context, resource, objects);

        // objects.add(c1);
        //  objects.add(c2);
        this.context=context;
        this.eleves=objects;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.item_eleve_classe, null);

        TextView nom = (TextView) convertView.findViewById(R.id.nom);
        TextView dateNaissance = (TextView) convertView.findViewById(R.id.date);
        TextView numero = (TextView) convertView.findViewById(R.id.numero);

        numero.setText(String.valueOf(position+1));
        nom.setText(eleves.get(position).getNom()+" "+eleves.get(position).getPrenom());
        dateNaissance.setText(eleves.get(position).getDateNaissance());

        return convertView;
    }
}
