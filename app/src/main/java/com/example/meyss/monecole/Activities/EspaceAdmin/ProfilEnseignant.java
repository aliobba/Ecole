package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.meyss.monecole.R;

public class ProfilEnseignant extends AppCompatActivity {
    TextView nom, prenom, mail, tel;
    Button appler,envoiMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_enseignant);
        setTitle("Enseignant");

        nom = findViewById(R.id.nomProftxt);
        prenom = findViewById(R.id.prenomProfttxt);
        mail = findViewById(R.id.mailProftxt);
        tel = findViewById(R.id.telProfTxt);

        System.out.println(getIntent().getExtras().getString("mailEns"));

        nom.setText(getIntent().getExtras().getString("nomEns"));
        prenom.setText(getIntent().getExtras().getString("prenomEns"));
        mail.setText(getIntent().getExtras().getString("mailEns"));
        tel.setText(getIntent().getExtras().getString("telEns"));
        appler = findViewById(R.id.appel);
        envoiMail = findViewById(R.id.mailTo);
        appler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intcall = new Intent();
                intcall.setAction(Intent.ACTION_VIEW);
                intcall.setData(Uri.parse("tel:"+getIntent().getExtras().getString("telEns")));
                                         /* if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                                                   != PackageManager.PERMISSION_GRANTED){
                                                return;
                                           }*/
                startActivity(intcall);
            }
        });

        envoiMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   /* Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{mail.getText().toString(),"aaaaa"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Mon Ecole");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }*/
                String mailto = "mailto:"+mail.getText().toString() +
                        "?cc=" + "" +
                        "&subject=" + Uri.encode("Mon Ecole") ;

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                }

            }
        });
    }
}
