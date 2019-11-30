package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.meyss.monecole.R;

public class AdminAccueil extends Fragment {
    public static String username;
    String [] values ={"Classes","Elèves","Professeur"};
    int [] images ={R.drawable.classe,R.drawable.eleves,R.drawable.prof};

    public AdminAccueil() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.activity_admin_accueil, container, false);

        GridView gridview = (GridView) root.findViewById(R.id.accueilGrid);
        gridview.setAdapter(new ImageAdapter(root.getContext(),values,images));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                switch(position)

                {
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.fragment1,new ClassesAdminListe()).commit();

                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.fragment1,new ElevesAdminList()).commit();

                        break;
                    case 2:
                        getFragmentManager().beginTransaction().replace(R.id.fragment1,new ListeEnseignantAdmin()).commit();


                        break;

                }

            }
        });
        return root;
    }

}
