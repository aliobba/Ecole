package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.example.meyss.monecole.Activities.EspaceParent.Profil;

import com.example.meyss.monecole.Activities.login;
import com.example.meyss.monecole.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class EspaceAdmine extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static FragmentTransaction f;
    public static String fileName = "MonEcole";
    public static File file = new File(Environment.getExternalStorageDirectory(), fileName);
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_admin);
        mydatabase = this.openOrCreateDatabase ("monecole",
                android.content.Context.MODE_PRIVATE, null);
        f = getSupportFragmentManager().beginTransaction();
        if (f.isEmpty()) {
            f.add(R.id.fragment1, new AdminAccueil());
        }
        f.commit();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerAd);
        final NavigationView navigationView = findViewById(R.id.navAd);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SharedPreferences sharePref = getSharedPreferences("log", this.MODE_PRIVATE);


        boolean success = false;
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "default folder doesn't exist, create it", Toast.LENGTH_SHORT).show();
            file.mkdir();

            success = true;
        }
        if (success) {
            Toast.makeText(getApplicationContext(), "folder created successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "failed to create directory", Toast.LENGTH_SHORT).show();
        }


        View header = navigationView.getHeaderView(0);
        CircleImageView image = (CircleImageView) header.findViewById(R.id.profil);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Profil();

                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment1, fragment);
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerAd);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId())

                {
                    case R.id.profil:
                        fragment = new Profil();

                        break;
                    case R.id.adAccueil:
                        fragment = new AdminAccueil();

                        break;
                    case R.id.adClasses:
                        fragment = new ClassesAdminListe();

                        break;
                    case R.id.adEleve:

                        fragment = new ElevesAdminList();
                        break;
                    case R.id.adEnseignant:

                        fragment = new ListeEnseignantAdmin();
                        break;
                    case R.id.adlogout:
                        sharePref.edit().clear().commit();
                        Intent logout = new Intent(EspaceAdmine.this, login.class);
                        startActivity(logout);
                        Toast toast = Toast.makeText(navigationView.getContext(), "Au Revoir !!", Toast.LENGTH_LONG);
                        toast.show();
                        break;
                }

                if (fragment != null) {
                    ft.replace(R.id.fragment1, fragment);
                    ft.commit();
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerAd);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d("EspaceAdmine", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
