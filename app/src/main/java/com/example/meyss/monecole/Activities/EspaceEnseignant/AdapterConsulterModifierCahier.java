package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meyss.monecole.Entities.Exercice;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import java.util.List;

public class AdapterConsulterModifierCahier extends RecyclerView.Adapter<AdapterConsulterModifierCahier.MyViewHolder> {
    List<Exercice> exercices;
    Context context;
    private LayoutInflater inflater;
    private Fragment mFragment ;
    public AdapterConsulterModifierCahier(@NonNull Context context, List<Exercice> exercice) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.exercices=exercice;

    }

    @NonNull
    @Override
    public AdapterConsulterModifierCahier.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_consult_exercice, viewGroup, false);
        AdapterConsulterModifierCahier.MyViewHolder holder = new AdapterConsulterModifierCahier.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConsulterModifierCahier.MyViewHolder myViewHolder, int position) {
        final Exercice e = exercices.get(position);

        myViewHolder.date.setText(e.getDateRendu().toString());
        myViewHolder.description.setText(e.getConsigne().toString());
        myViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment = new ModifierCahier();//EnseignantAjoutExercice();
                UserLoged.exercice=e;
                switchContent(R.id.fragment_E, mFragment);
                System.out.println(UserLoged.id_classe_eleve);
            }
        });

    }

    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof EspaceEnseignant) {
            EspaceEnseignant mainActivity = (EspaceEnseignant) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView date,description;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            description = (TextView) itemView.findViewById(R.id.description);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }
}

