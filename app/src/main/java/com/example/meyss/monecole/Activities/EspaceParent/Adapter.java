package com.example.meyss.monecole.Activities.EspaceParent;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    List<Eleve> eleves;
    Context context;
    private LayoutInflater inflater;
    private Fragment mFragment ;
    private Bundle mBundle;

    public Adapter(@NonNull Context context,List<Eleve> eleve) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.eleves=eleve;
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.eleve_parent_item, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.MyViewHolder myViewHolder, int position) {
        final Eleve e = eleves.get(position);
        System.out.println("----->"+e.getNom());

        myViewHolder.nom.setText(e.getNom());
        myViewHolder.emploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment = new Emploi();
                UserLoged.id_classe_eleve=e.getIdClasse();
                switchContent(R.id.fragment, mFragment);
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
        return eleves.size();
    }


    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof parents) {
            parents mainActivity = (parents) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nom;
        Button emploi;

        public MyViewHolder(View itemView) {
            super(itemView);
            nom = (TextView)itemView.findViewById(R.id.nom);
            emploi = (Button) itemView.findViewById(R.id.emploi);
        }
    }


}
