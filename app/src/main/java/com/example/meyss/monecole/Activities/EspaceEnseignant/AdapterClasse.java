package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.meyss.monecole.Activities.EspaceParent.Adapter;
import com.example.meyss.monecole.Activities.EspaceParent.Emploi;
import com.example.meyss.monecole.Activities.EspaceParent.parents;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import java.util.List;

public class AdapterClasse extends RecyclerView.Adapter<AdapterClasse.MyViewHolder> {

    List<Classe> classes;
    Context context;
    private LayoutInflater inflater;
    private Fragment mFragment ;

    public AdapterClasse(@NonNull Context context, List<Classe> classe) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.classes=classe;

    }

    @NonNull
    @Override
    public AdapterClasse.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.classe_enseignant_item, viewGroup, false);
        AdapterClasse.MyViewHolder holder = new AdapterClasse.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClasse.MyViewHolder myViewHolder, int position) {
        final Classe c = classes.get(position);

        myViewHolder.classe.setText(String.valueOf(c.getNiveau())+" "+c.getNom());
        myViewHolder.eleve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment = new ListeEleves();
                UserLoged.id_classe_eleve=c.getId();
                switchContent(R.id.fragment_E, mFragment);
                System.out.println(UserLoged.id_classe_eleve);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return classes.size();
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

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView classe;
        Button eleve;

        public MyViewHolder(View itemView) {
            super(itemView);
            classe = (TextView)itemView.findViewById(R.id.classe);
            eleve = (Button) itemView.findViewById(R.id.eleve);
        }
    }
}
