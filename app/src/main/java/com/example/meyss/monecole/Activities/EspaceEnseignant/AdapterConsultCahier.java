package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Exercice;
import com.example.meyss.monecole.R;

import java.util.List;

public class AdapterConsultCahier extends RecyclerView.Adapter<AdapterConsultCahier.MyViewHolder> {
    List<Exercice> exercices;
    Context context;
    private LayoutInflater inflater;
    public AdapterConsultCahier(@NonNull Context context, List<Exercice> exercice) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.exercices=exercice;

    }

    @NonNull
    @Override
    public AdapterConsultCahier.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_consult_exercice, viewGroup, false);
        AdapterConsultCahier.MyViewHolder holder = new AdapterConsultCahier.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConsultCahier.MyViewHolder myViewHolder, int position) {
        final Exercice e = exercices.get(position);

            myViewHolder.date.setText(e.getDateRendu().toString());
            myViewHolder.description.setText(e.getConsigne().toString());

    }

    @Override
    public int getItemCount() {
        return exercices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView date,description;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
