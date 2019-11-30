package com.example.meyss.monecole.Activities.EspaceParent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Matiere;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;

import java.util.ArrayList;

public class AdapterAccueilParent extends RecyclerView.Adapter<AdapterAccueilParent.MyViewHolder>{


    ArrayList<Eleve> eleves;
    ArrayList<sceance> seances;
    ArrayList<Matiere> matieres;
    Context context;
    private LayoutInflater inflater;

    public AdapterAccueilParent(@NonNull Context context, ArrayList<sceance> seance, ArrayList<Matiere> matiere, ArrayList<Eleve> eleve) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.seances=seance;
        this.matieres=matiere;
        this.eleves=eleve;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_parent_accueil, viewGroup, false);
        AdapterAccueilParent.MyViewHolder holder = new AdapterAccueilParent.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final sceance s = seances.get(position);
        final Matiere m = matieres.get(position);
        final Eleve e = eleves.get(position);

        myViewHolder.debut.setText(s.getHeureDeb().toString());
        myViewHolder.fin.setText(s.getHeureFin().toString());
        myViewHolder.matiere.setText(m.getNom());
        myViewHolder.nom.setText(e.getPrenom().toString()+" "+e.getNom().toString());

    }

    @Override
    public int getItemCount() {
        return seances.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView debut,fin,matiere,nom;

        public MyViewHolder(View itemView) {
            super(itemView);
            debut = (TextView)itemView.findViewById(R.id.debut);
            fin = (TextView) itemView.findViewById(R.id.fin);
            matiere = (TextView) itemView.findViewById(R.id.matiere);
            nom = (TextView) itemView.findViewById(R.id.nom);
        }


    }
}
