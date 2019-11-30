package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meyss.monecole.Activities.EspaceParent.Accueil;
import com.example.meyss.monecole.Activities.EspaceParent.Profil;
import com.example.meyss.monecole.Activities.login;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EspaceEnseignant extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_enseignant);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final NavigationView navigationView = findViewById(R.id.nav);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SharedPreferences sharePref=getSharedPreferences("log",this.MODE_PRIVATE);

        View header = navigationView.getHeaderView(0);
        CircleImageView image = (CircleImageView) header.findViewById(R.id.profil);
        Picasso.with(this).load("https://"+UserLoged.url+"/MonEcole-web/UploadedImages/"+UserLoged.userConnected.getImg()).into(image);

        TextView prenom = (TextView) header.findViewById(R.id.prenom);
        String pre = sharePref.getString("prenom","");
        String name = sharePref.getString("nom","");
        prenom.setText(pre+" "+name);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_E,new AccueilEnseignant()).commit();

        image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new ProfilEnseignant();

                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_E, fragment);
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId())
                {
                    case R.id.accueil:
                        fragment = new AccueilEnseignant();
                        break;
                    case R.id.classe:
                        fragment = new ClasseEnseignant();
                        break;
                    case R.id.Absence:
                        fragment = new ClasseAbsence();
                        break;
                    case R.id.cahier:
                        fragment = new CahierClasseEnseignant();//CahierTexte();
                       /* Intent cahier = new Intent(getApplication(),CahierTexte.class);
                        startActivity(cahier);*/
                        break;
                    case R.id.logout:
                        sharePref.edit().clear().commit();
                        Intent logout = new Intent(getApplication(),login.class);
                        startActivity(logout);
                        Toast toast = Toast.makeText(navigationView.getContext(),"Au Revoir !!",Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                        break;
                }

                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_E, fragment);
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }

}
