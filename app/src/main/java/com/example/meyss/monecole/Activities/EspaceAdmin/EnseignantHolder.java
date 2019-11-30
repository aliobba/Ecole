package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.meyss.monecole.Activities.EspaceParent.Profil;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EnseignantHolder extends RecyclerView.ViewHolder {

    private CircleImageView image;
    private TextView nom, prenom, mail, tel;
    Button delete;

    public EnseignantHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.imgProf);
        nom = itemView.findViewById(R.id.nomProf);
        prenom = itemView.findViewById(R.id.prenomProf);
        mail = itemView.findViewById(R.id.mailProf);
        tel = itemView.findViewById(R.id.telProf);
        delete = itemView.findViewById(R.id.deleteEns);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redraw the old selection and the new
                Intent redirection = new Intent(v.getContext(), ProfilEnseignant.class);
                redirection.putExtra("mailEns", mail.getText().toString());
                redirection.putExtra("nomEns", nom.getText().toString());
                redirection.putExtra("prenomEns", prenom.getText().toString());
                redirection.putExtra("telEns", tel.getText().toString());

                v.getContext().startActivity(redirection);
            }
        });

    }

    public void setDetails(final Personne personne) {
        /*if (personne.getImg() != null) {
            image.setImageURI(Uri.parse(personne.getImg()));
        }*/
        nom.setText(personne.getNom());
        prenom.setText(personne.getPrenom());
        mail.setText(personne.getMail());
        tel.setText(personne.getTel());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("delete prof");
            }
        });

    }
}
