package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.meyss.monecole.Activities.EspaceParent.ModificationProfil;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilEnseignant extends Fragment {
    TextView nomPrenom;
    ImageButton modifProfil;
    CircleImageView image;
    SharedPreferences shared;
    public ProfilEnseignant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_profil, container, false);
        nomPrenom = (TextView) root.findViewById(R.id.nomPrenom);
        shared = this.getActivity().getSharedPreferences("log", Context.MODE_PRIVATE);
        String username = shared.getString("prenom","")+" "+shared.getString("nom","");
        nomPrenom.setText(username);
        image = (CircleImageView) root.findViewById(R.id.profil);
        Picasso.with(getActivity()).load("https://"+UserLoged.url+"/MonEcole-web/UploadedImages/"+shared.getString("image","")).into(image);
        modifProfil = (ImageButton) root.findViewById(R.id.modif);

        modifProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ModificationProfil.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
