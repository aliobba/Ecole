package com.example.meyss.monecole.Activities.EspaceParent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meyss.monecole.Activities.EspaceEnseignant.AdapterConsultCahier;
import com.example.meyss.monecole.Entities.Exercice;
import com.example.meyss.monecole.Entities.Matiere;
import com.example.meyss.monecole.R;

import java.util.List;

public class AdapterConsultCahierEnfant extends RecyclerView.Adapter<AdapterConsultCahierEnfant.MyViewHolder> {
    List<Matiere> matieres;
    List<Exercice> exercices;
    Context context;
    private LayoutInflater inflater;
    public AdapterConsultCahierEnfant(@NonNull Context context, List<Exercice> exercice,List<Matiere> matiere) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.exercices=exercice;
        this.matieres=matiere;

    }

    @NonNull
    @Override
    public AdapterConsultCahierEnfant.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_consult_cahier_enfant, viewGroup, false);
        AdapterConsultCahierEnfant.MyViewHolder holder = new AdapterConsultCahierEnfant.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConsultCahierEnfant.MyViewHolder myViewHolder, int position) {
        final Exercice e = exercices.get(position);
        final Matiere m = matieres.get(position);

        myViewHolder.matiere.setText(m.getNom().toString());
        myViewHolder.date.setText(e.getDateRendu().toString());
        myViewHolder.description.setText(e.getConsigne().toString());

    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView date,description,matiere;

        public MyViewHolder(View itemView) {
            super(itemView);
            matiere = (TextView) itemView.findViewById(R.id.matiere);
            date = (TextView)itemView.findViewById(R.id.date);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}

