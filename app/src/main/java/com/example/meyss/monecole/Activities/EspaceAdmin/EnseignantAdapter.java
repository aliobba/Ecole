package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.R;

import java.util.ArrayList;

class EnseignantAdapter extends RecyclerView.Adapter<EnseignantHolder> {

    private ArrayList<Personne> enseignants;
    private Context context;


        public EnseignantAdapter(Context context, ArrayList<Personne> enseignants) {
            this.context = context;
            this.enseignants = enseignants;
        }
    @NonNull
    @Override
    public EnseignantHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.enseignant_item,viewGroup, false);
        return new EnseignantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnseignantHolder enseignantHolder, int i) {
        Personne enseignant = enseignants.get(i);
        enseignantHolder.setDetails(enseignant);
    }

    @Override
    public int getItemCount() {
        return enseignants.size();
    }
}
