package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Exercice;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAccueil extends RecyclerView.Adapter<AdapterAccueil.MyViewHolder> {

    ArrayList<Classe> classes;
    ArrayList<sceance> seances;
    Context context;
    private LayoutInflater inflater;
    public AdapterAccueil(@NonNull Context context, ArrayList<sceance> seance,ArrayList<Classe> classe) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.seances=seance;
        this.classes=classe;

    }

    @NonNull
    @Override
    public AdapterAccueil.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_enseignant_accueil, viewGroup, false);
        AdapterAccueil.MyViewHolder holder = new AdapterAccueil.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAccueil.MyViewHolder myViewHolder, int position) {
        final sceance s = seances.get(position);
        final Classe c = classes.get(position);

        myViewHolder.debut.setText(s.getHeureDeb().toString());
        myViewHolder.fin.setText(s.getHeureFin().toString());
        myViewHolder.classe.setText(String.valueOf(c.getNiveau())+" "+c.getNom());

    }

    @Override
    public int getItemCount() {
        return seances.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView debut,fin,classe;

        public MyViewHolder(View itemView) {
            super(itemView);
            debut = (TextView)itemView.findViewById(R.id.debut);
            fin = (TextView) itemView.findViewById(R.id.fin);
            classe = (TextView) itemView.findViewById(R.id.classe);
        }
    }
}
