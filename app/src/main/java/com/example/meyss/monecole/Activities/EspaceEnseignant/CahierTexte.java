package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meyss.monecole.Activities.login;
import com.example.meyss.monecole.R;

public class CahierTexte extends Fragment {

    public CahierTexte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_cahier_texte, container, false);
        final BottomNavigationView navigationView = root.findViewById(R.id.nav);

        getFragmentManager().beginTransaction().add(R.id.fragment_container,new ConsulterCahierTexte()).commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch(menuItem.getItemId())
                {
                    case R.id.consulter:
                        fragment = new ConsulterCahierTexte();
                        break;
                    case R.id.ajouter:
                        fragment = new EnseignantAjoutExercice();
                        break;
                    case R.id.modifier:
                         fragment = new ConsulterModifierCahier();
                        break;


                }

                if (fragment != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                }
                return true;
            }
        });

        return root;
    }
}
